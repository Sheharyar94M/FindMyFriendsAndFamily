package com.care360.findmyfamilyandfriends.SharedPreference.ResetPassword.ByEmail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.databinding.ActivityResetPasswordEmailBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordEmailActivity extends AppCompatActivity {

    private static final String TAG = "RESET_PASS_EMAIL";

    private ActivityResetPasswordEmailBinding binding;

    private String enteredEmail;
    private final TextWatcher emailTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            enteredEmail = charSequence.toString();

            //setting the helper text value to empty
            binding.layoutEmail.setHelperText(" ");

            //validating the email
            if (Commons.validateEmailAddress(enteredEmail)) {

                //enabling the button
                binding.btnSendInstruction.setEnabled(true);
                binding.btnSendInstruction.setBackgroundResource(R.drawable.orange_rounded_button);
                binding.btnSendInstruction.setTextColor(getColor(R.color.white));
            } else {

                //disable the button
                binding.btnSendInstruction.setEnabled(false);
                binding.btnSendInstruction.setBackgroundResource(R.drawable.disabled_round_button);
                binding.btnSendInstruction.setTextColor(getColor(R.color.orange));
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

        //initializing binding
        binding = ActivityResetPasswordEmailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MobileAds.initialize(this);

        //banner
        adRequest = new AdRequest.Builder().build();
        setAd();

        BannerAds();

        //text watcher
        binding.edtEmail.addTextChangedListener(emailTextWatcher);

        //button click listener
        binding.btnSendInstruction.setOnClickListener(v -> {
            buttonClickListener();
        });

    }

    private AdSize getAdaptiveAdSize() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        float density = getResources().getDisplayMetrics().density;
        int widthDp = (int) (widthPixels / density);
        int adWidth = Math.min(widthDp, 640);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    public void BannerAds() {
        AdView adView;
        AdSize adaptiveAdSize = getAdaptiveAdSize();
        adView = new AdView(this);
        adView.setAdUnitId(BuildConfig.BANNER_AD_ID);
        adView.setAdSize(adaptiveAdSize);
        binding.adaptiveBanner.addView(adView);
        adView.loadAd(adRequest);
    }

    private void buttonClickListener() {

        //setting the helper text to empty
        binding.layoutEmail.setHelperText(" ");

        //sending the recovery email
        FirebaseAuth.getInstance().
                sendPasswordResetEmail(enteredEmail)
                .addOnSuccessListener(unused -> {

                    Log.i(TAG, "password reset email send successfully");

                    //navigating to next activity
                    startActivity(new Intent(this, CheckEmailActivity.class));
                })
                .addOnFailureListener(e -> {
                    //email does not exists
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Email does not exists! Try with another email!!", Snackbar.LENGTH_INDEFINITE);

                    snackbar.setAction("Ok", v -> {
                        snackbar.dismiss();
                        finish();
                    });

                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                });


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

}