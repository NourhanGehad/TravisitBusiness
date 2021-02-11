package com.travisit.travisitbusiness.vvm.destination;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.data.Const;
import com.travisit.travisitbusiness.databinding.FragmentMapsBinding;
import com.travisit.travisitbusiness.utils.IntentServices;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.MapVM;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MapsFragment extends Fragment {
    private MapVM VM;
    private View view;
    private FragmentMapsBinding binding;
    private GoogleMap myMap;
    LatLng myLocation;
    private static  final int REQUEST_LOCATION_CODE=1;
    //FusedLocationProviderClient fusedLocationProviderClient;
    private ResultReceiver resultReceiver;
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
        ((AppActivity)getActivity()).changeBottomNavVisibility(View.GONE,false);
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        this.view=view;
        VM= ViewModelProviders.of(this).get(MapVM.class);
        observeOnLivaData();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultReceiver=new AddressResultReceiver(new Handler());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        handleUserInteractions();
        confirmClick(view);
        keyboardSearchButton();
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
        //locationNames=locatioName;
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
        myLocation=new LatLng(latLng.latitude,latLng.longitude);//passing location in Map
    }
    private void handleUserInteractions() {
        binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.setTextColor(getActivity().getColor(R.color.colorSubHeaderBlack2));
        binding.fMapLayoutInActiveSearchBar.layoutInactiveSearchBarTvSearch.setHint("Search Location");
        binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.setHint("Search Location");
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
        binding.fMapLayoutActiveSearchBar.layoutSearchBarIvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchGoogleLocation();
            }
        });
        binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                  //  Toast.makeText(getActivity(), "Got the focus", Toast.LENGTH_LONG).show();
                    searchGoogleLocation();
                } else {
                   // Toast.makeText(getActivity(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
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
        binding.fMapLayoutInActiveSearchBar.layoutInactiveSearchBarIvFilter.setVisibility(View.GONE);

    }
    private void performSearch() {
        if (((AppActivity)getActivity()).internet.getValue()){
            String locationNameTxt =binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.getText().toString();
            try {
                if (!locationNameTxt.isEmpty()){
                    List<Address> addressList=null;
                    Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        addressList= geocoder.getFromLocationName(locationNameTxt,1);
                    } catch (Exception e) {
                        Log.e("Map",e.getMessage());
                    }
                    Address address=addressList.get(0);
                    LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                    //addMarker(latLng,returnLocationName(latLng));
                    VM.locationLiveData.setValue(latLng);
                    fetchLocationName(latLng);
                }
            }catch (Exception e){
                Toast.makeText(getContext(), "Sorry this is not Location", Toast.LENGTH_SHORT).show();
                Log.e("Map",e.getMessage());
            }
        }else {
            ((AppActivity)getActivity()).showSnakeBar("Internet Is Failed",5000,R.color.colorRedMessage);
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
                if (VM.locationNameLiveData.getValue().isEmpty()){
                    Toast.makeText(getContext(), "It Is Not a Specific Location", Toast.LENGTH_SHORT).show();
                }else {
                    final NavController navController=Navigation.findNavController(view);
                    MapsFragmentDirections.ActionMapToBranch actionMapToBranch= MapsFragmentDirections.actionMapToBranch().setLocation(myLocation);
                    navController.navigate(actionMapToBranch);
                }
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
        /*
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task!=null){
                    LatLng latLng=new LatLng(task.getResult().getLatitude(),task.getResult().getLongitude());
                    String locatioName=returnLocationName(latLng);
                    addMarker(latLng,locatioName);
                }
            }
        });*/
        //*****************************
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);
                        if(locationResult!=null&&locationResult.getLocations().size()>0){
                            int lastIndex=locationResult.getLocations().size()-1;
                            LatLng latLng=new LatLng(locationResult.getLocations().get(lastIndex).getLatitude(),locationResult.getLocations().get(lastIndex).getLongitude());
                            //locationNames.setValue(returnLocationName(latLng));
                            VM.locationLiveData.setValue(latLng);
                            //addMarker(latLng,locationNames);

                        }
                    }
                }, Looper.getMainLooper());
        return null;
    }
    private void fetchLocationName(LatLng latLng){
        Intent intent=new Intent(getActivity(), IntentServices.class);
        intent.putExtra(Const.ADDRESS_RECEIVR,resultReceiver);
        intent.putExtra(Const.LOCATION_DATA_EXTRA,latLng);
        Log.e("NewMap","fgdfsddfgdfsdsfgfsd");
        getActivity().startService(intent);
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
    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.e("NewMap","resultData.getString(Const.RESULT_DATA_KEY)");
            super.onReceiveResult(resultCode, resultData);
            if (resultCode==Const.SUCCESS_RESULT){
                Log.e("NewMap",resultData.getString(Const.RESULT_ADDRESS_KEY));
                VM.locationNameLiveData.setValue(resultData.getString(Const.RESULT_ADDRESS_KEY));
            }else {
                Log.e("NewMapError",resultData.getString(Const.RESULT_ADDRESS_KEY));
            }
        }
    }
    public void observeOnLivaData(){
        VM.locationLiveData.observe(getActivity(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                fetchLocationName(latLng);
            }
        });
        VM.locationNameLiveData.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                addMarker(VM.locationLiveData.getValue(),s);
                binding.fMapLayoutInActiveSearchBar.layoutInactiveSearchBarTvSearch.setText(s);
            }
        });
    }
    private void searchGoogleLocation(){
        Places.initialize(getActivity().getApplicationContext(),"AIzaSyArh72Mnyx-6RoNZ9KroZBON0p-_hfzHKc");
        List<Place.Field>locationList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
        Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,locationList).build(getActivity());
        startActivityForResult(intent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==getActivity().RESULT_OK){
            Place place=Autocomplete.getPlaceFromIntent(data);
            String address=place.getAddress();
            binding.fMapLayoutActiveSearchBar.layoutSearchBarEtSearch.setText(address);
            performSearch();
        }else if(requestCode==100&&resultCode== AutocompleteActivity.RESULT_ERROR){
            //error
            Status status=Autocomplete.getStatusFromIntent(data);
            Log.e(TAG, "onActivityResult: "+status.getStatusMessage());
        }
    }
}