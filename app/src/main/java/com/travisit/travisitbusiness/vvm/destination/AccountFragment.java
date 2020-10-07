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
import com.travisit.travisitbusiness.databinding.FragmentAccountBinding;
import com.travisit.travisitbusiness.databinding.FragmentAccountStatusBinding;
import com.travisit.travisitbusiness.databinding.FragmentHomeBinding;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;


public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;
    public SharedPrefManager preferences;
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity)getActivity()).changeBottomNavVisibility(View.VISIBLE, true);
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new SharedPrefManager(getActivity());
        handleUserInteractions(view);
    }
    private void handleUserInteractions(final View view) {
        binding.fAccountMtbtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_from_account_to_profile);
            }
        });
        binding.fAccountMtbtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.logout();
                Navigation.findNavController(getView()).navigate(R.id.action_from_account_logout);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
