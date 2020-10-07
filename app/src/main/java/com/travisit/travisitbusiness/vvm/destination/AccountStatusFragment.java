package com.travisit.travisitbusiness.vvm.destination;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentAccountStatusBinding;
import com.travisit.travisitbusiness.databinding.FragmentResetPasswordCodeBinding;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.destination.CompleteProfileFragment;

public class AccountStatusFragment extends Fragment {
    public AccountStatusFragment() {
        // Required empty public constructor
    }

    private FragmentAccountStatusBinding binding;
    private Boolean isVerified;
    public SharedPrefManager preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        binding = FragmentAccountStatusBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new SharedPrefManager(getActivity());
        isVerified = AccountStatusFragmentArgs.fromBundle(getArguments()).getIsVerified();
        if(isVerified != null && isVerified == true){
            binding.layoutUnverified.getRoot().setVisibility(View.GONE);
            binding.layoutVerified.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.layoutUnverified.getRoot().setVisibility(View.VISIBLE);
            binding.layoutVerified.getRoot().setVisibility(View.GONE);
        }
        handleUserInteractions();
    }

    private void handleUserInteractions() {
        binding.layoutUnverified.layoutAccountUnverifiedBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.logout();
                Navigation.findNavController(getView()).navigate(R.id.action_from_account_status_to_auth_graph);
            }
        });
        binding.layoutVerified.layoutAccountVerifiedBtnGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}