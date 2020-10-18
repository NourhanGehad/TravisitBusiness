package com.travisit.travisitbusiness.vvm.destination;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentCompleteProfileBinding;
import com.travisit.travisitbusiness.databinding.FragmentHomeBinding;
import com.travisit.travisitbusiness.vvm.AppActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ((AppActivity)getActivity()).changeBottomNavVisibility(View.VISIBLE);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.fHomeLayoutInActiveSearchBar.getRoot().setVisibility(View.VISIBLE);
        binding.fHomeLayoutActiveSearchBar.getRoot().setVisibility(View.INVISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleUserInteractions();

    }
    private void handleUserInteractions() {
        binding.fHomeLayoutInActiveSearchBar.layoutInactiveSearchBarTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fHomeLayoutInActiveSearchBar.getRoot().setVisibility(View.INVISIBLE);
                binding.fHomeLayoutActiveSearchBar.getRoot().setVisibility(View.VISIBLE);
                binding.fHomeLayoutActiveSearchBar.layoutSearchBarEtSearch.requestFocus();
               // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        binding.fHomeLayoutActiveSearchBar.layoutSearchBarEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        binding.fHomeLayoutActiveSearchBar.layoutSearchBarIvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.fHomeLayoutActiveSearchBar.layoutSearchBarEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(getActivity(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void performSearch() {
        binding.fHomeLayoutActiveSearchBar.layoutSearchBarEtSearch.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.fHomeLayoutActiveSearchBar.layoutSearchBarEtSearch.getWindowToken(), 0);
        //...perform search
        Toast.makeText(getActivity(),"Search",Toast.LENGTH_LONG).show();
    }
}
