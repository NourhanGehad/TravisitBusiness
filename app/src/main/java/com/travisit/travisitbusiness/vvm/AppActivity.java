package com.travisit.travisitbusiness.vvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.ActivityAppBinding;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.observer.BottomNavigationControl;
import com.travisit.travisitbusiness.vvm.observer.IOnBackPressed;

import java.util.prefs.Preferences;

import javax.inject.Inject;

public class AppActivity extends AppCompatActivity implements BottomNavigationControl {
    private ActivityAppBinding binding;
    private View view;
    protected IOnBackPressed onBackPressedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_app_nav_host_fragment);
        NavigationUI.setupWithNavController(binding.activityAppBottomNavBar, navHostFragment.getNavController());
//        if(preferences.getUser() == null){
//
//        }
    }
    public void setOnBackPressedListener(IOnBackPressed onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }
    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.onBackPressed();
        } else
            super.onBackPressed();
    }
    /**
     * This function controls the visibility of the bottom navigation bar
     *
     */
    @Override
    public void changeBottomNavVisibility(Integer isVisible) {
        binding.activityAppBottomNavBar.setVisibility(isVisible);
        binding.activityAppFabAdd.setVisibility(isVisible);
    }
}
