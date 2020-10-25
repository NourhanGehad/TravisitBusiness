package com.travisit.travisitbusiness.utils;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class TravisitApp extends Application {
    private static TravisitApp INSTANCE;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Fresco.initialize(this);
    }
    public synchronized static TravisitApp getAppINSTANCE(){
        synchronized (TravisitApp.class) {
            if(INSTANCE==null){
                INSTANCE=new TravisitApp();
            }
        }
        return INSTANCE;
    }
}
