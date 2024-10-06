package com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.monthData;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.care360.findmyfamilyandfriends.BuildConfig;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.DrivingViewModel;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.model.MovementRecordModel;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.Util.Commons;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.care360.findmyfamilyandfriends.databinding.FragmentMonthDataBinding;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class MonthDataFragment extends Fragment {

    private final ClickedOnMapMonth clickedOnMapMonth;
    private final MonthLocations monthLocations;
    private DrivingViewModel drivingViewModel;
    private FragmentMonthDataBinding binding;
    private boolean itemChanged = false;
    private ProgressDialog dialog;
    private PieData data;
    private PieDataSet dataSet;
    private ArrayList<PieEntry> entries;
    private ShimmerDrawable shimmerDrawable;

    private AdRequest adRequest;

    public MonthDataFragment(ClickedOnMapMonth clickedOnMapMonth,MonthLocations monthLocations) {
        this.clickedOnMapMonth = clickedOnMapMonth;
        this.monthLocations = monthLocations;
    }

    @SuppressLint("LogNotTimber")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        drivingViewModel = new ViewModelProvider(requireActivity()).get(DrivingViewModel.class);

        binding = FragmentMonthDataBinding.inflate(inflater, container, false);

        dialog = new ProgressDialog(requireActivity(),R.style.AppCompatAlertDialogStyle);
        dialog.setMessage("Loading....");

        binding.chart.invalidate();

        entries = new ArrayList<>();

        monthLocations.monthLocations();

        //initializing shimmer
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#DACCCF"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#F3F3F3"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();

        //initializing shimmer drawable
        shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        //banner
        adRequest = new AdRequest.Builder().build();

        return binding.getRoot();
    }

    @SuppressLint({"LogNotTimber", "NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BannerAds();

        //shimmer
        binding.shimmerLayoutMonth.setVisibility(View.VISIBLE);
        binding.shimmerLayoutMonth.startShimmer();

        data = new PieData();

        binding.chart.invalidate();

        binding.chart.getDescription().setEnabled(false);
        binding.chart.setTouchEnabled(true);
        binding.chart.setDrawHoleEnabled(true);
        binding.chart.setHoleColor(Color.WHITE);
        binding.chart.setTransparentCircleColor(Color.WHITE);
        binding.chart.setTransparentCircleAlpha(110);
        binding.chart.setHoleRadius(35f);
        binding.chart.setTransparentCircleRadius(35f);
        binding.chart.setDrawEntryLabels(false);


        //current user data
        requireActivity().runOnUiThread(() -> {

            if (!itemChanged) {

                binding.progressList.setVisibility(View.VISIBLE);
                binding.progressChart.setVisibility(View.VISIBLE);
                binding.movementRecordLayout.movementRecordLayout.setVisibility(View.GONE);

                //shimmer
                binding.shimmerLayoutMonth.setVisibility(View.VISIBLE);
                binding.shimmerLayoutMonth.startShimmer();

                dialog.show();

                drivingViewModel.getMovementsMonthLiveData().observe(getViewLifecycleOwner(), Movements -> {

                    ArrayList<MovementRecordModel> stillStates = new ArrayList<>();
                    ArrayList<MovementRecordModel> walkingStates = new ArrayList<>();
                    ArrayList<MovementRecordModel> runningStates = new ArrayList<>();
                    ArrayList<MovementRecordModel> onFootStates = new ArrayList<>();
                    ArrayList<MovementRecordModel> onBicycleStates = new ArrayList<>();
                    ArrayList<MovementRecordModel> onVehicleStates = new ArrayList<>();

                    try {
                        dialog.dismiss();

                        if (!Movements.isEmpty()) {

                            Log.i("MonthMovementsStillFrag: ", Movements.toString());
                            Log.i("MonthMovementsStillFragSize: ", String.valueOf(Movements.size()));

                            binding.movementRecordLayout.movementRecordLayout.setVisibility(View.VISIBLE);
                            binding.noActivitiesFound.setVisibility(View.GONE);
                            binding.chart.setVisibility(View.VISIBLE);
                            binding.progressList.setVisibility(View.GONE);

                            binding.progressList.setVisibility(View.GONE);
                            binding.progressChart.setVisibility(View.GONE);

                            //shimmer
                            binding.shimmerLayoutMonth.setVisibility(View.GONE);
                            binding.shimmerLayoutMonth.stopShimmer();

                            for (MovementRecordModel movement : Movements){
                                if (movement.getMovementName().equals("STILL")){
                                    stillStates.add(new MovementRecordModel(
                                            movement.getMovementName(),
                                            movement.getMovementDuration(),
                                            movement.getMovementTime(),
                                            movement.getMovementTimeStamp()));

                                    Log.i( "sortedDataObject: ",movement.toString());
                                }
                                else if (movement.getMovementName().equals("WALKING")){
                                    walkingStates.add(new MovementRecordModel(
                                            movement.getMovementName(),
                                            movement.getMovementDuration(),
                                            movement.getMovementTime(),
                                            movement.getMovementTimeStamp()));
                                }
                                else if (movement.getMovementName().equals("RUNNING")){
                                    runningStates.add(new MovementRecordModel(
                                            movement.getMovementName(),
                                            movement.getMovementDuration(),
                                            movement.getMovementTime(),
                                            movement.getMovementTimeStamp()));
                                }
                                else if (movement.getMovementName().equals("ON FOOT")){
                                    onFootStates.add(new MovementRecordModel(
                                            movement.getMovementName(),
                                            movement.getMovementDuration(),
                                            movement.getMovementTime(),
                                            movement.getMovementTimeStamp()));
                                }
                                else if (movement.getMovementName().equals("ON BICYCLE")){
                                    onBicycleStates.add(new MovementRecordModel(
                                            movement.getMovementName(),
                                            movement.getMovementDuration(),
                                            movement.getMovementTime(),
                                            movement.getMovementTimeStamp()));
                                }
                                else if (movement.getMovementName().equals("IN VEHICLE")){
                                    onVehicleStates.add(new MovementRecordModel(
                                            movement.getMovementName(),
                                            movement.getMovementDuration(),
                                            movement.getMovementTime(),
                                            movement.getMovementTimeStamp()));
                                }
                            }

                            //Still state
                            Log.i( "sortedData: ", String.valueOf(stillStates.size()));
                            calculateDurationAndSetDataStill(stillStates);
                            //walking state
                            calculateDurationAndSetDataWalk(walkingStates);
                            Log.i( "sortedData: ",walkingStates.toString());
                            //Running
                            calculateDurationAndSetDataRunning(runningStates);
                            Log.i( "sortedData: ",runningStates.toString());
                            //ON FOOT
                            calculateDurationAndSetDataOnFoot(onFootStates);
                            Log.i( "sortedData: ",onFootStates.toString());
                            //ON BICYCLE
                            calculateDurationAndSetDataOnBicycle(onBicycleStates);
                            Log.i( "sortedData: ",onBicycleStates.toString());
                            //IN VEHICLE
                            calculateDurationAndSetDataOnVehicle(onVehicleStates);
                            Log.i( "sortedData: ",onVehicleStates.toString());

                        }
                        else{
                            binding.movementRecordLayout.movementRecordLayout.setVisibility(View.GONE);
                            binding.noActivitiesFound.setVisibility(View.VISIBLE);
                            binding.chart.setVisibility(View.GONE);
                            binding.progressList.setVisibility(View.GONE);
                            binding.progressChart.setVisibility(View.GONE);

                            //shimmer
                            binding.shimmerLayoutMonth.setVisibility(View.GONE);
                            binding.shimmerLayoutMonth.stopShimmer();
                        }
                    } catch (Exception e) {
                        Log.i("movementModelError: ", e.getMessage());
                    }

                });

            }

        });

        //clicked user data
        requireActivity().runOnUiThread(() -> {

            binding.progressList.setVisibility(View.VISIBLE);
            binding.progressChart.setVisibility(View.VISIBLE);
            binding.movementRecordLayout.movementRecordLayout.setVisibility(View.GONE);

            //shimmer
            binding.shimmerLayoutMonth.setVisibility(View.VISIBLE);
            binding.shimmerLayoutMonth.startShimmer();

            dialog.show();

            drivingViewModel.getMemberClickedMutableLiveData().observe(getViewLifecycleOwner(), memberDetail -> {

                itemChanged = true;

                Log.d("DataClickedMember: ", "" + memberDetail.getBatteryPercentage());

                dialog.dismiss();

                //image profile
                if (!memberDetail.getMemberImageUrl().equals(Constants.NULL))
                    Glide.with(requireActivity()).load(memberDetail.getMemberImageUrl()).placeholder(shimmerDrawable).into(binding.userProfileItemview.imgViewMemberProfile);
                else
                    Glide.with(requireActivity()).load(memberDetail.getMemberImageUrl()).placeholder(R.drawable.add_profile_img_placeholder).into(binding.userProfileItemview.imgViewMemberProfile);


                binding.userProfileItemview.txtViewMemberName.setText(memberDetail.getMemberFirstName() + " " + memberDetail.getMemberLastName());

                binding.userProfileItemview.txtViewLastKnownAddress.setText(memberDetail.getLocationAddress());

                if (memberDetail.getLocationTimestamp() != 0) {
                    // time stamp
                    binding.userProfileItemview.txtViewTimestamp.setText(requireActivity().getString(R.string.last_seen).concat(" ").concat(Commons.timeInMilliToDateFormat(String.valueOf(memberDetail.getLocationTimestamp()))));
                }

                binding.userProfileItemview.txtViewBatteryPercentage.setText(String.valueOf(memberDetail.getBatteryPercentage()).concat(" %"));

                // phone charging status
                if (memberDetail.isPhoneCharging()) {
                    if (memberDetail.getBatteryPercentage() <= 20) {
                        binding.userProfileItemview.imgViewBatteryStatus.setImageResource(R.drawable.ic_battery_low_charging);
                    } else if (memberDetail.getBatteryPercentage() > 20) {
                        binding.userProfileItemview.imgViewBatteryStatus.setImageResource(R.drawable.ic_battery_good_charging);
                    }
                } else {
                    if (memberDetail.getBatteryPercentage() <= 20) {
                        binding.userProfileItemview.imgViewBatteryStatus.setImageResource(R.drawable.ic_battery_low);
                    } else if (memberDetail.getBatteryPercentage() > 20) {
                        binding.userProfileItemview.imgViewBatteryStatus.setImageResource(R.drawable.ic_battery_good);
                    }
                }

                if (drivingViewModel.getSize()>1){

                    //Activity load on clicked user
                    drivingViewModel.getActivityStateListMonth(memberDetail.getMemberEmail());

                    //Activity
                    drivingViewModel.getMovementsMonthLiveData().observe(getViewLifecycleOwner(), Movements -> {

                        ArrayList<MovementRecordModel> stillStates = new ArrayList<>();
                        ArrayList<MovementRecordModel> walkingStates = new ArrayList<>();
                        ArrayList<MovementRecordModel> runningStates = new ArrayList<>();
                        ArrayList<MovementRecordModel> onFootStates = new ArrayList<>();
                        ArrayList<MovementRecordModel> onBicycleStates = new ArrayList<>();
                        ArrayList<MovementRecordModel> onVehicleStates = new ArrayList<>();

                        try {
                            dialog.dismiss();

                            if (!Movements.isEmpty()) {

                                Log.i("MonthMovementsFrag: ", Movements.toString());
                                Log.i("MonthMovementsStillFragSize: ", String.valueOf(Movements.size()));

                                binding.movementRecordLayout.movementRecordLayout.setVisibility(View.VISIBLE);
                                binding.noActivitiesFound.setVisibility(View.GONE);
                                binding.chart.setVisibility(View.VISIBLE);
                                binding.progressList.setVisibility(View.GONE);

                                binding.progressList.setVisibility(View.GONE);
                                binding.progressChart.setVisibility(View.GONE);

                                //shimmer
                                binding.shimmerLayoutMonth.setVisibility(View.GONE);
                                binding.shimmerLayoutMonth.stopShimmer();

                                for (MovementRecordModel movement : Movements){
                                    if (movement.getMovementName().equals("STILL")){
                                        stillStates.add(new MovementRecordModel(
                                                movement.getMovementName(),
                                                movement.getMovementDuration(),
                                                movement.getMovementTime(),
                                                movement.getMovementTimeStamp()));

                                        Log.i( "sortedDataObject: ",movement.toString());
                                    }
                                    else if (movement.getMovementName().equals("WALKING")){
                                        walkingStates.add(new MovementRecordModel(
                                                movement.getMovementName(),
                                                movement.getMovementDuration(),
                                                movement.getMovementTime(),
                                                movement.getMovementTimeStamp()));
                                    }
                                    else if (movement.getMovementName().equals("RUNNING")){
                                        runningStates.add(new MovementRecordModel(
                                                movement.getMovementName(),
                                                movement.getMovementDuration(),
                                                movement.getMovementTime(),
                                                movement.getMovementTimeStamp()));
                                    }
                                    else if (movement.getMovementName().equals("ON FOOT")){
                                        onFootStates.add(new MovementRecordModel(
                                                movement.getMovementName(),
                                                movement.getMovementDuration(),
                                                movement.getMovementTime(),
                                                movement.getMovementTimeStamp()));
                                    }
                                    else if (movement.getMovementName().equals("ON BICYCLE")){
                                        onBicycleStates.add(new MovementRecordModel(
                                                movement.getMovementName(),
                                                movement.getMovementDuration(),
                                                movement.getMovementTime(),
                                                movement.getMovementTimeStamp()));
                                    }
                                    else if (movement.getMovementName().equals("IN VEHICLE")){
                                        onVehicleStates.add(new MovementRecordModel(
                                                movement.getMovementName(),
                                                movement.getMovementDuration(),
                                                movement.getMovementTime(),
                                                movement.getMovementTimeStamp()));
                                    }
                                }

                                //Still state
                                Log.i( "sortedData: ", String.valueOf(stillStates.size()));
                                calculateDurationAndSetDataStill(stillStates);
                                //walking state
                                calculateDurationAndSetDataWalk(walkingStates);
                                Log.i( "sortedData: ",walkingStates.toString());
                                //Running
                                calculateDurationAndSetDataRunning(runningStates);
                                Log.i( "sortedData: ",runningStates.toString());
                                //ON FOOT
                                calculateDurationAndSetDataOnFoot(onFootStates);
                                Log.i( "sortedData: ",onFootStates.toString());
                                //ON BICYCLE
                                calculateDurationAndSetDataOnBicycle(onBicycleStates);
                                Log.i( "sortedData: ",onBicycleStates.toString());
                                //IN VEHICLE
                                calculateDurationAndSetDataOnVehicle(onVehicleStates);
                                Log.i( "sortedData: ",onVehicleStates.toString());

                            }
                            else{
                                binding.movementRecordLayout.movementRecordLayout.setVisibility(View.GONE);
                                binding.noActivitiesFound.setVisibility(View.VISIBLE);
                                binding.chart.setVisibility(View.GONE);
                                binding.progressList.setVisibility(View.GONE);
                                binding.progressChart.setVisibility(View.GONE);

                                //shimmer
                                binding.shimmerLayoutMonth.setVisibility(View.GONE);
                                binding.shimmerLayoutMonth.stopShimmer();
                            }
                        } catch (Exception e) {
                            Log.i("movementModelError: ", e.getMessage());
                        }

                    });
                }
                else{
                    Log.i( "noMembers: ","No members found!");
                }
            });
        });

        entries = new ArrayList<>();

        //SETTING CHART
        dataSet = new PieDataSet(entries, "Survey Results");

        data.addDataSet(dataSet);
        binding.chart.setData(data);
        binding.chart.notifyDataSetChanged();

        Log.i("chartEntries: ", String.valueOf(dataSet));

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);


        binding.locationCardButtonMonth.setOnClickListener(v -> {
            clickedOnMapMonth.mapClickButtonMonth();
        });
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
//        binding.adaptiveBanner.addView(adView);
        adView.loadAd(adRequest);
    }

    @SuppressLint("SetTextI18n")
    public void calculateDurationAndSetDataStill(ArrayList<MovementRecordModel> stillStates){

        if (stillStates.size()>1){

            Log.i( "calculateDurationAndSetDataStill_InIF: ", String.valueOf(stillStates.size()));

            binding.movementRecordLayout.stillLayout.setVisibility(View.VISIBLE);
            binding.progressList.setVisibility(View.GONE);
            binding.progressChart.setVisibility(View.GONE);

            //set data to the view
            binding.movementRecordLayout.movementRecordTimeStill.setText(stillStates.get(stillStates.size() - 1).getMovementTime());

            //adding values
            int duration = 0;
            for (int index = 0; index < stillStates.size()-1; index++) {
                duration += Integer.parseInt(stillStates.get(index).getMovementDuration());
            }

            Log.i("durationCalculatedStill: ", String.
                    valueOf(duration));

            //set values to
            binding.movementRecordLayout.movementLengthStill.setText(duration+ " "+ "mins");

            entries.clear();
            entries.add(new PieEntry(Float.parseFloat(String.valueOf(duration)), "STILL"));

        }
        else{
            binding.movementRecordLayout.stillLayout.setVisibility(View.GONE);
            binding.movementRecordLayout.movementLengthStill.setText(stillStates.get(0).getMovementDuration());

        }
    }
    @SuppressLint("SetTextI18n")
    public void calculateDurationAndSetDataWalk(ArrayList<MovementRecordModel> walkingStates){
        if (walkingStates.size()>1){
            binding.movementRecordLayout.walkingLayout.setVisibility(View.VISIBLE);
            binding.progressList.setVisibility(View.GONE);
            binding.progressChart.setVisibility(View.GONE);

            //set data to the view
            binding.movementRecordLayout.movementRecordTimeWalk.setText(walkingStates.get(walkingStates.size() - 1).getMovementTime());

            //adding values
            int duration = 0;
            for (int index = 0; index < walkingStates.size()-1; index++) {
                duration += Integer.parseInt(walkingStates.get(index).getMovementDuration());
            }
            Log.i("durationCalculatedStill: ", String.
                    valueOf(duration));

            //set values to
            binding.movementRecordLayout.movementLengthWalk.setText(duration+ " "+ "mins");

            entries.add(new PieEntry(Float.parseFloat(String.valueOf(duration)), "WALKING"));

        }
        else{
            binding.movementRecordLayout.walkingLayout.setVisibility(View.GONE);
            binding.movementRecordLayout.movementLengthWalk.setText(walkingStates.get(0).getMovementDuration());
        }
    }
    @SuppressLint("SetTextI18n")
    public void calculateDurationAndSetDataRunning(ArrayList<MovementRecordModel> runningStates){

        if (runningStates.size()>1){
            binding.movementRecordLayout.runningLayout.setVisibility(View.VISIBLE);
            binding.progressList.setVisibility(View.GONE);
            binding.progressChart.setVisibility(View.GONE);

            //set data to the view
            binding.movementRecordLayout.movementRecordTimeRunning.setText(runningStates.get(runningStates.size() - 1).getMovementTime());

            //adding values
            int duration = 0;
            for (int index = 0; index < runningStates.size()-1; index++) {
                duration += Integer.parseInt(runningStates.get(index).getMovementDuration());
            }
            Log.i("durationCalculatedStill: ", String.
                    valueOf(duration));

            //set values to
            binding.movementRecordLayout.movementLengthRunning.setText(duration+ " "+ "mins");

            entries.add(new PieEntry(Float.parseFloat(String.valueOf(duration)), "RUNNING"));

        }
        else{
            binding.movementRecordLayout.runningLayout.setVisibility(View.GONE);
            binding.movementRecordLayout.movementLengthRunning.setText(runningStates.get(0).getMovementDuration());
        }
    }
    @SuppressLint("SetTextI18n")
    public void calculateDurationAndSetDataOnFoot(ArrayList<MovementRecordModel> onFootStates){

        if (onFootStates.size()>1){

            binding.movementRecordLayout.onfootLayout.setVisibility(View.VISIBLE);
            binding.progressList.setVisibility(View.GONE);
            binding.progressChart.setVisibility(View.GONE);

            //set data to the view
            binding.movementRecordLayout.movementRecordTimeOnfoot.setText(onFootStates.get(onFootStates.size() - 1).getMovementTime());

            //adding values
            int duration = 0;
            for (int index = 0; index < onFootStates.size()-1; index++) {
                duration += Integer.parseInt(onFootStates.get(index).getMovementDuration());
            }
            Log.i("durationCalculatedStill: ", String.
                    valueOf(duration));

            //set values to
            binding.movementRecordLayout.movementLengthOnfoot.setText(duration+ " "+ "mins");

            entries.add(new PieEntry(Float.parseFloat(String.valueOf(duration)), "On FOOT"));

        }
        else{
            binding.movementRecordLayout.onfootLayout.setVisibility(View.GONE);
            binding.movementRecordLayout.movementLengthOnfoot.setText(onFootStates.get(0).getMovementDuration());
        }
    }
    @SuppressLint("SetTextI18n")
    public void calculateDurationAndSetDataOnBicycle(ArrayList<MovementRecordModel> onBicycleStates){

        if (onBicycleStates.size()>1){

            binding.movementRecordLayout.onbicycleLayout.setVisibility(View.VISIBLE);
            binding.progressList.setVisibility(View.GONE);
            binding.progressChart.setVisibility(View.GONE);

            //set data to the view
            binding.movementRecordLayout.movementRecordTimeOnbicycle.setText(onBicycleStates.get(onBicycleStates.size() - 1).getMovementTime());

            //adding values
            int duration = 0;
            for (int index = 0; index < onBicycleStates.size()-1; index++) {
                duration += Integer.parseInt(onBicycleStates.get(index).getMovementDuration());
            }
            Log.i("durationCalculatedStill: ", String.valueOf(duration));

            //set values to
            binding.movementRecordLayout.movementLengthOnbicycle.setText(duration+ " "+ "mins");

            entries.add(new PieEntry(Float.parseFloat(String.valueOf(duration)), "On BICYCLE"));

        }
        else{
            binding.movementRecordLayout.onbicycleLayout.setVisibility(View.GONE);
            binding.movementRecordLayout.movementLengthOnbicycle.setText(onBicycleStates.get(0).getMovementDuration());
        }
    }
    @SuppressLint("SetTextI18n")
    public void calculateDurationAndSetDataOnVehicle(ArrayList<MovementRecordModel> onVehicleStates){

        if (onVehicleStates.size()>1){

            binding.movementRecordLayout.invehicleLayout.setVisibility(View.VISIBLE);
            binding.progressList.setVisibility(View.GONE);
            binding.progressChart.setVisibility(View.GONE);

            //set data to the view
            binding.movementRecordLayout.movementRecordTimeInvehicle.setText(onVehicleStates.get(onVehicleStates.size() - 1).getMovementTime());

            //adding values
            int duration = 0;
            for (int index = 0; index < onVehicleStates.size()-1; index++) {
                duration += Integer.parseInt(onVehicleStates.get(index).getMovementDuration());
            }
            Log.i("durationCalculatedStill: ", String.valueOf(duration));

            //set values to
            binding.movementRecordLayout.movementLengthInvehicle.setText(duration+ " "+ "mins");

            entries.add(new PieEntry(Float.parseFloat(String.valueOf(duration)), "In VEHICLE"));

        }
        else{
            binding.movementRecordLayout.invehicleLayout.setVisibility(View.GONE);
            binding.movementRecordLayout.movementLengthInvehicle.setText(onVehicleStates.get(0).getMovementDuration());
        }
    }

    public interface ClickedOnMapMonth{
        void mapClickButtonMonth();
    }

    public interface MonthLocations{
        void monthLocations();
    }
}