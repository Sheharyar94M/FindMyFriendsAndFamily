package com.care360.findmyfamilyandfriends.StartScreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.care360.findmyfamilyandfriends.HomeScreen.HomeActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Chat.ChatDetailActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Chat.Model.UserInfo;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentSafety.EmergencySOS.EmergencyLocationActivity;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.care360.findmyfamilyandfriends.auth.SignIn.PhoneNoSignInActivity;
import com.care360.findmyfamilyandfriends.auth.SignUp.PhoneNoSignUpActivity;
import com.care360.findmyfamilyandfriends.databinding.ActivityStartScreenBinding;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class StartScreenActivity extends AppCompatActivity {

    ActivityStartScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing binding
        binding = ActivityStartScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(view);

        FirebaseApp.initializeApp(this);
        MobileAds.initialize(this);

        //app check for security purposes
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance(), true);

        //Verifying token
        FirebaseAppCheck
                .getInstance()
                .getAppCheckToken(true)
                .addOnSuccessListener(tokenResponse -> {
                    String appCheckToken = tokenResponse.getToken();
                    Log.i("appCheckToken", appCheckToken);
                }).addOnFailureListener(e -> Log.e("appCheckTokenE", e.getMessage()));


        // Button sign up click listener
        binding.btnSignUp.setOnClickListener(v -> startActivity(new Intent(StartScreenActivity.this, PhoneNoSignUpActivity.class)));

        // Textview sign in click listener
        binding.txtSignIn.setOnClickListener(v -> startActivity(new Intent(StartScreenActivity.this, PhoneNoSignInActivity.class)));

        binding.privacyPolicy.setOnClickListener(v -> {
            viewPrivacyPolicy();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        conditionCheck();
    }

    private void viewPrivacyPolicy() {
        Uri uri = Uri.parse(getString(R.string.policy_link));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void conditionCheck() {

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            if (intent.getExtras().getString(Constants.FCM_LAT) != null && intent.getExtras().getString(Constants.FCM_LNG) != null) {
                // setting the progress bar visibility to 'GONE'
                binding.progressBar.setVisibility(View.GONE);

                Intent sosEmergIntent = new Intent(this, EmergencyLocationActivity.class);

                sosEmergIntent.putExtra(Constants.FCM_LAT, intent.getExtras().getString(Constants.FCM_LAT));
                sosEmergIntent.putExtra(Constants.FCM_LNG, intent.getExtras().getString(Constants.FCM_LNG));
                sosEmergIntent.putExtra(Constants.IS_APP_IN_FOREGROUND, false);
                startActivity(sosEmergIntent);
                finish();

            } else if (intent.getExtras().getString(Constants.SENDER_ID) != null) {

                FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                        .document(intent.getStringExtra(Constants.SENDER_ID))
                        .get()
                        .addOnSuccessListener(doc -> {

                            String fullName = Objects.requireNonNull(doc.getString(Constants.FIRST_NAME)).concat(" ")
                                    .concat(Objects.requireNonNull(doc.getString(Constants.LAST_NAME)));

                            UserInfo userInfo = new UserInfo(doc.getId(), doc.getString(Constants.FCM_TOKEN), fullName, doc.getString(Constants.IMAGE_PATH));

                            // setting the progress bar visibility to 'GONE'
                            binding.progressBar.setVisibility(View.GONE);

                            Intent chatIntent = new Intent(this, ChatDetailActivity.class);
                            chatIntent.putExtra(Constants.KEY_USER_INFO, userInfo);
                            chatIntent.putExtra(Constants.IS_APP_IN_FOREGROUND, false);
                            chatIntent.putExtra(Constants.RANDOM_COLOR, Commons.randomColor());
                            startActivity(chatIntent);
                            finish();
                        });
            } else {
                try {
                    if (!Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).isEmpty()) {
                        checkCurrentLoginStatus();
                    } else
                        binding.progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        } else {
            try {
                if (!Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).isEmpty()) {
                    checkCurrentLoginStatus();
                } else
                    binding.progressBar.setVisibility(View.GONE);

            } catch (Exception e) {
                binding.progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void checkCurrentLoginStatus() {

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            binding.btnSignUp.setEnabled(false);
            binding.txtSignIn.setEnabled(false);

            try {

                if (!SharedPreference.getEmailPref().isEmpty() && SharedPreference.getEmailPref().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())) {
                    // setting the progress bar visibility to 'GONE'
                    binding.progressBar.setVisibility(View.GONE);

                    startActivity(new Intent(StartScreenActivity.this, HomeActivity.class));
                    finish();
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnSignUp.setEnabled(true);
                    binding.txtSignIn.setEnabled(true);
                }

            } catch (Exception e) {
                Log.i("checkCurrentLoginStatus: ", e.getMessage());
                binding.progressBar.setVisibility(View.GONE);
                binding.btnSignUp.setEnabled(true);
                binding.txtSignIn.setEnabled(true);
            }

        }, 1500);

    }

}