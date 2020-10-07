package com.travisit.travisitbusiness.vvm.destination;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.travisit.travisitbusiness.data.Const;
import com.travisit.travisitbusiness.databinding.FragmentCompleteProfileBinding;
import com.travisit.travisitbusiness.databinding.FragmentHomeBinding;
import com.travisit.travisitbusiness.model.Offer;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.adapter.OffersAdapter;
import com.travisit.travisitbusiness.vvm.vm.BranchesVM;
import com.travisit.travisitbusiness.vvm.vm.OffersVM;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public SharedPrefManager preferences;
    private OffersVM vm;
    private OffersAdapter adapter = null;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity)getActivity()).changeBottomNavVisibility(View.VISIBLE, false);
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
        preferences = new SharedPrefManager(getActivity());
        vm = ViewModelProviders.of(this).get(OffersVM.class);
        handleUserInteractions(view);
        updateUI();
        vm.getOffers();
        vm.offersMutableLiveData.observe(getActivity(), new Observer<ArrayList<Offer>>() {
            @Override
            public void onChanged(ArrayList<Offer> offers) {
                Log.d("here", String.valueOf(offers.size()));
                initRecyclerView(offers, view);
            }
        });
    }

    private void initRecyclerView(ArrayList<Offer> offers, View view) {
        adapter = new OffersAdapter(offers, getActivity(), new OffersAdapter.SelectionPropagator() {
            @Override
            public void offerSelected(Offer offer) {
                NavDirections action = HomeFragmentDirections.actionFromHomeToOfferDetails().setOffer(offer);
                Navigation.findNavController(view).navigate(action);
            }
        });
        binding.fragmentHomeRvOffers.setAdapter(adapter);
        binding.fragmentHomeRvOffers.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                RecyclerView.VERTICAL,
                false
        ));
        adapter.getFilter().filter("");
    }

    private void handleUserInteractions(View view) {
        binding.fHomeLayoutInActiveSearchBar.layoutInactiveSearchBarTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fHomeLayoutInActiveSearchBar.getRoot().setVisibility(View.INVISIBLE);
                binding.fHomeLayoutActiveSearchBar.getRoot().setVisibility(View.VISIBLE);
                binding.fHomeLayoutActiveSearchBar.layoutSearchBarEtSearch.requestFocus();
               // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(
                        InputMethodManager.SHOW_FORCED, 0);
            }
        });
        binding.fHomeSdvLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_from_home_to_profile);
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

    private void updateUI(){
        String userPhotoPath = Const.IMAGES_SERVER_ADDRESS + preferences.getUser().getLogo();
        binding.fHomeSdvLogo.setImageURI(Uri.parse(userPhotoPath));
    }
    private void performSearch() {
        binding.fHomeLayoutActiveSearchBar.layoutSearchBarEtSearch.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.fHomeLayoutActiveSearchBar.layoutSearchBarEtSearch.getWindowToken(), 0);
        //...perform search
        if(adapter != null){
            adapter.getFilter().filter(binding.fHomeLayoutActiveSearchBar.layoutSearchBarEtSearch.getText().toString());
        }
        //Toast.makeText(getActivity(),"Search",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
