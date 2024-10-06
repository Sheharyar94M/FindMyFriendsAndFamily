package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers.MemberDetail;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Chat.Model.UserInfo;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.JoinCircle.CircleModel;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.MainSharedViewModel;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.care360.findmyfamilyandfriends.databinding.ActivityChatDashboardBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatDashboardActivity extends AppCompatActivity implements ChatDashboardAdapter.OnChatMemberListener {

    private static final String TAG = "CHAT_DASHBOARD";

    ActivityChatDashboardBinding binding;

    //members info list
    List<UserInfo> membersInfoList = new ArrayList<>();

    //members list
    List<String> circleMembersList = new ArrayList<>();

    //adapter
    ChatDashboardAdapter adapter;
    private final TextWatcher searchMemberTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Filter membersFilter = new Filter() {
                @Override
                public FilterResults performFiltering(CharSequence charSequence) {

                    List<UserInfo> filteredMemberList = new ArrayList<>();

                    if (charSequence == null || charSequence.length() == 0) {
                        filteredMemberList.addAll(adapter.membersFilteringList);
                    } else {
                        String filterPattern = charSequence.toString().trim().toLowerCase();

                        for (UserInfo member : adapter.membersFilteringList) {
                            if (member.getUserFullName().toLowerCase().contains(filterPattern)) {
                                filteredMemberList.add(member);
                            }
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredMemberList;
                    return filterResults;
                }

                @SuppressLint("NotifyDataSetChanged")
                @SuppressWarnings("unchecked")
                @Override
                public void publishResults(CharSequence charSequence, FilterResults filterResults) {

                    adapter.membersList.clear();
                    adapter.membersList.addAll((List<UserInfo>) filterResults.values);
                    adapter.notifyDataSetChanged();

                }
            };

            membersFilter.filter(charSequence);

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private InterstitialAd mInterstitialAd = null;
    private AdRequest adRequest;

    private MainSharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing binding
        binding = ActivityChatDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedViewModel = new ViewModelProvider(this).get(MainSharedViewModel.class);

        MobileAds.initialize(this);

        adRequest = new AdRequest.Builder().build();
        setAd();

        BannerAds();

        adapter = new ChatDashboardAdapter(this, membersInfoList, this);

        //get the circle members list
        getCirclesMemberList();


        // edit text search
        binding.editTextSearch.addTextChangedListener(searchMemberTextWatcher);

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
    private void getCirclesMemberList() {

        // current user email
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            try {
                String currentUserEmail;

                if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() != null)
                    currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                else
                    currentUserEmail = SharedPreference.getEmailPref();

                FirebaseFirestore.getInstance().collectionGroup(Constants.CIRCLE_COLLECTION)
                        .whereArrayContains(Constants.CIRCLE_MEMBERS, currentUserEmail)
                        .get()
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {

                                for (DocumentSnapshot doc : task.getResult()) {

                                    ArrayList<String> members = (ArrayList<String>) doc.get(Constants.CIRCLE_MEMBERS);

                                    circleMembersList.addAll(members);
                                }

                                // removes all the occurrences of current user email (if any) from list
                                circleMembersList.removeAll(Collections.singleton(currentUserEmail));

                                if (circleMembersList.size() > 0) {

                                    // retrieve the details of all members
                                    for(int i=0; i < circleMembersList.size(); i++) {

                                        //fetch the user info
                                        int loopIndex = i;

                                        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                                                .document(circleMembersList.get(i))
                                                .get()
                                                .addOnSuccessListener(doc -> {
//                                            Toast.makeText(App.getAppContext(), "ReadsChat: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                                                    String fullName = doc.getString(Constants.FIRST_NAME).concat(" ").concat(doc.getString(Constants.LAST_NAME));

                                                    membersInfoList.add(new UserInfo(circleMembersList.get(loopIndex), doc.getString(Constants.FCM_TOKEN),fullName, doc.getString(Constants.IMAGE_PATH)));

                                                    //setting recyclerview
                                                    if(loopIndex == circleMembersList.size() -1 ) {

                                                        //recyclerview
                                                        setRecyclerView(membersInfoList);

                                                    }
                                                })
                                                .addOnFailureListener(e -> Log.e(TAG, "error getting member details: " + e.getMessage()));
                                    }

                                } else if (circleMembersList.size() == 0) {

                                    //show the no members view
                                    binding.consNoMembers.setVisibility(View.VISIBLE);
                                    binding.recyclerView.setVisibility(View.GONE);

                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "error getting members list:" + e.getMessage());
                        });

                executor.shutdown();
            } catch (Exception e) {
                Log.i("getCirclesMemberList: ", e.getMessage());
            }
        });

    }

    private void setRecyclerView(List<UserInfo> membersInfoList) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatDashboardAdapter(this, membersInfoList, this);
        binding.recyclerView.setAdapter(adapter);

        //if the activity is called from notification in case a new message is received
        Intent intent = getIntent();
        if (intent.getStringExtra(Constants.SENDER_ID) != null) {

            for (int i = 0; i < membersInfoList.size(); i++) {
                if (intent.getStringExtra(Constants.SENDER_ID).equals(membersInfoList.get(i).getUserId())) {

                    Intent chatIntent = new Intent(this, ChatDetailActivity.class);
                    chatIntent.putExtra(Constants.KEY_USER_INFO, membersInfoList.get(i));
                    chatIntent.putExtra(Constants.IS_APP_IN_FOREGROUND, intent.getBooleanExtra(Constants.IS_APP_IN_FOREGROUND, true));
                    chatIntent.putExtra(Constants.RANDOM_COLOR, Commons.randomColor());
                    startActivity(chatIntent);
                }
            }
        }
    }

    //member click listener
    @Override
    public void onChatMemberClick(int position, int randomColor) {

        Intent intent = new Intent(this, ChatDetailActivity.class);
        intent.putExtra(Constants.KEY_USER_INFO, membersInfoList.get(position));
        intent.putExtra(Constants.IS_APP_IN_FOREGROUND, true);
        intent.putExtra(Constants.RANDOM_COLOR, randomColor);
        startActivity(intent);
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