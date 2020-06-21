package com.travisit.travisitbusiness.vvm.destination;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentBranchesBinding;
import com.travisit.travisitbusiness.databinding.FragmentResetPasswordBinding;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.utils.ChosenAction;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.adapter.BranchesAdapter;
import com.travisit.travisitbusiness.vvm.vm.AuthenticationVM;
import com.travisit.travisitbusiness.vvm.vm.BranchesVM;

import java.util.ArrayList;


public class BranchesFragment extends Fragment {
    FragmentBranchesBinding binding;
    private BranchesVM vm;
    public BranchesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.VISIBLE);
        binding = FragmentBranchesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = ViewModelProviders.of(this).get(BranchesVM.class);
        handleUserInteractions(view);
        vm.getBranches();
        vm.branchesMutableLiveData.observe(getActivity(), new Observer<ArrayList<Branch>>() {
            @Override
            public void onChanged(ArrayList<Branch> branches) {
                initRecyclerView(branches, view);
            }
        });
    }

    private void handleUserInteractions(View view) {
    }
    private void initRecyclerView(ArrayList<Branch> branches, View view){
        binding.fBranchesRvBranches.setAdapter(new BranchesAdapter(branches, getActivity(), new BranchesAdapter.SelectionPropagator() {
            @Override
            public void branchSelected(Branch branch, ChosenAction actionChosen) {
                if(actionChosen == ChosenAction.EDIT){
                    NavDirections action = BranchesFragmentDirections.actionFromBranchesToEditBranch().setBranch(branch);
                    Navigation.findNavController(view).navigate(action);
                } else if (actionChosen == ChosenAction.DELETE){

                }
            }
        }));

        binding.fBranchesRvBranches.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                RecyclerView.VERTICAL,
                false
        ));
    }
}
