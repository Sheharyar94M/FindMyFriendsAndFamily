package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Settings.CircleManagement.RemoveMember;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.care360.findmyfamilyandfriends.Application.App;
import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.databinding.ActivityDeleteCircleMemberBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RemoveCircleMemberActivity extends AppCompatActivity {

    ActivityDeleteCircleMemberBinding binding;

    List<MemberModel> memberList = new ArrayList<>();
    List<String> emailList = new ArrayList<>();

    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initializing binding
        binding = ActivityDeleteCircleMemberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this);

        adRequest = new AdRequest.Builder().build();

        setAd();

        BannerAds();

        // toolbar
        binding.toolbarDeleteCircleMembers.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbarDeleteCircleMembers.setTitle(SharedPreference.getCircleName());

        // get the members data
        getMembersData();
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

    @SuppressWarnings("unchecked")
    private void getMembersData() {

        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(SharedPreference.getCircleAdminId())
                .collection(Constants.CIRCLE_COLLECTION)
                .document(SharedPreference.getCircleId())
                .get()
                .addOnSuccessListener(doc -> {
//                    Toast.makeText(App.getAppContext(), "ReadsMembers: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                    ArrayList<String> list = (ArrayList<String>) doc.get(Constants.CIRCLE_MEMBERS);

                    emailList.addAll(list);

                    for(int i = 0; i < emailList.size(); i++)
                    {
                        int loopIndex = i;

                        // getting the full name of user
                        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                                .document(emailList.get(i))
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
//                                    Toast.makeText(App.getAppContext(), "Reads: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                                    String name= documentSnapshot.getString(Constants.FIRST_NAME).concat(" ").concat(documentSnapshot.getString(Constants.LAST_NAME));

                                    memberList.add(new MemberModel(name, emailList.get(loopIndex)));

                                    // on last iteration, sets data to recyclerview
                                    if(loopIndex == emailList.size() - 1) {

                                        // setting the recyclerview
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(RemoveCircleMemberActivity.this,LinearLayoutManager.VERTICAL,false);
                                        binding.recyclerDeleteMembers.setLayoutManager(layoutManager);

                                        RemoveCircleMemberAdapter adapter = new RemoveCircleMemberAdapter(RemoveCircleMemberActivity.this, memberList);
                                        binding.recyclerDeleteMembers.setAdapter(adapter);
                                    }
                                });
                    }
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