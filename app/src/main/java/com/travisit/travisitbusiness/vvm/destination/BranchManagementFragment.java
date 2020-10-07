package com.travisit.travisitbusiness.vvm.destination;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentBranchManagementBinding;
import com.travisit.travisitbusiness.databinding.FragmentOfferManagementBinding;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.model.Offer;
import com.travisit.travisitbusiness.utils.ManagementOption;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.BranchesVM;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BranchManagementFragment extends Fragment {

    FragmentBranchManagementBinding binding;
    Branch branch;
    Geocoder geocoder;
    List<Address> addresses;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = FragmentBranchManagementBinding.inflate(inflater, container, false);
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
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

//                try {
//                    addresses = geocoder.getFromLocation(branch.getLatitude(), branch.getLongitude(), 1);
//                    String address = addresses.get(0).getAddressLine(0);
//                    binding.fBranchManagementTietLocation.setText(address);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                break;
            }
        }
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}