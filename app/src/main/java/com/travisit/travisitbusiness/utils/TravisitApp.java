package com.travisit.travisitbusiness.utils;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class TravisitApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
