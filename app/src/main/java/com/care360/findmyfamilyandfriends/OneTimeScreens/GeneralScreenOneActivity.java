package com.care360.findmyfamilyandfriends.OneTimeScreens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.databinding.ActivityGeneralScreenOneBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class GeneralScreenOneActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private ActivityGeneralScreenOneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing view binding
        binding = ActivityGeneralScreenOneBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        MobileAds.initialize(this);
        //banner
        adRequest = new AdRequest.Builder().build();
        setAd();

        setContentView(view);

        BannerAds();

        //button continue click listener
        binding.btnContScreenOne.setOnClickListener(v -> {

            if (mInterstitialAd == null)
                setAd();
            else
                mInterstitialAd.show(this);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    setAd();
                    startActivity(new Intent(GeneralScreenOneActivity.this,GeneralScreenTwoActivity.class));

                }
            });
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

    private void setAd() {

        InterstitialAd.load(
                this,
                BuildConfig.INTERSTITIAL_AD_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {

                        Log.d("loaded", adError.toString());
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