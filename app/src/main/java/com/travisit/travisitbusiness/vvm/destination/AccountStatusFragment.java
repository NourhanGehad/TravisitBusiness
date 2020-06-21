package com.travisit.travisitbusiness.vvm.destination;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travisit.travisitbusiness.databinding.FragmentAccountStatusBinding;
import com.travisit.travisitbusiness.databinding.FragmentResetPasswordCodeBinding;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.destination.CompleteProfileFragment;

public class AccountStatusFragment extends Fragment {
    public AccountStatusFragment() {
        // Required empty public constructor
    }

    private FragmentAccountStatusBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE);
        binding = FragmentAccountStatusBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO CHECK USER STATUS
       // binding.layoutUnverified.getRoot().setVisibility(View.GONE);
        //binding.layoutVerified.getRoot().setVisibility(View.VISIBLE);
        handleUserInteractions();
    }

    private void handleUserInteractions() {
        binding.layoutUnverified.layoutAccountUnverifiedBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.layoutVerified.layoutAccountVerifiedBtnGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}