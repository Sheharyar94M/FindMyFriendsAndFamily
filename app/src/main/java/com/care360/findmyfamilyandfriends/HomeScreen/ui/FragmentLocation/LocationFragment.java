package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.care360.findmyfamilyandfriends.Application.App;
import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.HomeScreen.receiver.ActivityTransitionReceiver;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.AddMember.AddMemberActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Battery.BatteryStatusModelClass;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers.BottomSheetMemberAdapter;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers.MemberDetail;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers.MemberLocationActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Chat.ChatDashboardActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.JoinCircle.CircleModel;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.JoinCircle.CircleToolbarAdapter;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.JoinCircle.CreateCircle.CreateCircleMainActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.JoinCircle.JoinCircleMainActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Settings.SettingsActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.MainSharedViewModel;
import com.care360.findmyfamilyandfriends.Permission.Permissions;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.ActivityTransitionUtils;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.care360.findmyfamilyandfriends.WorkManager.CircleExpiryDateWorker;
import com.care360.findmyfamilyandfriends.WorkManager.LocationUpdateWorker;
import com.care360.findmyfamilyandfriends.databinding.CustomMarkerBinding;
import com.care360.findmyfamilyandfriends.databinding.FragmentLocationBinding;
import com.care360.findmyfamilyandfriends.databinding.LayoutBottomSheetMapTypeBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@RequiresApi(api = Build.VERSION_CODES.O)
@AndroidEntryPoint
public class LocationFragment extends Fragment implements OnMapReadyCallback, CircleToolbarAdapter.OnToolbarCircleClickListener, LocationListener, BottomSheetMemberAdapter.OnAddedMemberClickInterface, BottomSheetMemberAdapter.OnAddNewMemberInterface {
    private static final String TAG = "FRAG_LOCATION";
    //show and hide extended toolbar view animations
    Animation showToolbarExtAnim, hideToolbarExtAnim;
    private FragmentLocationBinding binding;
    ActivityResultLauncher<Intent> addMemberResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK) {

            Intent intent = result.getData();

            if (intent != null) {

                boolean isToolbarAddMemberBtnClicked = intent.getBooleanExtra(Constants.ADD_MEMBER_BUTTON_CLICKED, false);

                if (isToolbarAddMemberBtnClicked) {

                    //delay the shrunk to give an animation type look
                    delayCircleShrunkView();

                } else {

                    //collapse the bottom navigation
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View bottomSheet = binding.bottomSheetMembers.consBottomSheet;
                            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }, 300);
                }
            }
        }
    });
    ActivityResultLauncher<Intent> createNewCircleResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK) {

            //delay the shrunk to give an animation type look
            delayCircleShrunkView();

            Intent intent = result.getData();

            if (intent != null) {

                boolean isCircleCreated = intent.getBooleanExtra(Constants.IS_CIRCLE_CREATED, false);

                if (isCircleCreated) {

                    if (!SharedPreference.getCircleName().equals(Constants.NULL)){
                        binding.toolbar.textViewCircleName.setText(SharedPreference.getCircleName());
                        binding.toolbarExtendedView.txtCircleName.setText(SharedPreference.getCircleName());
                    }
                    else{
                        binding.toolbar.textViewCircleName.setText("Select circle");
                        binding.toolbarExtendedView.txtCircleName.setText("Select circle");
                    }

//                    //setting the data
//                    try {
//                        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() != null) {
//                            getDetailDataFromFirebase();
//
//                        }
//                    } catch (Exception e) {
//                        Log.i("LocationFragment: ", "Installed again!");
//                    }

                    Toast.makeText(requireContext(), "Circle Created Successfully.", Toast.LENGTH_SHORT).show();
                }
            }

        }
    });
    ActivityResultLauncher<Intent> joinCircleResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK) {

            Intent intent = result.getData();

            if (intent != null) {

                //delay the shrunk to give an animation type look
                delayCircleShrunkView();

                //sets the joined circle name onto toolbar & extended toolbar view
                if (!SharedPreference.getCircleName().equals(Constants.NULL)){
                    binding.toolbar.textViewCircleName.setText(SharedPreference.getCircleName());
                    binding.toolbarExtendedView.txtCircleName.setText(SharedPreference.getCircleName());
                }
                else{
                    binding.toolbar.textViewCircleName.setText("Select circle");
                    binding.toolbarExtendedView.txtCircleName.setText("Select circle");
                }

//                // setting the data
//                try {
//                    if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() != null) {
//                        getDetailDataFromFirebase();
//
//                    }
//                } catch (Exception e) {
//                    Log.i("LocationFragment: ", "Installed again!");
//                }

            }
        } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
            //when back pressed is called from 'JoinCircleMainActivity'
            delayCircleShrunkView();
        }

    });
    private FusedLocationProviderClient mLocationClient;
    private GoogleMap mGoogleMap;
    private Location location;
    //recyclerview of extended toolbar
    private RecyclerView circleSelectionRecyclerView;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private NotificationManager nMN;
    private NotificationChannel channel;
    private Dialog dialog;
    private Dialog dialogBackground;
    Dialog dialogActivity;
    private ActivityRecognitionClient recognitionClient;
    private ActivityTransitionReceiver transitionReceiver;
    private MainSharedViewModel sharedViewModel;
    private BottomSheetMemberAdapter bottomSheetMemberAdapter;
    private int clicks=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initializing view binding
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(MainSharedViewModel.class);

        View view = binding.getRoot();

        //Ads
        MobileAds.initialize(requireContext());

        adRequest = new AdRequest.Builder().build();

        setAd();

        BannerAds();

        bottomSheetMemberAdapter = new BottomSheetMemberAdapter(requireContext(), this, this);

        //initializing map
        initializeMap();

        transitionReceiver = new ActivityTransitionReceiver();

        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(transitionReceiver, new IntentFilter("com.care360.findmyfamilyandfriends.action.TRANSITIONS_DATA"));

        recognitionClient = ActivityRecognition.getClient(requireContext());

        dialog = new Dialog(requireActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.layout_permission_dialog);
        dialog.setCancelable(false);

        dialogBackground = new Dialog(requireActivity());
        dialogBackground.setContentView(R.layout.layout_location_dialog);
        dialogBackground.setCancelable(false);

        //Activity
        dialogActivity = new Dialog(requireContext());
        dialogActivity.setContentView(R.layout.layout_permission_dialog2);

        //starting and showing shimmer
        binding.bottomSheetMembers.shimmerLayout.startShimmer();
        binding.bottomSheetMembers.shimmerLayout.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    && !Permissions.hasActivityPermission(requireContext())) {

                //setting the transparent background
                dialogActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //this sets the width of dialog to 90%
                DisplayMetrics displayMetrics = new DisplayMetrics();
                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = (int) (displayMetrics.widthPixels * 0.9);

                //setting the width and height of alert dialog
                dialogActivity.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);


                AppCompatButton buttonSettings = dialogActivity.findViewById(R.id.btn_settings_perm_dialog);

                buttonSettings.setOnClickListener(v -> {

                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, Constants.REQUEST_CODE_ACTIVITY);

                    dialogActivity.dismiss();
                });

                dialogActivity.setCancelable(true);
                dialogActivity.show();

            } else {
                // this condition will save location update to firebase for 1 time throughout the application lifecycle.
                if (!App.IS_LOCATION_UPDATE_SAVED_TO_FIREBASE) {
                    //checking location permission
                    checkLocationPermission();

                    // updates the value from false to true
                    App.IS_LOCATION_UPDATE_SAVED_TO_FIREBASE = true;
                }
            }
        }

        //recyclerview initialization
        circleSelectionRecyclerView = binding.toolbarExtendedView.recyclerViewCircle;

        //items click listenerl
        clickListeners();

        //work manager for updating user location every hour
        periodicLocationUpdated();

        //work manager for checking circle code expiry date every 8 hours
        periodicCircleCodeChecker();

        nMN = (NotificationManager) requireActivity().getSystemService(NOTIFICATION_SERVICE);
        channel = new NotificationChannel("com.care360.findmyfamilyandfriends", "updated location!", NotificationManager.IMPORTANCE_HIGH);


        // setting the circle name to toolbar & toolbar extended view
        if (!SharedPreference.getCircleName().equals(Constants.NULL)){
            binding.toolbar.textViewCircleName.setText(SharedPreference.getCircleName());
            binding.toolbarExtendedView.txtCircleName.setText(SharedPreference.getCircleName());
        }
        else{
            binding.toolbar.textViewCircleName.setText("Select circle");
            binding.toolbarExtendedView.txtCircleName.setText("Select circle");
        }

        //get and observe circles list
        requireActivity().runOnUiThread(() -> {
            sharedViewModel.getCircleModelMutableLiveData().observe(getViewLifecycleOwner(), circleModels -> {

                Log.i("circleListObserver: ", String.valueOf(circleModels.size()));

                if (!circleModels.isEmpty()) {

                    selectCircleRecyclerview(circleModels);

                    binding.bottomSheetMembers.noResultsL.setVisibility(View.GONE);
                    binding.bottomSheetMembers.noListViewL.setVisibility(View.GONE);
                    binding.bottomSheetMembers.recyclerBottomSheetMember.setVisibility(View.VISIBLE);

                    //stopping and hiding shimmer
                    binding.bottomSheetMembers.shimmerLayout.stopShimmer();
                    binding.bottomSheetMembers.shimmerLayout.setVisibility(View.GONE);

                } else {
                    Snackbar snackbar = Snackbar.make(requireView(), "No circles found!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", null);
                    snackbar.setActionTextColor(Color.WHITE);

                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(requireActivity().getColor(R.color.orange));

                    TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(18f);
                    snackbar.show();
                }

            });
        });

        setBottomSheetMembersRecyclerView();

    }

    private AdSize getAdaptiveAdSize() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        float density = getResources().getDisplayMetrics().density;
        int widthDp = (int) (widthPixels / density);
        int adWidth = Math.min(widthDp, 640);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireContext(), adWidth);
    }

    public void BannerAds() {
        AdView adView;
        AdSize adaptiveAdSize = getAdaptiveAdSize();
        adView = new AdView(requireContext());
        adView.setAdUnitId(BuildConfig.BANNER_AD_ID);
        adView.setAdSize(adaptiveAdSize);
        binding.adaptiveBanner.addView(adView);
        adView.loadAd(adRequest);
    }

    private void periodicLocationUpdated() {

//        showNotification();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicLocationUpdateRequest = new PeriodicWorkRequest.Builder(LocationUpdateWorker.class, 60, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager
                .getInstance(requireActivity().getApplicationContext())
                .enqueueUniquePeriodicWork("locUpdate", ExistingPeriodicWorkPolicy.KEEP, periodicLocationUpdateRequest);

    }

    private void periodicCircleCodeChecker() {

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicCircleCodeRequest = new PeriodicWorkRequest.Builder(CircleExpiryDateWorker.class, 8, TimeUnit.HOURS)
                .setConstraints(constraints).build();

        WorkManager.getInstance(requireActivity().getApplicationContext())
                .enqueueUniquePeriodicWork("circleInfoUpdate", ExistingPeriodicWorkPolicy.KEEP, periodicCircleCodeRequest);
    }

    private void initializeMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(binding.map.getId());

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //when this listener is called, the live location icon visibility will be set to VISIBLE
        mGoogleMap.setOnCameraMoveListener(() -> binding.consLiveLoc.setVisibility(View.VISIBLE));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"MissingPermission", "InlinedApi"})
    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Fine Location permission is granted
            // Check if current android version >= 11, if >= 11 check for Background Location permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Background Location Permission is granted so do your work here
                    //getting the current location
                    Commons.isGpsEnabled(requireActivity(), isSuccessful -> {

                        if (isSuccessful) {
                            Log.i(TAG, "checkLocationPermission() : gps already enabled");

                            //fetch the location
                            getLocationThroughLastKnownApproach();
                        } else {
                            Log.i(TAG, "checkLocationPermission() : gps not enabled");

                            //displays the built in dialog
                            googleDefaultGPSDialog();
                        }
                    });
                } else {
                    // Ask for Background Location Permission
                    checkBackgroundLocation();
                }
            }
        } else {
            // Fine Location Permission is not granted so ask for permission
            if (SharedPreference.getFirstRun()){
                askForLocationPermission();
            }
            else{
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.layout_location_dialog_two);

                //setting the transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //this sets the width of dialog to 90%
                DisplayMetrics displayMetrics = new DisplayMetrics();
                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = (int) (displayMetrics.widthPixels * 0.9);

                //setting the width and height of alert dialog
                dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);


                AppCompatButton ok = dialog.findViewById(R.id.btn_settings_perm_dialog_location);

                ok.setOnClickListener(v1 -> {

                    openSettings();

                    dialog.dismiss();
                });

                dialog.setCancelable(true);
                dialog.show();
            }
        }
    }

    private void askForLocationPermission() {

        dialog.dismiss();

        CardView buttonSettings = dialog.findViewById(R.id.agree_btn);

        buttonSettings.setOnClickListener(v -> {

            if (clicks < 2){
                clicks++;

                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        requestLocationPermission();

                    } else {
                        // No explanation needed, we can request the permission.
                        requestLocationPermission();
                    }
                } else {
                    checkBackgroundLocation();
                }
            }
            else{

                SharedPreference.setFirstRun(false);

                openSettings();

            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }


    private void checkBackgroundLocation() {

        dialog.dismiss();

        //setting the transparent background
        dialogBackground.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //this sets the width of dialog to 90%
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.9);

        //setting the width and height of alert dialog
        dialogBackground.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        AppCompatButton buttonSettings = dialogBackground.findViewById(R.id.btn_settings_loc_dialog);

        buttonSettings.setOnClickListener(v -> {

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestBackgroundLocationPermission();
                }

            } else {
                checkLocationPermission();
            }

            dialogBackground.dismiss();
        });

        dialogBackground.setCancelable(true);
        dialogBackground.show();

    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, Constants.BACKGROUND_LOCATION_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, Constants.BACKGROUND_LOCATION_PERMISSION_CODE);
        }
    }

    ActivityResultLauncher<IntentSenderRequest> gpsResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {

        if (result.getResultCode() == RESULT_OK) {
            Log.i(TAG, "gpsResultLauncher : gps permission allowed");
            //fetch the location
            checkLocationPermission();
        } else {
            //show the dialog again
            Commons.isGpsEnabled(requireActivity(), isSuccessful -> {

                if (!isSuccessful) {
                    Log.i(TAG, "gpsResultLauncher : gps permission denied");
                    //displays the built in dialog
                    googleDefaultGPSDialog();
                }
            });
        }
    });

    public void openSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);
        requireActivity().startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.REQUEST_CODE_LOCATION) {

            dialog.dismiss();
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // User granted location permission

                dialog.dismiss();
                // Now check if android version >= 11, if >= 11 check for Background Location Permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // Background Location Permission is granted so do your work here
                        //getting the current location

                        Commons.isGpsEnabled(requireActivity(), isSuccessful -> {

                            if (isSuccessful) {
                                Log.i(TAG, "checkLocationPermission() : gps already enabled");

                                //fetch the location
                                getLocationThroughLastKnownApproach();
                            } else {
                                Log.i(TAG, "checkLocationPermission() : gps not enabled");

                                //displays the built in dialog
                                googleDefaultGPSDialog();
                            }
                        });
                    } else {
                        // Ask for Background Location Permission
                        checkBackgroundLocation();
                    }
                }
            } else {

                // User denied location permission
                if (SharedPreference.getFirstRun()){
                    askForLocationPermission();
                }
                else{
                    Dialog dialog = new Dialog(requireContext());
                    dialog.setContentView(R.layout.layout_location_dialog_two);
                    dialog.setCancelable(false);

                    //setting the transparent background
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //this sets the width of dialog to 90%
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int width = (int) (displayMetrics.widthPixels * 0.9);

                    //setting the width and height of alert dialog
                    dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);


                    AppCompatButton ok = dialog.findViewById(R.id.btn_settings_perm_dialog_location);

                    ok.setOnClickListener(v1 -> {

                        openSettings();
                        dialog.dismiss();
                    });

                    dialog.setCancelable(true);
                    dialog.show();
                }

            }
        } else if (requestCode == Constants.BACKGROUND_LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // User granted for Background Location Permission.
                dialog.dismiss();
                Commons.isGpsEnabled(requireActivity(), isSuccessful -> {

                    if (isSuccessful) {
                        Log.i(TAG, "checkLocationPermission() : gps already enabled");

                        //fetch the location
                        getLocationThroughLastKnownApproach();
                    } else {
                        Log.i(TAG, "checkLocationPermission() : gps not enabled");

                        //displays the built in dialog
                        googleDefaultGPSDialog();
                    }
                });
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    checkBackgroundLocation();
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_ACTIVITY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "onRequestPermissionsResult() : permission granted");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestForUpdates();
                }

            } else {

                dialog.dismiss();

                //setting the transparent background
                dialogActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //this sets the width of dialog to 90%
                DisplayMetrics displayMetrics = new DisplayMetrics();
                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = (int) (displayMetrics.widthPixels * 0.9);

                //setting the width and height of alert dialog
                dialogActivity.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);


                AppCompatButton buttonSettings = dialogActivity.findViewById(R.id.btn_settings_perm_dialog);

                buttonSettings.setOnClickListener(v -> {

                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, Constants.REQUEST_CODE_ACTIVITY);

                    dialogActivity.dismiss();
                });

                dialogActivity.setCancelable(true);
                dialogActivity.show();
            }
        }
    }

    private void googleDefaultGPSDialog() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000).setFastestInterval(3000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder locationBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse> responseTask = LocationServices.getSettingsClient(requireActivity().getApplicationContext())
                .checkLocationSettings(locationBuilder.build());

        responseTask.addOnCompleteListener(task -> {

            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);

                // gps is already enabled
                Log.i(TAG, "googleDefaultGPSDialog() : gps is already enabled");

            } catch (ApiException e) {
                if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;

                    IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(resolvableApiException.getResolution()).build();
                    gpsResultLauncher.launch(intentSenderRequest);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private void getLocationThroughLastKnownApproach() {

        Log.i(TAG, "getLocationThroughLastKnownApproach() ");

        // this function will call LocationListener overridden method when location changes
        startLocationUpdates();

        mLocationClient.getLastLocation().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                location = task.getResult();

                if (location != null) {

                    Log.i(TAG, "getLocationThroughLastKnownApproach() -> location != null");

                    // saves the location update in firebase
                    try {
                        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() != null) {
                            saveLocationInFirebase(location);
                            setMarkers(null);
                        }

                    } catch (Exception e) {
                        Log.i("saveLocationInFirebase: ", "installed again");
                    }
//                    try {
//                        showNotification(location);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }

                } else {
                    Log.i(TAG, "getLocationThroughLastKnownApproach() -> location == null");

                    /*
                        if task result returned has null location (case like when gps is turned on, it will first return null and after sometime location won't be null)
                        if that's the case, then will get location through mLocationClient.getCurrentLocation (currentLocationRequest, cancellationToken)
                    */
                    getLocationThroughCurrentLocationApproach();
                }
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private void getLocationThroughCurrentLocationApproach() {

        Log.i(TAG, "getLocationThroughCurrentLocationApproach()");

        CurrentLocationRequest currentLocationRequest = new CurrentLocationRequest.Builder().build();

        CancellationToken cancellationToken = new CancellationToken() {
            @Override
            public CancellationToken onCanceledRequested(OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        };

        mLocationClient.getCurrentLocation(currentLocationRequest, cancellationToken).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                location = task.getResult();

                if (location != null) {
                    Log.i(TAG, "getLocationThroughCurrentLocationApproach() -> location != null");

                    // saves the location update in firebase
                    try {
                        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() != null) {
                            saveLocationInFirebase(location);
                            setMarkers(null);
                        }

                    } catch (Exception e) {
                        Log.i("saveLocationInFirebase: ", "installed again");
                    }
//                    try {
//                        showNotification(location);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        Log.i(TAG, "startLocationUpdates()");

        //location manager method
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        // 60000 milliseconds = 1 minute = 120000 = 2 minute
        // 1000 milliseconds = 1 second
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1200000, 500, this);

    }

    // LocationListener overridden method
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i(TAG, "onLocationChanged()");

        // saves the location in firebase firestore
        try {
            if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() != null) {
                saveLocationInFirebase(location);
                setMarkers(null);
            }
        } catch (Exception e) {
            Log.i("saveLocationInFirebase: ", "installed again");
        }
    }

    private void saveLocationInFirebase(Location location) {

        Context context = App.getAppContext();

        String currentUserEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        BatteryStatusModelClass batteryStatus = Commons.getCurrentBatteryStatus(context);

        // location data
        SharedPreference.setLocationFetched(Commons.getLocationAddress(context, location));
        Map<String, Object> locData = new HashMap<>();

        locData.put(Constants.LAT, location.getLatitude());
        locData.put(Constants.LNG, location.getLongitude());
        locData.put(Constants.LOC_ADDRESS, Commons.getLocationAddress(context, location));
        locData.put(Constants.IS_PHONE_CHARGING, batteryStatus.isCharging());
        locData.put(Constants.BATTERY_PERCENTAGE, batteryStatus.getBatteryPercentage());
        locData.put(Constants.LOC_TIMESTAMP, System.currentTimeMillis());

        // the location info in LOCATION collection
//        Toast.makeText(App.getAppContext(), "WriteLocation: " + App.writeCount++, Toast.LENGTH_SHORT).show();

        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(Objects.requireNonNull(currentUserEmail))
                .collection(Constants.LOCATION_COLLECTION)
                .document()
                .set(locData)
                .addOnSuccessListener(unused -> Log.i(TAG, "Firestore location update successful in LOCATION COLLECTION"))
                .addOnFailureListener(e -> Log.e(TAG, "Error. Firestore location update in LOCATION COLLECTION" + e.getMessage()));

        // updates the values of location in USER collection
//        Toast.makeText(App.getAppContext(), "Write: " + App.writeCount++, Toast.LENGTH_SHORT).show();

        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(currentUserEmail)
                .update(locData)
                .addOnSuccessListener(unused -> Log.i(TAG, " successful location updated in USER collection"))
                .addOnFailureListener(e -> Log.e(TAG, "error. updating location data in USER collection: " + e.getMessage()));

        //notification
//        Commons.showNotification(requireContext(),Commons.getLocationAddress(context, location));

    }

    private void loadAnimations() {

        //show toolbar extended view animation
        showToolbarExtAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.top_anim);

        //hide toolbar extended view animation
        hideToolbarExtAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_anim);
    }

    private void clickListeners() {

        //toolbar settings click listener
        binding.toolbar.consSettingToolbar.setOnClickListener(v -> {
            toolbarSettings();
        });

        //toolbar messages click listener
        binding.toolbar.consChatToolbar.setOnClickListener(v -> {
            toolbarChat();
        });

        //toolbar circle name click listener
        binding.toolbar.consCircle.setOnClickListener(v -> circleExtendedView());

        //check-in click listener
        binding.consCheckIn.setOnClickListener(v -> Toast.makeText(getContext(), "Check In", Toast.LENGTH_SHORT).show());

        //map type click listener
        binding.consMapType.setOnClickListener(v -> {

//            new Handler().postDelayed(this::setAd,30000);

            try {
                mInterstitialAd.show(requireActivity());
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();

                        setAd();
                        bottomSheetMapType();

                    }
                });
            } catch (Exception e) {
                bottomSheetMapType();
            }


        });

        //navigate to live location
        binding.consLiveLoc.setOnClickListener(v -> {

            //moves to current location
            checkLocationPermission();

            //after clicking and moving to current location, hides the live location view
            binding.consLiveLoc.setVisibility(View.GONE);
        });

        //click listener of the extended toolbar view (circle selection view)
        extendedToolbarViewClickListeners();

        //bottom sheet containing info about the current circle members
        bottomSheetMembers();
    }

    private void toolbarSettings() {

        try {

            mInterstitialAd.show(requireActivity());
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    setAd();
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    //slide up animation of Settings activity
                    Objects.requireNonNull(requireActivity()).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);

                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    //slide up animation of Settings activity
                    Objects.requireNonNull(requireActivity()).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

                }
            });
        } catch (Exception e) {
            Log.i("toolbarSettings: ", e.getMessage());

            setAd();
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            //slide up animation of Settings activity
            Objects.requireNonNull(requireActivity()).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
        }
    }

    private void toolbarChat() {

        // Make the ad request again here

        try {
            mInterstitialAd.show(requireActivity());
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    setAd();
                    startActivity(new Intent(getActivity(), ChatDashboardActivity.class));
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);

                    setAd();
                    startActivity(new Intent(getActivity(), ChatDashboardActivity.class));
                }
            });
        } catch (Exception e) {
            Log.i("toolbarChatAdError: ", e.getMessage());
            startActivity(new Intent(getActivity(), ChatDashboardActivity.class));
        }

    }

    private void extendedToolbarViewClickListeners() {

        //add member click listener
        binding.toolbarExtendedView.imgViewAddCircleMembers.setOnClickListener(v -> addCircleMember(true));

        //circle name click listener
        binding.toolbarExtendedView.consCircleName.setOnClickListener(v -> circleShrunkView());

        //button create circle click listener
        binding.toolbarExtendedView.btnCreateCircleToolbar.setOnClickListener(v -> createNewCircle());

        //button join circle click listener
        binding.toolbarExtendedView.btnJoinCircleToolbar.setOnClickListener(v -> joinCircle());

        // if this view is clicked, the extended view visibility will be set to gone
        binding.toolbarExtendedView.backgroundOpaqueView.setOnClickListener(v -> circleShrunkView());
    }

    //we have add member button in extended toolbar view and bottom navigation as well
    private void addCircleMember(boolean isToolbarAddMemberBtnClicked) {

        Intent intent = new Intent(getActivity(), AddMemberActivity.class);
        intent.putExtra(Constants.ADD_MEMBER_BUTTON_CLICKED, isToolbarAddMemberBtnClicked);
        addMemberResultLauncher.launch(intent);
    }

    private void createNewCircle() {
        createNewCircleResultLauncher.launch(new Intent(requireActivity(), CreateCircleMainActivity.class));
    }

    private void joinCircle() {
        joinCircleResultLauncher.launch(new Intent(requireActivity(), JoinCircleMainActivity.class));
    }

    //delays the extended toolbar view to give it an animation like flow when Activity Result Launchers are called
    private void delayCircleShrunkView() {

        new Handler().postDelayed(this::circleShrunkView, 300);
    }

    private void circleShrunkView() {

        //loading the toolbar extended view animations
        loadAnimations();

        binding.toolbarExtendedView.consCircleSelection.setAnimation(hideToolbarExtAnim);

        //the background of extended view visibility is set to gone to set the hide extended toolbar animation
        binding.toolbarExtendedView.backgroundOpaqueView.setVisibility(View.GONE);

        //setting visibility to gone after 300 millisecond
        new Handler().postDelayed(() -> binding.toolbarExtendedView.consCircleSelection.setVisibility(View.GONE), 300);

    }

    public void circleExtendedView() {

        //loading the toolbar extended view animations
        loadAnimations();

        //setting the visibility of extended view to visible
        binding.toolbarExtendedView.backgroundOpaqueView.setVisibility(View.VISIBLE);

        //setting the background of extended view visibility to visible
        binding.toolbarExtendedView.consCircleSelection.setVisibility(View.VISIBLE);

        //setting the animation
        binding.toolbarExtendedView.consCircleSelection.setAnimation(showToolbarExtAnim);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void selectCircleRecyclerview(List<CircleModel> circleList) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        circleSelectionRecyclerView.setLayoutManager(layoutManager);
        circleSelectionRecyclerView.setHasFixedSize(true);

        CircleToolbarAdapter adapterToolbar = new CircleToolbarAdapter(requireContext(), circleList, this);
        circleSelectionRecyclerView.setAdapter(adapterToolbar);
        adapterToolbar.notifyDataSetChanged();

    }

    //toolbar circle name recyclerview click listener
    @Override
    public void onCircleSelected(CircleModel clickedCircle) {

        // setting the values to shared preference
        SharedPreference.setCircleId(clickedCircle.getCircleId());
        SharedPreference.setCircleName(clickedCircle.getCircleName());
        SharedPreference.setCircleInviteCode(clickedCircle.getCircleJoinCode());
        SharedPreference.setCircleMembersList(clickedCircle.getCircleMembersList());

        // setting the updated circle name to toolbar & extended view
        binding.toolbar.textViewCircleName.setText(clickedCircle.getCircleName());
        binding.toolbarExtendedView.txtCircleName.setText(clickedCircle.getCircleName());

        Log.i("onCircleSelectedMembersList: ", clickedCircle.getCircleMembersList().toString());

        sharedViewModel.setCircleMembers(clickedCircle.getCircleMembersList());
        setBottomSheetMembersRecyclerView();
    }

    private void bottomSheetMembers() {

        binding.bottomSheetMembers.consPlaces.setOnClickListener(v -> {

//            mInterstitialAd.show(requireActivity());
//            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdDismissedFullScreenContent() {
//                    super.onAdDismissedFullScreenContent();
//
//                    setAd();
//                    startActivity(new Intent(requireActivity(), Subs.class));
//                }
//            });
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setBottomSheetMembersRecyclerView() {


        //purpose of this condition is to remove null pointer exception. (If you're in Safety Fragment & location changes)
        requireActivity().runOnUiThread(() -> {

            //stopping and hiding shimmer
            binding.bottomSheetMembers.shimmerLayout.startShimmer();
            binding.bottomSheetMembers.shimmerLayout.setVisibility(View.VISIBLE);

            sharedViewModel.getCircleMembersMutableLiveData().observe(getViewLifecycleOwner(), memberDetails -> {

                if (!memberDetails.isEmpty()) {

                    List<MemberDetail> members = new ArrayList<>(memberDetails);
                    Log.i("setBottomSheetMembersRecyclerViewSize: ", String.valueOf(members.size()));

                    //saving user data to shared preferences
                    for (MemberDetail member:memberDetails){
                        if(member.getMemberEmail().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())){
                            SharedPreference.setFirstNamePref(member.getMemberFirstName());
                            SharedPreference.setLastNamePref(member.getMemberLastName());
                        }
                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.bottomSheetMembers.recyclerBottomSheetMember.setLayoutManager(layoutManager);
                    bottomSheetMemberAdapter.setMemberDetailList(members);
                    binding.bottomSheetMembers.recyclerBottomSheetMember.setAdapter(bottomSheetMemberAdapter);
                    //stopping and hiding shimmer
                    binding.bottomSheetMembers.shimmerLayout.stopShimmer();
                    binding.bottomSheetMembers.shimmerLayout.setVisibility(View.GONE);
                    bottomSheetMemberAdapter.notifyDataSetChanged();

                    // set the markers on map
                    setMarkers(members);

                    binding.bottomSheetMembers.noResultsL.setVisibility(View.GONE);
                    binding.bottomSheetMembers.noListViewL.setVisibility(View.GONE);
                    binding.bottomSheetMembers.recyclerBottomSheetMember.setVisibility(View.VISIBLE);
                } else {
                    binding.bottomSheetMembers.noResultsL.setVisibility(View.VISIBLE);
                    binding.bottomSheetMembers.noListViewL.setVisibility(View.VISIBLE);
                    binding.bottomSheetMembers.noResultsL.playAnimation();
                    binding.bottomSheetMembers.recyclerBottomSheetMember.setVisibility(View.GONE);
                }
            });
        });
    }

    private void setMarkers(List<MemberDetail> memberDetailsList) {

        if (mGoogleMap != null) {

            //setting the map type from preference
            int mapTypePreference = SharedPreference.getMapType();

            switch (mapTypePreference) {

                case 0:
                    //map type is default/normal
                    if (mGoogleMap != null) mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;

                case 1:
                    //map type is satellite
                    if (mGoogleMap != null) mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;

                case 2:
                    //map type is hybrid
                    if (mGoogleMap != null) mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;

                case 3:
                    //map type is terrain
                    if (mGoogleMap != null) mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
            }

            // removes marker if (any) on map
            assert mGoogleMap != null;
            mGoogleMap.clear();

            if (memberDetailsList != null) {
                for (MemberDetail item : memberDetailsList) {
                    if (item.getLocationLat() != null && item.getLocationLng() != null) {

                        if (item.getMemberEmail().equals(SharedPreference.getEmailPref())) {

                            LatLng latLng = new LatLng(Double.parseDouble(item.getLocationLat()), Double.parseDouble(item.getLocationLng()));

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            mGoogleMap.moveCamera(cameraUpdate);
                            mGoogleMap.animateCamera(cameraUpdate);

                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(String.valueOf(item.getMemberFirstName().charAt(0)))))
                                    .title(item.getMemberFirstName().concat(" ").concat(item.getMemberLastName()))
                                    .anchor((float) 0.5, (float) 0.5));
                        }
                    }
                }

            } else {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mGoogleMap.moveCamera(cameraUpdate);
                mGoogleMap.animateCamera(cameraUpdate);

                mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView("You")))
                        .anchor((float) 0.5, (float) 0.5));
            }

        }

    }

    private Bitmap getMarkerBitmapFromView(String userNameFirstLetter) {

        CustomMarkerBinding markerBinding = CustomMarkerBinding.inflate(getLayoutInflater());
        markerBinding.getRoot().measure(0, 0);
        markerBinding.getRoot().layout(0, 0, markerBinding.getRoot().getMeasuredWidth(), markerBinding.getRoot().getMeasuredHeight());

        // setting the user first name char
        markerBinding.textUserNameLetter.setText(userNameFirstLetter);

        Bitmap bitmap = Bitmap.createBitmap(markerBinding.getRoot().getMeasuredWidth(), markerBinding.getRoot().getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        markerBinding.getRoot().draw(new Canvas(bitmap));
        return bitmap;
    }

    // recyclerview bottom sheet member 'Add new member' click listener
    @Override
    public void onAddNewMemberClicked() {
        mInterstitialAd.show(requireActivity());
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();

                setAd();
                if (!SharedPreference.getCircleId().equals(Constants.NULL)) {
                    addCircleMember(false);
                } else {
                    Snackbar snackbar = Snackbar.make(requireView(), "Please select the circle first!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", null);
                    snackbar.setActionTextColor(Color.WHITE);

                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(requireActivity().getColor(R.color.orange));

                    TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(18f);
                    snackbar.show();
                }

            }
        });
    }

    // recyclerview bottom sheet member click listener
    @Override
    public void onAddedMemberClicked(int position, MemberDetail memberItem) {

        Intent intent = new Intent(getActivity(), MemberLocationActivity.class);
        intent.putExtra(Constants.EMAIL, memberItem.getMemberEmail());
        intent.putExtra(Constants.FIRST_NAME, memberItem.getMemberFirstName());
        startActivity(intent);
    }

    private void bottomSheetMapType() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), com.google.android.material.R.style.Theme_Design_BottomSheetDialog);

        LayoutBottomSheetMapTypeBinding mapTypeBinding = LayoutBottomSheetMapTypeBinding.inflate(LayoutInflater.from(getContext()));

        bottomSheetDialog.setContentView(mapTypeBinding.getRoot());

        bottomSheetDialog.show();

        mapTypeBinding.consMapDefault.setOnClickListener(v -> setDefaultMapType(mapTypeBinding));

        mapTypeBinding.consMapSatellite.setOnClickListener(v -> setSatelliteMapType(mapTypeBinding));

        mapTypeBinding.consMapHybrid.setOnClickListener(v -> setHybridMapType(mapTypeBinding));

        mapTypeBinding.consMapTerrain.setOnClickListener(v -> setTerrainMapType(mapTypeBinding));

        // cancel click listener
        mapTypeBinding.imgViewCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // getting the preference value and setting value to map type
        int mapTypePref = SharedPreference.getMapType();

        switch (mapTypePref) {

            case 0:
                //map type is default/normal
                setDefaultMapType(mapTypeBinding);
                break;

            case 1:
                //map type is satellite
                setSatelliteMapType(mapTypeBinding);
                break;

            case 2:
                //map type is hybrid
                setHybridMapType(mapTypeBinding);
                break;

            case 3:
                //map type is terrain
                setTerrainMapType(mapTypeBinding);
                break;
        }

    }

    private void setDefaultMapType(LayoutBottomSheetMapTypeBinding mapTypeBinding) {

        //highlight the selected view
        mapTypeBinding.cardDefault.setCardBackgroundColor(requireContext().getColor(R.color.orange));
        mapTypeBinding.txtDefault.setTextColor(requireContext().getColor(R.color.orange));

        //setting the value in preference
        SharedPreference.setMapType(0);

        //change the map type
        if (mGoogleMap != null) mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //change the rest of views
        mapTypeBinding.cardSatellite.setCardBackgroundColor(requireContext().getColor(R.color.white));
        mapTypeBinding.cardTerrain.setCardBackgroundColor(requireContext().getColor(R.color.white));
        mapTypeBinding.cardHybrid.setCardBackgroundColor(requireContext().getColor(R.color.white));

        //changing text color of rest of text views
        mapTypeBinding.txtSatellite.setTextColor(requireContext().getColor(R.color.black));
        mapTypeBinding.txtTerrain.setTextColor(requireContext().getColor(R.color.black));
        mapTypeBinding.txtHybrid.setTextColor(requireContext().getColor(R.color.black));

    }

    private void setSatelliteMapType(LayoutBottomSheetMapTypeBinding mapTypeBinding) {

        //highlight the selected view
        mapTypeBinding.cardSatellite.setCardBackgroundColor(requireContext().getColor(R.color.orange));
        mapTypeBinding.txtSatellite.setTextColor(requireContext().getColor(R.color.orange));

        //setting the value in preference
        SharedPreference.setMapType(1);

        //change the map type
        if (mGoogleMap != null) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        //change the rest of views
        mapTypeBinding.cardDefault.setCardBackgroundColor(requireContext().getColor(R.color.white));
        mapTypeBinding.cardTerrain.setCardBackgroundColor(requireContext().getColor(R.color.white));
        mapTypeBinding.cardHybrid.setCardBackgroundColor(requireContext().getColor(R.color.white));

        //changing text color of rest of text views
        mapTypeBinding.txtDefault.setTextColor(requireContext().getColor(R.color.black));
        mapTypeBinding.txtTerrain.setTextColor(requireContext().getColor(R.color.black));
        mapTypeBinding.txtHybrid.setTextColor(requireContext().getColor(R.color.black));

    }

    private void setHybridMapType(LayoutBottomSheetMapTypeBinding mapTypeBinding) {

        //highlight the selected view
        mapTypeBinding.cardHybrid.setCardBackgroundColor(requireContext().getColor(R.color.orange));
        mapTypeBinding.txtHybrid.setTextColor(requireContext().getColor(R.color.orange));

        //setting the value in preference
        SharedPreference.setMapType(2);

        //change the map type
        if (mGoogleMap != null) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

        //change the rest of views
        mapTypeBinding.cardSatellite.setCardBackgroundColor(requireContext().getColor(R.color.white));
        mapTypeBinding.cardTerrain.setCardBackgroundColor(requireContext().getColor(R.color.white));
        mapTypeBinding.cardDefault.setCardBackgroundColor(requireContext().getColor(R.color.white));

        //changing text color of rest of text views
        mapTypeBinding.txtSatellite.setTextColor(requireContext().getColor(R.color.black));
        mapTypeBinding.txtTerrain.setTextColor(requireContext().getColor(R.color.black));
        mapTypeBinding.txtDefault.setTextColor(requireContext().getColor(R.color.black));

    }

    private void setTerrainMapType(LayoutBottomSheetMapTypeBinding mapTypeBinding) {

        //highlight the selected view
        mapTypeBinding.cardTerrain.setCardBackgroundColor(requireContext().getColor(R.color.orange));
        mapTypeBinding.txtTerrain.setTextColor(requireContext().getColor(R.color.orange));

        //setting the value in preference
        SharedPreference.setMapType(3);

        //change the map type
        if (mGoogleMap != null) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        //change the rest of views
        mapTypeBinding.cardSatellite.setCardBackgroundColor(requireContext().getColor(R.color.white));
        mapTypeBinding.cardDefault.setCardBackgroundColor(requireContext().getColor(R.color.white));
        mapTypeBinding.cardHybrid.setCardBackgroundColor(requireContext().getColor(R.color.white));

        //changing text color of rest of text views
        mapTypeBinding.txtSatellite.setTextColor(requireContext().getColor(R.color.black));
        mapTypeBinding.txtDefault.setTextColor(requireContext().getColor(R.color.black));
        mapTypeBinding.txtHybrid.setTextColor(requireContext().getColor(R.color.black));

    }

    //nullifying binding object
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setAd() {

        InterstitialAd.load(
                requireActivity().getApplicationContext(),
                BuildConfig.INTERSTITIAL_AD_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {

                        Log.i("AdError", adError.toString());
                        mInterstitialAd = null;

                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Log.i("AdError", "Ad was loaded.");
                        mInterstitialAd = interstitialAd;
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onResume() {
        super.onResume();

        setAd();
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                && !Permissions.hasActivityPermission(requireContext())) {

            dialog.dismiss();

            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.layout_permission_dialog2);

            //setting the transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //this sets the width of dialog to 90%
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = (int) (displayMetrics.widthPixels * 0.9);

            //setting the width and height of alert dialog
            dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);


            AppCompatButton buttonSettings = dialog.findViewById(R.id.btn_settings_perm_dialog);

            buttonSettings.setOnClickListener(v -> {

                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, Constants.REQUEST_CODE_ACTIVITY);

                dialog.dismiss();
            });

            dialog.setCancelable(true);
            dialog.show();

        } else {
            checkLocationPermission();
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private PendingIntent getPendingIntent() {

        Intent intent = new Intent(requireActivity(), ActivityTransitionReceiver.class);

        return PendingIntent.getBroadcast(requireActivity(),
                Constants.REQUEST_CODE_INTENT_ACTIVITY_TRANSITION,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void requestForUpdates() {
        if (Permissions.hasActivityPermission(requireContext())) {

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                recognitionClient.requestActivityUpdates(
                        ActivityTransitionUtils.getActivityTransitionRequest().describeContents(),
                        getPendingIntent()

                ).addOnSuccessListener(unused -> {
                    Log.i("requestForUpdates: ", "successful Registration");

                }).addOnFailureListener(e -> {
                    Log.i("requestForUpdates: ", "Unsuccessful Registration");

                });

                Log.i("TAG", "checkLocationPermission() : app has location permission");
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, Constants.REQUEST_CODE_ACTIVITY);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(transitionReceiver);
//        sharedViewModel.getCircleMembersMutableLiveData().removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onPause() {
        super.onPause();
//        sharedViewModel.getCircleMembersMutableLiveData().removeObservers(getViewLifecycleOwner());
//        setBottomSheetMembersRecyclerView();
    }


}