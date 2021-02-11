package com.travisit.travisitbusiness.vvm.destination;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.data.Client;
import com.travisit.travisitbusiness.databinding.FragmentSplashBinding;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.BranchesVM;

public class SplashFragment extends Fragment {
    private FragmentSplashBinding binding;
    Business user;
    View view;
    public SplashFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation aniFade = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
        binding.fSplashIvLogo.startAnimation(aniFade);
        binding.fSplashTvAppName.startAnimation(aniFade);
        getUserFromShared();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Navigation.findNavController(getView()).navigate(R.id.action_from_splash_to_auth_graph);
               /* Log.d("wtf",userToken);*/
                try { // if We press back during delay it will crashed
                    navigation();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 1500);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getUserFromShared(){
        user = new SharedPrefManager(getActivity()).getUser();
        if(user != null){
            String userToken =  user.getToken();
            if(userToken != null) {
                Client.reinstantiateClient(userToken);
            }
        }
    }
    private void navigation(){
        if(user != null){
            Log.e("TAAAG",user.getApprovementStatus());
            if (user.getApprovementStatus() != null) {
                if (user.getApprovementStatus().contains("inComplete")) {
                    NavDirections action = SplashFragmentDirections.actionFromSplashToCompleteProfile().setUser(user);
                    Navigation.findNavController(view).navigate(action);
                } else if (user.getApprovementStatus().contains("pending")) {
                    NavDirections action = SplashFragmentDirections.actionFromSplashToShowAccountStatus().setIsVerified(false);;
                    Navigation.findNavController(view).navigate(action);
                } else {
                    //Navigation.findNavController(getView()).navigate(R.id.action_from_splash_to_home);
                    if (user.getBranchesCount() == null || user.getBranchesCount() < 1) {
                        NavDirections action = SplashFragmentDirections.actionFromSplashToShowAccountStatus().setIsVerified(true);
                        Navigation.findNavController(view).navigate(action);
                    } else {
                        Navigation.findNavController(getView()).navigate(R.id.action_from_splash_to_home);
                    }
                }
            }         } else {
            Navigation.findNavController(getView()).navigate(R.id.action_from_splash_to_auth_graph);
        }
    }
}