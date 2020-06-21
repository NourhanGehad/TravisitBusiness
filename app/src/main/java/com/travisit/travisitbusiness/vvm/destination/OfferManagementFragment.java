package com.travisit.travisitbusiness.vvm.destination;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.travisit.travisitbusiness.databinding.FragmentOfferDetailsBinding;
import com.travisit.travisitbusiness.databinding.FragmentOfferManagementBinding;
import com.travisit.travisitbusiness.vvm.AppActivity;

public class OfferManagementFragment extends Fragment {

    FragmentOfferManagementBinding binding;

    public OfferManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = FragmentOfferManagementBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}