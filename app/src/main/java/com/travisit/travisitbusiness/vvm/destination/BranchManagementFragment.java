package com.travisit.travisitbusiness.vvm.destination;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.data.Const;
import com.travisit.travisitbusiness.databinding.FragmentBranchManagementBinding;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.utils.IntentServices;
import com.travisit.travisitbusiness.utils.ManagementOption;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.BranchesVM;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BranchManagementFragment extends Fragment {
    /*private final String SHARED_PREFS = "sharedPrefs";
    private final String KEY = "BranchName";*/
    private ResultReceiver resultReceiver;
    private static  final int REQUEST_LOCATION_CODE=1;
    FragmentBranchManagementBinding binding;
    public static Branch branch;
    //Geocoder geocoder;
    private BranchesVM vm;
    Double selectedLatitude = 3.0;
    Double selectedLongitude = 3.0;
    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (getFieldText("name").length() == 0 ||
                    getFieldText("location").length() == 0 ) {
                binding.fBranchManagementMtbtnSave.setEnabled(false);
                binding.fBranchManagementMtbtnSave.setAlpha(0.5f);
            } else {
                binding.fBranchManagementMtbtnSave.setEnabled(true);
                binding.fBranchManagementMtbtnSave.setAlpha(1f);
            }
        }
    };
    public BranchManagementFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = FragmentBranchManagementBinding.inflate(inflater, container, false);
        resultReceiver=new AddressResultReceiver(new Handler());
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = ViewModelProviders.of(this).get(BranchesVM.class);
        branch = BranchManagementFragmentArgs.fromBundle(getArguments()).getBranch();
        if(branch == null){
            updateUI(ManagementOption.ADD);
        } else {
            updateUI(ManagementOption.EDIT);
        }
        handleUserInteractions(view);
        getArgFromMap();
        observeOnLocation();
    }
    private void updateUI(ManagementOption option){
        switch(option){
            case ADD:{
                binding.fBranchManagementTvTitle.setText(getString(R.string.add_branch_title));
                binding.fBranchManagementTvSubtitle.setText(getString(R.string.have_a_new_branch_q));
                binding.fBranchManagementMtbtnSave.setText(getString(R.string.add_now));
                break;
            }
            case EDIT: {
                binding.fBranchManagementTvTitle.setText(getString(R.string.edit_branch_title));
                binding.fBranchManagementTvSubtitle.setText(getString(R.string.have_an_update_in_branch_q));
                binding.fBranchManagementTietBranchName.setText(branch.getName());
                binding.fBranchManagementMtbtnSave.setText(getString(R.string.update_now));

                break;
            }
        }

        binding.fBranchManagementTietLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BranchesVM.branchNameTxt= getFieldText("name");
                if (((AppActivity)getActivity()).internet.getValue()){
                    showPermission();
                }else {
                    ((AppActivity)getActivity()).showSnakeBar("Internet Is Failed",5000,R.color.colorRedMessage);
                }
            }
        });
    }
    private void handleUserInteractions(View view){
        binding.fBranchManagementMtbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(branch == null){
                    createBranchObject();
                    vm.addBranch(branch);
                } else {
                    branch.setName(getFieldText("name"));
                    branch.setLatitude(selectedLatitude);
                    branch.setLongitude(selectedLongitude);
                    vm.editBranch(branch);
                    Log.d("branchid", branch.getId().toString());
                }
                vm.branchMutableLiveData.observe(getActivity(), new Observer<Branch>() {
                    @Override
                    public void onChanged(Branch branch) {
                        Navigation.findNavController(view).navigateUp();
                    }
                });
            }
        });
        binding.fBranchManagementTietBranchName.addTextChangedListener(watcher);
        binding.fBranchManagementTietLocation.addTextChangedListener(watcher);
    }
    private void createBranchObject() {
        branch = new Branch(getFieldText("name"), selectedLatitude ,selectedLongitude);
    }
    private String getFieldText(String fieldName) {
        switch (fieldName) {
            case "name":
                return binding.fBranchManagementTietBranchName.getText().toString();
            case "location":
                return binding.fBranchManagementTietLocation.getText().toString();
            default:
                return "invalid";
        }
    }
    private void getArgFromMap(){
        BranchManagementFragmentArgs fragmentArgs=BranchManagementFragmentArgs.fromBundle(getArguments());
        if (fragmentArgs!=null&&fragmentArgs.getLocation()!=null){
            vm.myLocation.setValue(fragmentArgs.getLocation());
           binding.fBranchManagementTietBranchName.setText(BranchesVM.branchNameTxt);
        }
    }
    private void observeOnLocation(){
        vm.myLocation.observe(getActivity(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                //returnLocationName(latLng);
                selectedLatitude=latLng.latitude;
                selectedLongitude=latLng.longitude;
                fetchLocationName(latLng);
            }
        });
        vm.locationName.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.fBranchManagementTietLocation.setText(s);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //Get Location Functions
    private void showPermission(){
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           // getMyLocation();
            Navigation.findNavController(this.getView()).navigate(R.id.action_branch_to_map);
        }else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_CODE) {
            if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                Navigation.findNavController(this.getView()).navigate(R.id.action_branch_to_map);
                Toast.makeText(getActivity(), "Granted", Toast.LENGTH_LONG).show();
            } else {
                //Return to Home fragment
                Toast.makeText(getActivity(), "Denied!!", Toast.LENGTH_LONG).show();
                //Navigation.findNavController(this.getView()).navigate(R.id.action_map_to_branch);
            }
        }
    }
    private void fetchLocationName(LatLng latLng){
        Intent intent=new Intent(getActivity(), IntentServices.class);
        intent.putExtra(Const.ADDRESS_RECEIVR,resultReceiver);
        intent.putExtra(Const.LOCATION_DATA_EXTRA,latLng);
        Log.e("NewMap","fgdfsddfgdfsdsfgfsd");
        getActivity().startService(intent);
    }
    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.e("NewMap","resultData.getString(Const.RESULT_DATA_KEY)");
            super.onReceiveResult(resultCode, resultData);
            if (resultCode==Const.SUCCESS_RESULT){
                Log.e("NewMap",resultData.getString(Const.RESULT_ADDRESS_KEY));
                vm.locationName.setValue(resultData.getString(Const.RESULT_ADDRESS_KEY));
            }else {
                Log.e("NewMapError",resultData.getString(Const.RESULT_ADDRESS_KEY));
            }
        }
    }
}