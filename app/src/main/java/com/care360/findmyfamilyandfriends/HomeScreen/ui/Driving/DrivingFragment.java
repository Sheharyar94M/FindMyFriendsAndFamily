package com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.adapters.MembersListAdapter;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.adapters.ViewPagerAdapter;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.models.TabsData;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers.MemberDetail;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.MainSharedViewModel;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.databinding.CustomMarkerBinding;
import com.care360.findmyfamilyandfriends.databinding.FragmentDrivingBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrivingFragment extends Fragment implements OnMapReadyCallback, MembersListAdapter.OnAddedMemberClickInterface, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ViewPagerAdapter.DayMapClicked, ViewPagerAdapter.MonthMapClicked, ViewPagerAdapter.WeekMapClicked, ViewPagerAdapter.YearMapClicked, ViewPagerAdapter.DayLocations, ViewPagerAdapter.MonthLocations, ViewPagerAdapter.WeekLocations, ViewPagerAdapter.YearLocations {

    private DrivingViewModel mViewModel;
    private FragmentDrivingBinding binding;
    private GoogleMap mGoogleMap;
    private TabLayout.Tab profileTab;
    private MembersListAdapter listAdapter;
    private BottomSheetBehavior<View> behavior;
    private LatLng latLng;

    private ProgressDialog dialog;
    private CustomMarkerBinding markerBinding;
    private Bitmap bitmap;
    private MainSharedViewModel sharedViewModel;
    private BottomSheetBehavior sheetBehavior;

    private AdRequest adRequest;

    public DrivingFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(MainSharedViewModel.class);

        MobileAds.initialize(requireContext());
        adRequest = new AdRequest.Builder().build();

        binding = FragmentDrivingBinding.inflate(inflater, container, false);

        buildGoogleApiClient();

        initializeMap();

        sheetBehavior = BottomSheetBehavior.from(binding.layoutSheetMembers.bottomSheetMove);

        BannerAds();

        dialog = new ProgressDialog(requireActivity(), R.style.AppCompatAlertDialogStyle);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        //setting up marker
        markerBinding = CustomMarkerBinding.inflate(getLayoutInflater());
        markerBinding.getRoot().measure(0, 0);
        markerBinding.getRoot().layout(0, 0, markerBinding.getRoot().getMeasuredWidth(), markerBinding.getRoot().getMeasuredHeight());

        return binding.getRoot();
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
    @SuppressLint("LogNotTimber")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new ProgressDialog(requireActivity(), R.style.AppCompatAlertDialogStyle);
        dialog.setMessage("Loading....");

        behavior = BottomSheetBehavior.from(binding.layoutSheetMembers.bottomSheetMove);

        listAdapter = new MembersListAdapter(requireActivity(), this);

        ////1st tab layout
        //setup tabs
        TabLayout.Tab allItems = binding.layoutSheetMembers.tabsProfileList.newTab().setCustomView(R.layout.tab_all_items_layout);
        profileTab = binding.layoutSheetMembers.tabsProfileList.newTab().setCustomView(R.layout.tab_item_layout);

        //default selection
        CircleImageView imageView = Objects.requireNonNull(profileTab.getCustomView()).findViewById(R.id.image_profile_);
        imageView.setBorderColor(requireActivity().getResources().getColor(R.color.orange));
        imageView.setBorderWidth(3);

        binding.layoutSheetMembers.tabsProfileList.selectTab(profileTab);
        binding.layoutSheetMembers.tabsProfileList.setSelectedTabIndicatorColor(requireActivity().getResources().getColor(R.color.orange));

        //all of the members
        setAllMembersToTabList();

        binding.layoutSheetMembers.tabsProfileList.addTab(profileTab);
        binding.layoutSheetMembers.tabsProfileList.addTab(allItems);

        //default tab
        if (profileTab.isSelected()) {
            mViewModel.getTabsListMutableLiveData().observe(getViewLifecycleOwner(), tabsData -> {

                for (TabsData tabData : tabsData) {
                    if (Objects.equals(
                            Objects.requireNonNull(
                                    FirebaseAuth.getInstance().getCurrentUser()).getEmail(), tabData.getMember().getMemberEmail())) {

                        mViewModel.setMemberClickedMutableLiveData(tabData.getMember());
                        mViewModel.setSize(tabsData.size());

                        //selecting tab and handling view
                        tabData.setSelected(!tabData.isSelected());
                    }
                }
            });
        }


        //selection of tabs
        binding.layoutSheetMembers.tabsProfileList.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getCustomView() != null) {
                    if (tab.getCustomView().findViewById(R.id.image_profile_) != null) {
                        CircleImageView imageView = tab.getCustomView().findViewById(R.id.image_profile_);
                        imageView.setBorderColor(0);
                        imageView.setBorderWidth(0);


                        binding.layoutSheetMembers.tabsProfileList.setSelected(true);
                        binding.layoutSheetMembers.tabsProfileList.setSelectedTabIndicatorColor(requireActivity().getResources().getColor(R.color.orange));

                        Log.d("onTabSelected: ", "" + tab.getId());

                        mViewModel.getTabsListMutableLiveData().observe(getViewLifecycleOwner(), tabsData -> {

                            binding.layoutSheetMembers.noMembersFound.setVisibility(View.GONE);
                            binding.layoutSheetMembers.noResultsL.setVisibility(View.GONE);
                            binding.layoutSheetMembers.allMembersList.setVisibility(View.GONE);

                            for (TabsData tabData : tabsData) {
                                if (tab.getPosition() == tabData.getTab().getPosition()) {

                                    mViewModel.setMemberClickedMutableLiveData(tabData.getMember());
                                    mViewModel.setSize(tabsData.size());

                                    //selecting tab and handling view
                                    tabData.setSelected(true);
                                }
                            }

                        });

                    } else {
                        binding.layoutSheetMembers.tabsProfileList.setSelected(true);
                        binding.layoutSheetMembers.tabsProfileList.setSelectedTabIndicatorColor(requireActivity().getResources().getColor(R.color.orange));

                        //make recyclerview visible and viewpager invisible
                        mViewModel.getTabsListMutableLiveData().observe(getViewLifecycleOwner(), tabsData -> {

                            Log.i("onTabSelectedListTab: ", String.valueOf(tabsData.size()));

                            if (tabsData.size() > 1) {
                                binding.layoutSheetMembers.noMembersFound.setVisibility(View.GONE);
                                binding.layoutSheetMembers.noResultsL.setVisibility(View.GONE);
                                binding.layoutSheetMembers.allMembersList.setVisibility(View.VISIBLE);

                            } else {
                                binding.layoutSheetMembers.noMembersFound.setVisibility(View.VISIBLE);
                                binding.layoutSheetMembers.noResultsL.setVisibility(View.VISIBLE);
                                binding.layoutSheetMembers.noResultsL.playAnimation();
                                binding.layoutSheetMembers.allMembersList.setVisibility(View.GONE);
                            }
                        });

                        binding.layoutSheetMembers.tabsDatatypeList.setVisibility(View.GONE);
                        binding.layoutSheetMembers.viewPager.setVisibility(View.GONE);

                        setAllMembersList();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                if (tab.getCustomView() != null) {
                    if (tab.getCustomView().findViewById(R.id.image_profile_) != null) {
                        CircleImageView imageView = tab.getCustomView().findViewById(R.id.image_profile_);
                        imageView.setBorderColor(0);
                        imageView.setBorderWidth(0);

                        binding.layoutSheetMembers.tabsProfileList.setSelected(false);
                        binding.layoutSheetMembers.tabsProfileList.setSelectedTabIndicatorColor(0);

                        Log.d("onTabUnselected: ", "" + tab.getId());

                        mViewModel.getTabsListMutableLiveData().observe(getViewLifecycleOwner(), tabsData -> {

                            binding.layoutSheetMembers.noMembersFound.setVisibility(View.GONE);
                            binding.layoutSheetMembers.noResultsL.setVisibility(View.GONE);
                            binding.layoutSheetMembers.allMembersList.setVisibility(View.GONE);

                            for (TabsData tabData : tabsData) {
                                if (tab.getPosition() == tabData.getTab().getPosition()) {

                                    //selecting tab and handling view
                                    tabData.setSelected(false);
                                }
                            }

                        });

                    } else {
                        binding.layoutSheetMembers.tabsProfileList.setSelected(false);
                        binding.layoutSheetMembers.tabsProfileList.setSelectedTabIndicatorColor(0);

                        //make recyclerview invisible and viewpager visible
                        mViewModel.getTabsListMutableLiveData().observe(getViewLifecycleOwner(), tabsData -> {

                            if (tabsData.size() > 1 && tab.isSelected()) {
                                binding.layoutSheetMembers.noMembersFound.setVisibility(View.GONE);
                                binding.layoutSheetMembers.noResultsL.setVisibility(View.GONE);
                                binding.layoutSheetMembers.allMembersList.setVisibility(View.VISIBLE);

                            } else {
                                binding.layoutSheetMembers.noMembersFound.setVisibility(View.VISIBLE);
                                binding.layoutSheetMembers.noResultsL.setVisibility(View.VISIBLE);
                                binding.layoutSheetMembers.noResultsL.playAnimation();
                                binding.layoutSheetMembers.allMembersList.setVisibility(View.GONE);

                            }

                        });

                        binding.layoutSheetMembers.tabsDatatypeList.setVisibility(View.VISIBLE);
                        binding.layoutSheetMembers.viewPager.setVisibility(View.VISIBLE);

                        binding.layoutSheetMembers.allMembersList.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                if (tab.getCustomView() != null) {
                    if (tab.getCustomView().findViewById(R.id.image_profile_) != null) {

                        CircleImageView imageView = tab.getCustomView().findViewById(R.id.image_profile_);
                        imageView.setBorderColor(requireActivity().getResources().getColor(R.color.orange));
                        imageView.setBorderWidth(3);

                        binding.layoutSheetMembers.tabsProfileList.setSelected(true);
                        binding.layoutSheetMembers.tabsProfileList.setSelectedTabIndicatorColor(requireActivity().getResources().getColor(R.color.orange));

                        Log.d("onTabSelected: ", "" + tab.getId());

                        mViewModel.getTabsListMutableLiveData().observe(getViewLifecycleOwner(), tabsData -> {

                            binding.layoutSheetMembers.noMembersFound.setVisibility(View.GONE);
                            binding.layoutSheetMembers.noResultsL.setVisibility(View.GONE);
                            binding.layoutSheetMembers.allMembersList.setVisibility(View.GONE);

                            for (TabsData tabData : tabsData) {
                                if (tab.getPosition() == tabData.getTab().getPosition()) {

                                    mViewModel.setMemberClickedMutableLiveData(tabData.getMember());
                                    mViewModel.setSize(tabsData.size());

                                    tabData.setSelected(true);

                                    //selecting tab and handling view
//                                    mViewModel.setNameSelected(tabData.getMember().getMemberFirstName());

                                }
                            }

                        });

                    } else {
                        binding.layoutSheetMembers.tabsProfileList.setSelected(true);
                        binding.layoutSheetMembers.tabsProfileList.setSelectedTabIndicatorColor(requireActivity().getResources().getColor(R.color.orange));

                        //make recyclerview visible and viewpager invisible
                        mViewModel.getTabsListMutableLiveData().observe(getViewLifecycleOwner(), tabsData -> {

                            Log.i("onTabSelectedListTab: ", String.valueOf(tabsData.size()));

                            if (tabsData.size() > 1) {
                                binding.layoutSheetMembers.noMembersFound.setVisibility(View.GONE);
                                binding.layoutSheetMembers.noResultsL.setVisibility(View.GONE);
                                binding.layoutSheetMembers.allMembersList.setVisibility(View.VISIBLE);

                            } else {
                                binding.layoutSheetMembers.noMembersFound.setVisibility(View.VISIBLE);
                                binding.layoutSheetMembers.noResultsL.setVisibility(View.VISIBLE);
                                binding.layoutSheetMembers.noResultsL.playAnimation();
                                binding.layoutSheetMembers.allMembersList.setVisibility(View.GONE);
                            }
                        });

                        binding.layoutSheetMembers.tabsDatatypeList.setVisibility(View.GONE);
                        binding.layoutSheetMembers.viewPager.setVisibility(View.GONE);

                        setAllMembersList();
                    }
                }
            }
        });


        //2nd tab layout
        String[] tabs = {"Day", "Week", "Month", "Year"};

        //adapter
        ViewPager2 viewPager = binding.layoutSheetMembers.viewPager;
        viewPager.setOffscreenPageLimit(1);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this, this, this, this, this, this, this, this, this);
        viewPager.setAdapter(pagerAdapter);

        //setting viewpager and tabLayout
        new TabLayoutMediator(binding.layoutSheetMembers.tabsDatatypeList, viewPager, true, (tab, position) -> {
            tab.setText(tabs[position]);
        }).attach();


        binding.layoutSheetMembers.allMembersList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        binding.layoutSheetMembers.allMembersList.setHasFixedSize(true);


    }

    private void initializeMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(binding.map.getId());

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(requireActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        System.out.println("activity locations buildGoogleApiClient map was invoked: ");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //when this listener is called, the live location icon visibility will be set to VISIBLE
    }


    @SuppressLint("LogNotTimber")
    void setAllMembersToTabList() {

        requireActivity().runOnUiThread(() -> {

            sharedViewModel.getCircleMembersMutableLiveData().observe(getViewLifecycleOwner(), memberDetails -> {

                Log.d("onViewCreatedDrive: ", "" + memberDetails.size());

                if (!memberDetails.isEmpty()) {

                    binding.layoutSheetMembers.allMembersList.setVisibility(View.VISIBLE);
                    binding.layoutSheetMembers.noMembersFound.setVisibility(View.GONE);
                    binding.layoutSheetMembers.noResultsL.setVisibility(View.GONE);

                    for (MemberDetail member : memberDetails) {

                        if (!member.getMemberEmail().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())) {

                            TabLayout.Tab profile = binding.layoutSheetMembers.tabsProfileList.newTab().setCustomView(R.layout.tab_item_layout);

                            CircleImageView profileImage = Objects.requireNonNull(profile.getCustomView()).findViewById(R.id.image_profile_);
                            AppCompatTextView name = profile.getCustomView().findViewById(R.id.profile_name);
                            TextView charater = profile.getCustomView().findViewById(R.id.txt_view_member_first_char);

                            Glide
                                    .with(requireActivity())
                                    .load(member.getMemberImageUrl())
                                    .placeholder(R.drawable.add_profile_img_placeholder)
                                    .into(profileImage);

                            name.setText(member.getMemberFirstName());

                            if (member.getMemberImageUrl().isEmpty()){
                                charater.setVisibility(View.VISIBLE);
                                charater.setText(member.getMemberFirstName().charAt(0));
                            }
                            else{
                                charater.setVisibility(View.GONE);
                            }

                            binding.layoutSheetMembers.tabsProfileList.addTab(profile);

                            mViewModel.setTabsListLiveData(new TabsData(profile, new MemberDetail(
                                    member.getMemberFirstName(),
                                    member.getMemberLastName(),
                                    member.getMemberImageUrl(),
                                    member.getLocationLat(),
                                    member.getLocationLng(),
                                    member.getLocationAddress(),
                                    member.getLocationTimestamp(),
                                    member.isPhoneCharging(),
                                    member.getBatteryPercentage(),
                                    member.getMemberEmail()),
                                    false));

                        } else {
                            CircleImageView profile_image = Objects.requireNonNull(profileTab.getCustomView()).findViewById(R.id.image_profile_);
                            Glide.with(requireActivity())
                                    .load(member.getMemberImageUrl())
                                    .placeholder(R.drawable.add_profile_img_placeholder)
                                    .into(profile_image);

                            AppCompatTextView name = Objects.requireNonNull(profileTab.getCustomView()).findViewById(R.id.profile_name);
                            TextView charater = profileTab.getCustomView().findViewById(R.id.txt_view_member_first_char);

                            name.setText(member.getMemberFirstName());

                            if (member.getMemberImageUrl().isEmpty()){
                                charater.setVisibility(View.VISIBLE);
                                charater.setText(member.getMemberFirstName().charAt(0));
                            }
                            else{
                                charater.setVisibility(View.GONE);
                            }

                            mViewModel.setTabsListLiveData(new TabsData(profileTab, new MemberDetail(
                                    member.getMemberFirstName(),
                                    member.getMemberLastName(),
                                    member.getMemberImageUrl(),
                                    member.getLocationLat(),
                                    member.getLocationLng(),
                                    member.getLocationAddress(),
                                    member.getLocationTimestamp(),
                                    member.isPhoneCharging(),
                                    member.getBatteryPercentage(),
                                    member.getMemberEmail()),
                                    false));

                        }
                    }
                }
            });

        });
    }

    @SuppressLint({"NotifyDataSetChanged", "LogNotTimber"})
    void setAllMembersList() {

        List<MemberDetail> members = new ArrayList<>();

        requireActivity().runOnUiThread(() -> {

            sharedViewModel.getCircleMembersMutableLiveData().observe(getViewLifecycleOwner(), memberDetails -> {

                if (!memberDetails.isEmpty()) {

                    for (MemberDetail member : memberDetails) {
                        if (!member.getMemberEmail().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())) {
                            members.add(member);
                        }
                    }

                    Log.d("onViewCreatedDrive: ", "" + memberDetails.size());

                    listAdapter.setMemberDetailList(members);
                    binding.layoutSheetMembers.allMembersList.setAdapter(listAdapter);

                    listAdapter.notifyDataSetChanged();

                }
            });
        });
    }

    @Override
    public void onAddedMemberClicked(MemberDetail memberClicked) {

        mViewModel.setMemberClickedMutableLiveData(memberClicked);

        binding.layoutSheetMembers.allMembersList.setVisibility(View.GONE);

        mViewModel.getTabsListMutableLiveData().observe(getViewLifecycleOwner(), tabsData -> {

            mViewModel.setSize(tabsData.size());
            for (TabsData tab : tabsData) {

                if (tab.getMember().getMemberEmail().equals(memberClicked.getMemberEmail())) {

                    //selecting tab and handling view
                    tab.setSelected(!tab.isSelected());
                    binding.layoutSheetMembers.tabsProfileList.selectTab(tab.getTab());

                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("LogNotTimber")
    private void UpdateMapLocationsUI(double latitude, double longitude, int index) {

        latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("location " + index);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(String.valueOf(SharedPreference.getFirstNamePref().charAt(0)))));

        Log.i("firstnameUser: ", SharedPreference.getFirstNamePref());
        Log.i("firstnameUser: ", String.valueOf(SharedPreference.getFirstNamePref().charAt(0)));

        dialog.dismiss();

        requireActivity().runOnUiThread(() -> {
            try {

                mGoogleMap.addMarker(markerOptions);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            } catch (Exception e) {
                Log.i("UiUpdate: ", "" + e.getMessage());
            }
        });
    }

    private void UpdateMapLocationsUIOnClick(double latitude, double longitude, MemberDetail memberDetail) {

        latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(memberDetail.getMemberFirstName().concat(" ").concat(memberDetail.getMemberLastName()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(String.valueOf(memberDetail.getMemberFirstName().charAt(0)))));
        markerOptions.anchor((float) 0.5, (float) 0.5);

        Log.i("firstnameUserClicked: ", memberDetail.getMemberFirstName());
        Log.i("latlngUserClicked: ", latLng.toString());
        Log.i("firstnameUserClicked: ", String.valueOf(memberDetail.getMemberFirstName().charAt(0)));

        dialog.dismiss();

        requireActivity().runOnUiThread(() -> {
            try {

                try {
                    Marker marker = mGoogleMap.addMarker(markerOptions);

                    if (marker != null)
                        marker.remove();

                } catch (Exception e) {

                    Log.i("UpdateMapLocationsUIOnClick: ", e.getMessage());
                }

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20.0f));

            } catch (Exception e) {
                Log.i("UiUpdate: ", "" + e.getMessage());
            }
        });

    }

    private Bitmap getMarkerBitmapFromView(String userNameFirstLetter) {

        Log.i("firstnameUserClickedBITMAP: ", userNameFirstLetter);

        bitmap = Bitmap.createBitmap(markerBinding.getRoot().getMeasuredWidth(), markerBinding.getRoot().getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        // setting the user first name char
        markerBinding.textUserNameLetter.setText("");
        markerBinding.textUserNameLetter.setText(userNameFirstLetter);
        markerBinding.getRoot().draw(new Canvas(bitmap));
        return bitmap;
    }


    //listeners to hide the view
    @Override
    public void DayMapButtonClicked() {

        //set coordinator layout to minimum height
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        requireActivity().runOnUiThread(() -> {
            //locations load on clicked user
            dialog.show();
            mViewModel.getMemberClickedMutableLiveData().observe(getViewLifecycleOwner(), memberDetail -> {

                mViewModel.getLocationsListDay(memberDetail.getMemberEmail());

                mViewModel.getDayLocationsLiveData().observe(getViewLifecycleOwner(), locationsRecordModels -> {

                    behavior.setDraggable(true);
                    behavior.setHideable(true);
                    behavior.setPeekHeight(100);

                    if (!locationsRecordModels.isEmpty()) {

                        Log.i( "DayMapButtonClicked: ", String.valueOf(locationsRecordModels.size()));

                        for (int index = 0; index < locationsRecordModels.size(); index++) {

                            UpdateMapLocationsUIOnClick(
                                    locationsRecordModels.get(index).getLocation().latitude,
                                    locationsRecordModels.get(index).getLocation().longitude,
                                    memberDetail);
                        }
                    } else
                        Commons.showToastStatic(requireActivity(), "No locations found");

                });

            });
        });

    }

    @Override
    public void MonthMapButtonClicked() {

        //set coordinator layout to minimum height
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        requireActivity().runOnUiThread(() -> {
            dialog.show();
            //locations load on clicked user
            mViewModel.getMemberClickedMutableLiveData().observe(getViewLifecycleOwner(), memberDetail -> {

                mViewModel.getLocationsListMonth(memberDetail.getMemberEmail());

                mViewModel.getMonthLocationsLiveData().observe(getViewLifecycleOwner(), locationsRecordModels -> {

                    behavior.setDraggable(true);
                    behavior.setHideable(true);
                    behavior.setPeekHeight(100);

                    if (!locationsRecordModels.isEmpty()) {

                        for (int index = 0; index < locationsRecordModels.size(); index++) {

                            UpdateMapLocationsUIOnClick(
                                    locationsRecordModels.get(index).getLocation().latitude,
                                    locationsRecordModels.get(index).getLocation().longitude,
                                    memberDetail);
                        }
                    } else
                        Commons.showToastStatic(requireActivity(), "No locations found");

                });

            });
        });
    }

    @Override
    public void WeekMapButtonClicked() {

        //set coordinator layout to minimum height
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        requireActivity().runOnUiThread(() -> {
            dialog.show();

            //locations load on clicked user
            mViewModel.getMemberClickedMutableLiveData().observe(getViewLifecycleOwner(), memberDetail -> {

                mViewModel.getLocationsListWeek(memberDetail.getMemberEmail());

                mViewModel.getWeekLocationsLiveData().observe(getViewLifecycleOwner(), locationsRecordModels -> {

                    behavior.setDraggable(true);
                    behavior.setHideable(true);
                    behavior.setPeekHeight(100);

                    if (!locationsRecordModels.isEmpty()) {

                        for (int index = 0; index < locationsRecordModels.size(); index++) {

                            UpdateMapLocationsUIOnClick(
                                    locationsRecordModels.get(index).getLocation().latitude,
                                    locationsRecordModels.get(index).getLocation().longitude,
                                    memberDetail);
                        }
                    } else
                        Commons.showToastStatic(requireActivity(), "No locations found");

                });

            });
        });
    }

    @Override
    public void YearMapButtonClicked() {

        //set coordinator layout to minimum height
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        requireActivity().runOnUiThread(() -> {
            dialog.show();

            //locations load on clicked user
            mViewModel.getMemberClickedMutableLiveData().observe(getViewLifecycleOwner(), memberDetail -> {

                mViewModel.getLocationsListYear(memberDetail.getMemberEmail());

                mViewModel.getYearLocationsLiveData().observe(getViewLifecycleOwner(), locationsRecordModels -> {

                    behavior.setDraggable(true);
                    behavior.setHideable(true);
                    behavior.setPeekHeight(100);

                    if (!locationsRecordModels.isEmpty()) {

                        for (int index = 0; index < locationsRecordModels.size(); index++) {

                            UpdateMapLocationsUIOnClick(
                                    locationsRecordModels.get(index).getLocation().latitude,
                                    locationsRecordModels.get(index).getLocation().longitude,
                                    memberDetail);
                        }
                    } else
                        Commons.showToastStatic(requireActivity(), "No locations found");

                });

            });
        });
    }

    //locations data
    @Override
    public void dayLocations() {

        mViewModel.getLocationsListDay(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        requireActivity().runOnUiThread(() -> {
            dialog.show();

            //location observer
            mViewModel.getDayLocationsLiveData().observe(getViewLifecycleOwner(), locationsRecordModels -> {

                behavior.setDraggable(true);
                behavior.setHideable(true);
                behavior.setPeekHeight(100);

                if (!locationsRecordModels.isEmpty()) {

                    for (int index = 0; index < locationsRecordModels.size(); index++) {

                        UpdateMapLocationsUI(
                                locationsRecordModels.get(index).getLocation().latitude,
                                locationsRecordModels.get(index).getLocation().longitude,
                                index);
                    }
                } else
                    Commons.showToastStatic(requireActivity(), "No locations found");

            });
        });


    }

    @Override
    public void monthLocations() {

        mViewModel.getLocationsListMonth(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        requireActivity().runOnUiThread(() -> {
            dialog.show();
            //location observer
            mViewModel.getMonthLocationsLiveData().observe(getViewLifecycleOwner(), locationsRecordModels -> {

                behavior.setDraggable(true);
                behavior.setHideable(true);
                behavior.setPeekHeight(100);

                if (!locationsRecordModels.isEmpty()) {

                    for (int index = 0; index < locationsRecordModels.size(); index++) {

                        UpdateMapLocationsUI(
                                locationsRecordModels.get(index).getLocation().latitude,
                                locationsRecordModels.get(index).getLocation().longitude,
                                index);
                    }
                } else
                    Commons.showToastStatic(requireActivity(), "No locations found");

            });
        });

    }

    @Override
    public void weekLocations() {

        mViewModel.getLocationsListWeek(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        requireActivity().runOnUiThread(() -> {

            dialog.show();

            //location observer
            mViewModel.getWeekLocationsLiveData().observe(getViewLifecycleOwner(), locationsRecordModels -> {

                behavior.setDraggable(true);
                behavior.setHideable(true);
                behavior.setPeekHeight(100);

                if (!locationsRecordModels.isEmpty()) {

                    for (int index = 0; index < locationsRecordModels.size(); index++) {

                        UpdateMapLocationsUI(
                                locationsRecordModels.get(index).getLocation().latitude,
                                locationsRecordModels.get(index).getLocation().longitude,
                                index);
                    }
                } else
                    Commons.showToastStatic(requireActivity(), "No locations found");

            });
        });

    }

    @Override
    public void yearLocations() {

        mViewModel.getLocationsListYear(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        requireActivity().runOnUiThread(() -> {

            dialog.show();
            //location observer
            mViewModel.getYearLocationsLiveData().observe(getViewLifecycleOwner(), locationsRecordModels -> {

                behavior.setDraggable(true);
                behavior.setHideable(true);
                behavior.setPeekHeight(100);

                if (!locationsRecordModels.isEmpty()) {

                    for (int index = 0; index < locationsRecordModels.size(); index++) {

                        UpdateMapLocationsUI(
                                locationsRecordModels.get(index).getLocation().latitude,
                                locationsRecordModels.get(index).getLocation().longitude,
                                index);
                    }
                } else
                    Commons.showToastStatic(requireActivity(), "No locations found");

            });
        });

    }
}