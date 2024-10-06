package com.care360.findmyfamilyandfriends.HomeScreen.ui.subscription;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.HomeScreen.HomeActivity;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.subscription.adapters.FeaturesListAdapter;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.subscription.adapters.ProductDetailsAdapter;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.subscription.model.Features;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.subscription.model.ProductDetailsModel;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.databinding.FragmentSubscriptionBinding;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionFragment extends Fragment implements ProductDetailsAdapter.ProductSelected, FeaturesListAdapter.itemSelected {

    private SubscriptionViewModel viewModel;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private FragmentSubscriptionBinding binding;

    private BillingClient billingClient;
    private List<ProductDetails> productDetailsList;
    private Handler handler;
    private ProductDetailsAdapter adapter;

    private MutableLiveData<Integer> clickedItemPosition;
    private FeaturesListAdapter featuresAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);

        binding = FragmentSubscriptionBinding.inflate(inflater, container, false);

        clickedItemPosition = new MutableLiveData<>();

        MobileAds.initialize(requireContext());

        //banner
        adRequest = new AdRequest.Builder().build();

        //initializing shimmer
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#DACCCF"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#F3F3F3"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();

        //initializing shimmer drawable
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);


        setAd();

        productDetailsList = new ArrayList<>();

        adapter = new ProductDetailsAdapter(requireContext(),this);
        featuresAdapter = new FeaturesListAdapter(requireContext(),this);

        handler = new Handler();

        billingClient = BillingClient.newBuilder(requireContext())
                .enablePendingPurchases()
                .setListener(
                        (billingResult, list) -> {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                for (Purchase purchase : list) {
                                    verifySubPurchase(purchase);
                                }
                            }
                        }
                ).build();

        //start the connection after initializing the billing client
        establishConnection();

        //features
        viewModel.getDataFirestoreDatabase();

        requireActivity().runOnUiThread(() -> {

            //starting and showing shimmer
            binding.shimmerLayoutFeatures.startShimmer();
            binding.shimmerLayoutFeatures.setVisibility(View.VISIBLE);

            viewModel.getFeaturesMutableLiveData().observe(getViewLifecycleOwner(), features -> {

                try {
                    if (!features.isEmpty()){
                        Log.i( "onCreateViewFeatures: ",features.toString());

                        binding.features.setVisibility(View.VISIBLE);
                        binding.nothingFoundLayoutFeatures.setVisibility(View.GONE);

                        featuresAdapter.setFeaturesArrayList(features);
                        binding.features.setAdapter(featuresAdapter);

                        //stopping and hiding shimmer
                        binding.shimmerLayoutFeatures.stopShimmer();
                        binding.shimmerLayoutFeatures.setVisibility(View.GONE);

                        featuresAdapter.notifyDataSetChanged();
                    }else{

                        //UI change
                        binding.features.setVisibility(View.INVISIBLE);
                        binding.nothingFoundLayoutFeatures.setVisibility(View.VISIBLE);
                        binding.noResultsFeatures.playAnimation();

                        //stopping and hiding shimmer
                        binding.shimmerLayoutFeatures.stopShimmer();
                        binding.shimmerLayoutFeatures.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    Log.i( "onCreateViewSubscription: ",e.getMessage());
                }

            });
        });

        //product list
        binding.productList.setHasFixedSize(true);
        binding.productList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

        //features
        binding.features.setHasFixedSize(true);
        binding.features.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

        binding.startTrialButton.setOnClickListener(v -> {

            clickedItemPosition.observe(getViewLifecycleOwner(), integer -> {

                try {
                    if(!integer.toString().isEmpty()){
                        Log.i( "onCreateViewSubscription: ",integer.toString());

                        launchPurchaseFlow(productDetailsList.get(integer));
                    }
                    else {
                        Toast.makeText(requireActivity(), "no subscriptions found!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.i( "SubscriptionListException: ",e.getMessage());
                }
            });

        });

        return binding.getRoot();
    }

    void establishConnection() {

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    showProducts();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                establishConnection();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void showProducts() {

        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                //Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("one_week")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),

                //Product 2
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("one_month")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),

                //Product 3
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("one_year")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, prodDetailsList) -> {

                    // Process the result
                    productDetailsList.clear();

                    handler.postDelayed(() -> {

                        //starting and showing shimmer
                        binding.shimmerLayoutProducts.startShimmer();
                        binding.shimmerLayoutProducts.setVisibility(View.VISIBLE);

                        if (!prodDetailsList.isEmpty()){

                            binding.productList.setVisibility(View.VISIBLE);
                            binding.nothingFoundLayoutProducts.setVisibility(View.GONE);

                            //stopping and hiding shimmer
                            binding.shimmerLayoutProducts.stopShimmer();
                            binding.shimmerLayoutProducts.setVisibility(View.GONE);

                            productDetailsList.addAll(prodDetailsList);

                            Log.d(" number of products", "" + productDetailsList.size());

                            List<ProductDetailsModel> productDetailsModelList = new ArrayList<>();

                            for (ProductDetails details:productDetailsList){
                                productDetailsModelList.add(new ProductDetailsModel(details,false));
                            }

                            adapter.setProductDetails(productDetailsModelList);
                            binding.productList.setAdapter(adapter);
                        }else{

                            //stopping and hiding shimmer
                            binding.shimmerLayoutProducts.stopShimmer();
                            binding.shimmerLayoutProducts.setVisibility(View.GONE);

                            //make UI change
                            binding.productList.setVisibility(View.INVISIBLE);
                            binding.nothingFoundLayoutProducts.setVisibility(View.VISIBLE);
                            binding.noResultsProducts.playAnimation();

                        }

                    }, 2000);

                }
        );

    }

    void launchPurchaseFlow(ProductDetails productDetails) {
        assert productDetails.getSubscriptionOfferDetails() != null;
        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
                                .build()
                );
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

        billingClient.launchBillingFlow(requireActivity(), billingFlowParams);
    }

    void verifySubPurchase(Purchase purchases) {

        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchases.getPurchaseToken())
                .build();

        billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                //user prefs to set premium
                Toast.makeText(requireContext(), "Subscription activated, Enjoy!", Toast.LENGTH_SHORT).show();
                //Setting premium to 1
                // true - premium
                // false - no premium
                SharedPreference.setPremiumState(1);
                startActivity(new Intent(requireActivity(), HomeActivity.class));
                requireActivity().finish();
            }
        });

        Log.i("Purchase Token: ", "" + purchases.getPurchaseToken());
        Log.i("Purchase Time: ", "" + purchases.getPurchaseTime());
        Log.i("Purchase OrderID: ", "" + purchases.getOrderId());
    }

    private void setAd() {

        InterstitialAd.load(
                requireContext(),
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BannerAds();

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
    @Override
    public void clickedProduct(int position) {

        clickedItemPosition.setValue(position);
    }

    @Override
    public void itemClicked(Features feature) {

        //Full view activity

    }
}