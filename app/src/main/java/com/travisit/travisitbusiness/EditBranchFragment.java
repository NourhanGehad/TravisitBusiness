package com.travisit.travisitbusiness;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.vvm.vm.BranchesVM;

import java.util.ArrayList;

public class EditBranchFragment extends Fragment {
    private BranchesVM vm;
    public EditBranchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProviders.of(this).get(BranchesVM.class);
        getArgFromMap();
        observeOnLifeDate();
       /* vm.myLocation.observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                Toast.makeText(getContext(),"Location is: "+latLng.latitude,Toast.LENGTH_LONG).show();
            }
        });*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_branch, container, false);
    }
    private void getArgFromMap(){
        EditBranchFragmentArgs fragmentArgs=EditBranchFragmentArgs.fromBundle(getArguments());
        if (fragmentArgs!=null&&fragmentArgs.getLocation()!=null){
            vm.myLocation.setValue(fragmentArgs.getLocation());
        }
    }
    private void observeOnLifeDate(){
        vm.myLocation.observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {

            }
        });
        vm.branchesMutableLiveData.observe(this, new Observer<ArrayList<Branch>>() {
            @Override
            public void onChanged(ArrayList<Branch> branches) {

            }
        });
    }
}