package com.care360.findmyfamilyandfriends.auth.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.care360.findmyfamilyandfriends.HomeScreen.HomeActivity;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.ResetPassword.ByEmail.ResetPasswordEmailActivity;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.databinding.ActivityEnterPasswordSignInBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class EnterPasswordSignInActivity extends AppCompatActivity {

    private ActivityEnterPasswordSignInBinding binding;

    private String password;
    private final TextWatcher passwordTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            password = charSequence.toString().trim();

            if (password.length() == 0) {

                binding.btnContPhoneSignIn.setEnabled(false);
                binding.btnContPhoneSignIn.setBackgroundResource(R.drawable.disabled_round_button);

            } else if (password.length() > 0) {

                binding.btnContPhoneSignIn.setEnabled(true);
                binding.btnContPhoneSignIn.setBackgroundResource(R.drawable.white_rounded_button);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize binding
        binding = ActivityEnterPasswordSignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MobileAds.initialize(this);

        //TextWatcher
        binding.edtPasswordSignIn.addTextChangedListener(passwordTextWatcher);

        //button sign in click listener
        binding.btnContPhoneSignIn.setOnClickListener(v -> {

            buttonClickListener();

        });

        //forget password
        binding.txtForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(EnterPasswordSignInActivity.this, ResetPasswordEmailActivity.class));
        });

    }

    private void buttonClickListener() {

        Commons.signIn(EnterPasswordSignInActivity.this, password, isSuccessful -> {

            if (isSuccessful) {

                //add FCM token
                Commons.addFCMToken();

                //navigate to next activity
                startActivity(new Intent(EnterPasswordSignInActivity.this, HomeActivity.class));
                finish();
            }
        });

    }

//    private void setAd() {
//
//        InterstitialAd.load(
//                this,
//                BuildConfig.INTERSTITIAL_AD_ID,
//                adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
//
//                        Log.d("AdError", adError.toString());
//                        mInterstitialAd = null;
//                    }
//
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        Log.d("AdError", "Ad was loaded.");
//                        mInterstitialAd = interstitialAd;
//                    }
//                });
//    }

}