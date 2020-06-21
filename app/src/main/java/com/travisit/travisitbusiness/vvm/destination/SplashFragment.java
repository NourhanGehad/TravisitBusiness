package com.travisit.travisitbusiness.vvm.destination;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {
    private FragmentSplashBinding binding;
    public SplashFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE);
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation aniFade = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
        binding.fSplashIvLogo.startAnimation(aniFade);
        binding.fSplashTvAppName.startAnimation(aniFade);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Navigation.findNavController(view).navigate(R.id.action_to_auth_graph);
            }

        }, 1500);
    }

    @Override
    public void onStart() {
        super.onStart();
        Client.reinstantiateClient(
                new SharedPrefManager(getActivity()).getUserToken()
        );
        Log.d("heysaysme",new SharedPrefManager(getActivity()).getUserToken());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}