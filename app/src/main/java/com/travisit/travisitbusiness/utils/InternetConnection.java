package com.travisit.travisitbusiness.utils;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class InternetConnection extends LiveData<Boolean>{
    private Context context;
    private android.net.ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallBack;
    private BroadcastReceiver networkReceiver ;
    public InternetConnection(Context context) {
        this.context = context;
        connectivityManager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateConnection();
            }

        };
    }
    private ConnectivityManager.NetworkCallback connectivityManagerCallBack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallBack = new  ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    postValue(true);
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    postValue(false);
                }
            };
        }
        return networkCallBack;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void lollipopNetworkConnection() {
        NetworkRequest.Builder requestBuilder = new NetworkRequest.Builder();
        requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET);
        connectivityManager.registerNetworkCallback(requestBuilder.build(), networkCallBack);
    }


    private void updateConnection() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected() == true){
           postValue(true);
        }else {
            postValue(false);
        }
    }

    @Override
    protected void onActive() {
        super.onActive();
        updateConnection();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallBack());
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lollipopNetworkConnection();
        }
        else {
            context.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    @Override
    protected void onInactive() {
        super.onInactive();
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                connectivityManager.unregisterNetworkCallback(connectivityManagerCallBack());
            } else {
                context.unregisterReceiver(networkReceiver);
            }
        }catch (Exception e){ }
    }


}
