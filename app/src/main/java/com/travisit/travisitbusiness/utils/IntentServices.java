package com.travisit.travisitbusiness.utils;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.travisit.travisitbusiness.data.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class IntentServices extends IntentService {
    private ResultReceiver resultReceiver;
    public IntentServices() {
        super("IntentServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        deliverAddress(intent);
    }
    private void deliverAddress(Intent intent){
        if(intent!=null){
            String errorMessage="";
            resultReceiver=intent.getParcelableExtra(Const.ADDRESS_RECEIVR);
            LatLng location=intent.getParcelableExtra(Const.LOCATION_DATA_EXTRA);
            if(location==null){
                return;
            }
            Geocoder geocoder=new Geocoder(this, Locale.getDefault());
            List<Address> addressList=null;
            try{
                addressList=geocoder.getFromLocation(location.latitude,location.longitude,1);
            }catch (Exception e){errorMessage=e.getMessage();}
            if (addressList==null&&addressList.isEmpty()){
                deliverResultToReceiver(Const.FAILURE_RESULT,errorMessage);
            }else {
                Address address=addressList.get(0);
                ArrayList<String> addressFragments=new ArrayList<>();
                for(int i=0;i<=address.getMaxAddressLineIndex();i++){
                    addressFragments.add(address.getAddressLine(i));
                }
                deliverResultToReceiver(Const.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),addressFragments));
            }
        }
    }
    private void deliverResultToReceiver(int requestCode,String messageAddress){
        Bundle bundle=new Bundle();
        bundle.putString(Const.RESULT_ADDRESS_KEY,messageAddress);
        resultReceiver.send(requestCode,bundle);
    }
}
