package com.travisit.travisitbusiness.vvm.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class MapVM extends ViewModel {
    public MutableLiveData<String>locationNameLiveData=new MutableLiveData<>();
    public MutableLiveData<LatLng>locationLiveData=new MutableLiveData<>();
}
