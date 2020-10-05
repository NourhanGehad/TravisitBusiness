package com.travisit.travisitbusiness.vvm.destination;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentMapsBinding;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.BranchesVM;
import com.travisit.travisitbusiness.vvm.vm.RegistrationVM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsFragment extends Fragment {
    private View view;
    private BranchesVM vm;
    private FragmentMapsBinding binding;
    private GoogleMap myMap;
    LatLng myLocation;
    private static  final int REQUEST_LOCATION_CODE=1;
    FusedLocationProviderClient fusedLocationProviderClient;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            myMap=googleMap;
            LatLng myLocation = new LatLng(-34, 151);
            addMarker(myLocation,"Sydny");
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        ((AppActivity)getActivity()).changeBottomNavVisibility(View.GONE);
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        this.view=view;
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        handleUserInteractions();
        confirmClick(view);
        keyboardSearchButton();
        vm = ViewModelProviders.of(this).get(BranchesVM.class);
    }
    @Override
    public void onStart() {
        super.onStart();
        binding.fMapLayoutInActiveSearchBar.getRoot().setVisibility(View.VISIBLE);
        binding.fMapLayoutActiveSearchBar.getRoot().setVisibility(View.INVISIBLE);
        showPermission();
    }
    private void addMarker(LatLng latLng,String locatioName) {
        // circle settings
        myMap.clear();
        float radiusM = (float) 10000.0; // your radius in meters
        // draw circle
        int d = 500; // diameter
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.colorBorderSummerSky));
        c.drawCircle(d/2, d/2, d/2, p);
        // generate BitmapDescriptor from circle Bitmap
        BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);
        // mapView is the GoogleMap
        myMap.addGroundOverlay(new GroundOverlayOptions().image(bmD).position(latLng,radiusM*2,radiusM*2).transparency(0.4f));
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.0f));
        myMap.addMarker(new MarkerOptions().position(latLng).title(locatioName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        myLocation=latLng;//passing location in Map
    }
    private void handleUserInteractions() {
        binding.fMapLayoutInActiveSearchBar.layoutInactiveSearchBarTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fMapLayoutInActiveSearchBar.getRoot().setVisibility(View.INVISIBLE);
                binding.fMapLayoutActiveSearchBar.getRoot().setVisibility(View.VISIBLE);
                binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.requestFocus();
                // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //performSearch();
                    return true;
                }
                return false;
            }
        });
        binding.fMapLayoutActiveSearchBar.layoutSearchBarIvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
       String locationName =binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.getText().toString();
       try {
           if (!locationName.isEmpty()){
               List<Address> addressList=null;
               Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());
               try {
                   addressList= geocoder.getFromLocationName(locationName,1);
               } catch (Exception e) {
                   e.printStackTrace();
               }
               Address address=addressList.get(0);
               LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
               addMarker(latLng,locationName);
           }
       }catch (Exception e){
           Toast.makeText(getContext(), "Sorry this is not Location", Toast.LENGTH_SHORT).show();
           e.printStackTrace();
       }
    }
    private void keyboardSearchButton(){
        TextView.OnEditorActionListener onEditorActionListener=new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_SEARCH){
                    performSearch();
                }
                return false;
            }
        };
        binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.setOnEditorActionListener(onEditorActionListener);
    }
    private void confirmClick(View view){
        binding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //performSearch();
                //vm.myLocation.setValue(myLocation);
                //Navigation.findNavController(view).navigate(R.id.action_map_to_branch);
                final NavController navController=Navigation.findNavController(view);
                MapsFragmentDirections.ActionMapToBranch actionMapToBranch= MapsFragmentDirections.actionMapToBranch().setLocation(myLocation);
                navController.navigate(actionMapToBranch);
            }
        });
    }
    //Get Location Functions
    private void showPermission(){
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
        }
    }
    @SuppressLint("MissingPermission")
    private LatLng getMyLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                String locatioName=returnLocationName(task.getResult());
                LatLng latLng=new LatLng(task.getResult().getLatitude(),task.getResult().getLongitude());
                addMarker(latLng,locatioName);
                //Toast.makeText(getActivity(), locatioName, Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    private String returnLocationName(Location location){
        String locationName="";
        if(location!=null){
            Geocoder geocoder=new Geocoder(getActivity(),Locale.getDefault());
            try {
                List<Address>addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if(addressList!=null&&addressList.size()>0){
                    if(addressList.get(0).getThoroughfare()!=null){
                        locationName+=addressList.get(0).getThoroughfare()+" ";
                    }
                    if(addressList.get(0).getLocality()!=null){
                        locationName+=addressList.get(0).getLocality()+" ";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        binding.fMapLayoutInActiveSearchBar.layoutInactiveSearchBarTvSearch.setText(locationName);
        return locationName;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_CODE) {
            if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                getMyLocation();
                Toast.makeText(getActivity(), "Granted", Toast.LENGTH_LONG).show();
            } else {
             //Return to Home fragment
                Toast.makeText(getActivity(), "Denied!!", Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).navigate(R.id.action_map_to_branch);
            }
        }
    }
}