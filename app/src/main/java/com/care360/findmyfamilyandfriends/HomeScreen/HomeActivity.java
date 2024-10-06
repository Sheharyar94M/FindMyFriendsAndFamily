package com.care360.findmyfamilyandfriends.HomeScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.care360.findmyfamilyandfriends.Application.App;
import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.HomeScreen.receiver.ActivityTransitionReceiver;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.DrivingFragment;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.DrivingViewModel;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.LocationFragment;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentSafety.FragmentSafety;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.MainSharedViewModel;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.subscription.SubscriptionFragment;
import com.care360.findmyfamilyandfriends.Permission.Permissions;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.ActivityTransitionUtils;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.care360.findmyfamilyandfriends.databinding.ActivityHomeBinding;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    private final int count = 0;
    private final int count2 = 0;
    private ActivityHomeBinding binding;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private AdLoader adLoader;
    private Fragment fragment = null;

    private ActivityRecognitionClient recognitionClient;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrivingViewModel drivingViewModel = new ViewModelProvider(this).get(DrivingViewModel.class);
        MainSharedViewModel sharedViewModel = new ViewModelProvider(this).get(MainSharedViewModel.class);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ActivityTransitionReceiver transitionReceiver = new ActivityTransitionReceiver();

        registerReceiver(transitionReceiver, new IntentFilter("com.care360.findmyfamilyandfriends.action.TRANSITIONS_DATA"));


        //getting locations and activity Data
        try {
            if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() != null) {

                //locations data default user

                //activity data
                drivingViewModel.getActivityStateListDay(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
                drivingViewModel.getActivityStateListWeek(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
                drivingViewModel.getActivityStateListMonth(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
                drivingViewModel.getActivityStateListYear(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
            }
            else{
                //activity data
                drivingViewModel.getActivityStateListDay(SharedPreference.getEmailPref());
                drivingViewModel.getActivityStateListWeek(SharedPreference.getEmailPref());
                drivingViewModel.getActivityStateListMonth(SharedPreference.getEmailPref());
                drivingViewModel.getActivityStateListYear(SharedPreference.getEmailPref());
            }

        } catch (Exception e) {

            Log.i("onDrive: ",e.getMessage());

            //activity data
            drivingViewModel.getActivityStateListDay(SharedPreference.getEmailPref());
            drivingViewModel.getActivityStateListWeek(SharedPreference.getEmailPref());
            drivingViewModel.getActivityStateListMonth(SharedPreference.getEmailPref());
            drivingViewModel.getActivityStateListYear(SharedPreference.getEmailPref());
        }


        //activity recognition
        recognitionClient = ActivityRecognition.getClient(this);

        //initializing binding
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

//        FirebaseApp.initializeApp(this);

        adRequest = new AdRequest.Builder().build();

        setAd();

        setContentView(view);

        fragment = new LocationFragment();
        replaceFragment(fragment);

        binding.navView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_location) {

                fragment = new LocationFragment();
                replaceFragment(fragment);
                item.setChecked(true);

            } else if (item.getItemId() == R.id.navigation_safety) {

                fragment = new FragmentSafety();
                replaceFragment(fragment);
                item.setChecked(true);

            } else if (item.getItemId() == R.id.navigation_driving) {

                fragment = new DrivingFragment();
                replaceFragment(fragment);
                item.setChecked(true);

            } else if (item.getItemId() == R.id.navigation_subscription) {

                fragment = new SubscriptionFragment();
                replaceFragment(fragment);
                item.setChecked(true);
            }

            return false;
        });

        requestForUpdates();


        //getting data of the selected circle
        try {
            sharedViewModel.getCirclesList();
            if (!SharedPreference.getCircleMembersList().isEmpty()) {
                Log.i("onCreateCircleList: ", SharedPreference.getCircleMembersList().toString());

                sharedViewModel.setCircleMembers(SharedPreference.getCircleMembersList());

            }
        } catch (Exception e) {
            Log.i("Members_List_Exception: ", e.getMessage());
        }

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_home_screen, fragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    private void setAd() {

        InterstitialAd.load(
                this,
                BuildConfig.INTERSTITIAL_AD_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {

                        Log.d("AdError", adError.toString());
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Log.d("AdError", "Ad was loaded.");
                        mInterstitialAd = interstitialAd;
                    }
                });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.REQUEST_CODE_ACTIVITY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "onRequestPermissionsResult() : permission granted");

                requestForUpdates();

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void requestForUpdates() {

        if (Permissions.hasActivityPermission(this)) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {

                recognitionClient.requestActivityTransitionUpdates(
                        ActivityTransitionUtils.getActivityTransitionRequest(),
                        getPendingIntent()

                ).addOnSuccessListener(unused -> {
                    Log.i("requestForUpdates: ", "successful Registration");

                }).addOnFailureListener(e -> {
                    Log.i("requestForUpdates: ", "Unsuccessful Registration");

                });

                Log.i("TAG", "checkLocationPermission() : app has location permission");

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, Constants.REQUEST_CODE_ACTIVITY);
            }

        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private PendingIntent getPendingIntent() {

        Intent intent = new Intent(App.getAppContext(), ActivityTransitionReceiver.class);

        return PendingIntent.getBroadcast(HomeActivity.this,
                Constants.REQUEST_CODE_INTENT_ACTIVITY_TRANSITION,
                intent,
                PendingIntent.FLAG_MUTABLE);

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(transitionReceiver);
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}