package com.travisit.travisitbusiness.vvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.ActivityAppBinding;
import com.travisit.travisitbusiness.utils.InternetConnection;
import com.travisit.travisitbusiness.vvm.observer.BottomNavigationControl;
import com.travisit.travisitbusiness.vvm.observer.IOnBackPressed;

public class AppActivity extends AppCompatActivity implements BottomNavigationControl {
    private ActivityAppBinding binding;
    private View view;
    protected IOnBackPressed onBackPressedListener;
    NavHostFragment navHostFragment;
    Snackbar snackbar;
    private IntentFilter internetIntentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        observeOnConnection();
        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_app_nav_host_fragment);
        NavigationUI.setupWithNavController(binding.activityAppBottomNavBar, navHostFragment.getNavController());
        handleUserInteractions();
//        if(preferences.getUser() == null){
//
//        }
    }

    private void handleUserInteractions() {
        binding.activityAppFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment f = getForegroundFragment();
//                Log.d("yup","f: "+f.getId());
                NavDestination destination = getFragment();
                //Log.d("yup","f id: "+String.valueOf(id));
            //    Fragment f = getSupportFragmentManager().findFragmentById(id);
              //  Log.d("yup","f name id: "+f.getId());
               // Log.d("yup","f name "AppActivity": "+f.get"AppActivity"());
                Log.d("yup","f name id: "+destination.getId());
              Log.d("yup","f name label: "+destination.getLabel());
                Log.d("yup","f name nav name: "+destination.getNavigatorName());

                if ("fragment_home".equals(destination.getLabel())) {
                    Log.d("yup", "clicked: " + "home");
                    navHostFragment.getNavController().navigate(R.id.action_to_add_offer);
                } else if ("fragment_branches".equals(destination.getLabel())) {
                    Log.d("yup", "clicked: " + "branch");
                    navHostFragment.getNavController().navigate(R.id.action_to_add_branch);
                } else if ("fragment_notifications".equals(destination.getLabel())) {
                } else if ("fragment_account".equals(destination.getLabel())) {
                }
            }
        });
    }
    public NavDestination getFragment(){
        return NavHostFragment.findNavController(getSupportFragmentManager().getPrimaryNavigationFragment().getFragmentManager().getFragments().get(0)).getCurrentDestination();
        //return navHostFragment == null ? null : navHostFragment.getNavController().getCurrentDestination().getId();
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
    public void changeBottomNavVisibility(Integer isVisible, Boolean hideFabAlone) {
        binding.activityAppBottomNavBar.setVisibility(isVisible);
        if(hideFabAlone){
            binding.activityAppFabAdd.setVisibility(View.GONE);
        } else {
            binding.activityAppFabAdd.setVisibility(isVisible);
        }

    }
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("AppActivity","Permission is granted1");
                return true;
            } else {

                Log.v("AppActivity","Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("AppActivity","Permission is granted1");
            return true;
        }
    }
    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("AppActivity","Permission is granted2");
                return true;
            } else {

                Log.v("AppActivity","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("AppActivity","Permission is granted2");
            return true;
        }
    }
    public void showSnakeBar(String txt,int duration,int textColor){
        snackbar=Snackbar.make(view,txt,Snackbar.LENGTH_INDEFINITE);
        snackbar.setDuration(duration);
        snackbar.setTextColor(this.getResources().getColor(textColor));
        snackbar.show();
    }
    public InternetConnection internet;
    public void observeOnConnection(){
        internet=new InternetConnection(getApplicationContext());
        internet.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    showSnakeBar("Internet Is Connected",5000,R.color.colorMediumSeaGreen);
                }else {
                    showSnakeBar("Internet Is Failed",5000,R.color.colorRedMessage);
                }
            }
        });
    }

}
