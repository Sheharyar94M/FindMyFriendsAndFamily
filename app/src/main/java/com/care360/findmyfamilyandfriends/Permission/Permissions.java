package com.care360.findmyfamilyandfriends.Permission;

import static com.care360.findmyfamilyandfriends.Util.Constants.REQUEST_CODE_CAMERA;
import static com.care360.findmyfamilyandfriends.Util.Constants.REQUEST_CODE_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.care360.findmyfamilyandfriends.Util.Constants;

public class Permissions {

    public static boolean hasCameraPermission(@NonNull Context context){
        String permission = Manifest.permission.CAMERA;

        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void getCameraPermission(@NonNull Activity activity){
        activity.requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_CODE_CAMERA);
    }

    public static boolean hasStoragePermission(@NonNull Context context){

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        return context.checkCallingOrSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                context.checkCallingOrSelfPermission(permissions[1]) == PackageManager.PERMISSION_GRANTED ;
    }

    public static void getStoragePermission(@NonNull Activity activity){
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        activity.requestPermissions(permissions,REQUEST_CODE_STORAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean hasLocationPermission(@NonNull Context context) {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};

        return  context.checkCallingOrSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                context.checkCallingOrSelfPermission(permissions[1]) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void getLocationPermission(@NonNull Activity activity){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        activity.requestPermissions(permissions, Constants.REQUEST_CODE_LOCATION);

        getActivityPermission(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void getActivityPermission(@NonNull Activity activity){
        String[] permissions = {Manifest.permission.ACTIVITY_RECOGNITION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        activity.requestPermissions(permissions, Constants.REQUEST_CODE_ACTIVITY);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean hasActivityPermission(@NonNull Context context) {

        String permission = Manifest.permission.ACTIVITY_RECOGNITION;

        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean hasContactPermission(@NonNull Context context) {

        String permission = Manifest.permission.READ_CONTACTS;

        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void getContactPermission(@NonNull Activity activity) {
        activity.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},Constants.REQUEST_CODE_CONTACTS);
    }

    public static boolean hasSmsPermission(@NonNull Context context) {
        String permission = Manifest.permission.SEND_SMS;
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
