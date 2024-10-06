package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Settings.CircleManagement.ViewMember;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.care360.findmyfamilyandfriends.Application.App;
import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Settings.CircleManagement.RemoveMember.MemberModel;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.care360.findmyfamilyandfriends.databinding.ActivityViewCircleMemberBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewCircleMemberActivity extends AppCompatActivity {

    ActivityViewCircleMemberBinding binding;

    List<MemberModel> memberList = new ArrayList<>();
    List<String> emailList = new ArrayList<>();


    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initializing binding
        binding = ActivityViewCircleMemberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this);

        adRequest = new AdRequest.Builder().build();
        setAd();

        BannerAds();

        // toolbar
        binding.toolbarViewMembers.setNavigationOnClickListener(v -> onBackPressed());

        // get members
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
//                    Toast.makeText(App.getAppContext(), "Reads: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                    ArrayList<String> list = (ArrayList<String>) doc.get(Constants.CIRCLE_MEMBERS);

                    try {
                        if (list != null) {

                            binding.noResults.setVisibility(View.GONE);
                            binding.recyclerDeleteMembers.setVisibility(View.VISIBLE);
                            binding.noListView.setVisibility(View.GONE);
                            binding.progress.setVisibility(View.VISIBLE);

                            for (int i = 0; i < list.size(); i++) {
                                int loopIndex = i;

                                // getting the full name of user
                                FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                                        .document(list.get(i))
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
//                                        Toast.makeText(App.getAppContext(), "Reads: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                                            String name = Objects.requireNonNull(documentSnapshot.getString(Constants.FIRST_NAME)).concat(" ")
                                                    .concat(Objects.requireNonNull(documentSnapshot.getString(Constants.LAST_NAME)));

                                            binding.progress.setVisibility(View.GONE);

                                            // on last iteration, sets data to recyclerview
                                                if (loopIndex == list.size() - 1) {

                                                    memberList.add(new MemberModel(name, list.get(loopIndex)));

                                                    // setting the recyclerview
                                                    LinearLayoutManager layoutManager = new LinearLayoutManager(ViewCircleMemberActivity.this, LinearLayoutManager.VERTICAL, false);
                                                    binding.recyclerDeleteMembers.setLayoutManager(layoutManager);

                                                    ViewMemberAdapter adapter = new ViewMemberAdapter(ViewCircleMemberActivity.this, memberList);
                                                    binding.recyclerDeleteMembers.setAdapter(adapter);
                                                }

                                        });
                            }
                        } else {
                            binding.noResults.setVisibility(View.VISIBLE);
                            binding.noListView.setVisibility(View.VISIBLE);
                            binding.recyclerDeleteMembers.setVisibility(View.GONE);
                            binding.noResults.playAnimation();
                            binding.progress.setVisibility(View.GONE);

                        }
                    }catch (Exception e){
                        Log.e( "getMembersData: ", e.getMessage());
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