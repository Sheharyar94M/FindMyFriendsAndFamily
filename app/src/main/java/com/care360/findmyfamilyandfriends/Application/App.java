package com.care360.findmyfamilyandfriends.Application;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class App extends Application {

    public static boolean IS_LOCATION_UPDATE_SAVED_TO_FIREBASE;

    private static App app;

    public static App getAppContext() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        IS_LOCATION_UPDATE_SAVED_TO_FIREBASE = false;
    }
}
