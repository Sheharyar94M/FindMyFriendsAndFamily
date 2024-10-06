package com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.care360.findmyfamilyandfriends.Application.App;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.models.TabsData;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.model.LocationsRecordModel;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.model.MovementRecordModel;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers.MemberDetail;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.JoinCircle.CircleModel;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DrivingViewModel extends ViewModel {

    private final List<CircleModel> circleList;
    private final List<MemberDetail> membersDetailList;

    private final MutableLiveData<MemberDetail> memberClickedMutableLiveData;

    private final MutableLiveData<List<TabsData>> tabsListMutableLiveData;
    private final List<TabsData> tabs;

    private final MutableLiveData<ArrayList<MovementRecordModel>> movementsDayLiveData;
    private final MutableLiveData<ArrayList<MovementRecordModel>> movementsWeekLiveData;
    private final MutableLiveData<ArrayList<MovementRecordModel>> movementsMonthLiveData;
    private final MutableLiveData<ArrayList<MovementRecordModel>> movementsYearLiveData;
    //Locations
    private final MutableLiveData<List<LocationsRecordModel>> dayLocationsLiveData;
    private final MutableLiveData<List<LocationsRecordModel>> weekLocationsLiveData;
    private final MutableLiveData<List<LocationsRecordModel>> monthLocationsLiveData;
    private final MutableLiveData<List<LocationsRecordModel>> yearLocationsLiveData;
    private int size;
    private String memberFirstName;

    public DrivingViewModel() {
        tabsListMutableLiveData = new MutableLiveData<>();
        memberClickedMutableLiveData = new MutableLiveData<>();
        circleList = new ArrayList<>();
        membersDetailList = new ArrayList<>();
        tabs = new ArrayList<>();

        //movements
        movementsDayLiveData = new MutableLiveData<>();
        movementsWeekLiveData = new MutableLiveData<>();
        movementsMonthLiveData = new MutableLiveData<>();
        movementsYearLiveData = new MutableLiveData<>();

        //locations
        dayLocationsLiveData = new MutableLiveData<>();
        weekLocationsLiveData = new MutableLiveData<>();
        monthLocationsLiveData = new MutableLiveData<>();
        yearLocationsLiveData = new MutableLiveData<>();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public MutableLiveData<List<LocationsRecordModel>> getDayLocationsLiveData() {
        return dayLocationsLiveData;
    }

    public MutableLiveData<List<LocationsRecordModel>> getWeekLocationsLiveData() {
        return weekLocationsLiveData;
    }

    public MutableLiveData<List<LocationsRecordModel>> getMonthLocationsLiveData() {
        return monthLocationsLiveData;
    }

    public MutableLiveData<List<LocationsRecordModel>> getYearLocationsLiveData() {
        return yearLocationsLiveData;
    }

    public MutableLiveData<ArrayList<MovementRecordModel>> getMovementsDayLiveData() {
        return movementsDayLiveData;
    }

    public MutableLiveData<ArrayList<MovementRecordModel>> getMovementsWeekLiveData() {
        return movementsWeekLiveData;
    }

    public MutableLiveData<ArrayList<MovementRecordModel>> getMovementsMonthLiveData() {
        return movementsMonthLiveData;
    }

    public MutableLiveData<ArrayList<MovementRecordModel>> getMovementsYearLiveData() {
        return movementsYearLiveData;
    }

    public MutableLiveData<MemberDetail> getMemberClickedMutableLiveData() {
        return memberClickedMutableLiveData;
    }

    public void setMemberClickedMutableLiveData(MemberDetail memberClicked) {
        memberClickedMutableLiveData.postValue(memberClicked);
    }

    public MutableLiveData<List<TabsData>> getTabsListMutableLiveData() {
        return tabsListMutableLiveData;
    }

    public void setTabsListLiveData(TabsData tab) {
        tabs.add(tab);
        tabsListMutableLiveData.postValue(tabs);
    }

    //Locations Data
    @SuppressLint("LogNotTimber")
    public void getLocationsListDay(String email) {

        List<LocationsRecordModel> locations = new ArrayList<>();

        long startInterval = System.currentTimeMillis();
        long endInterval = startInterval - (24 * 60 * 60 * 1000);

        Log.i("intervals: ", endInterval + " "+ startInterval);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                        .document(email)
                        .collection(Constants.LOCATION_COLLECTION)
                        .whereLessThanOrEqualTo(Constants.LOC_TIMESTAMP, startInterval)
                        .whereGreaterThanOrEqualTo(Constants.LOC_TIMESTAMP, endInterval)
                        .limit(100)
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {

                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()){

                                Log.i("getLocationsListDay: ", "" + snap.getDouble("lat") + " " + snap.getDouble("lng"));
                                locations.add(new LocationsRecordModel(new LatLng(snap.getDouble("lat"), snap.getDouble("lng"))));

                            }



                            dayLocationsLiveData.postValue(locations);
                            executor.shutdown();

                            Log.d("getLocationsListDay: ", "" +
                                    Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));

                        }).addOnFailureListener(e -> {

                            Log.d("getLocationsListWeek: ", "" + e.getMessage());

                        });
            }catch (Exception e){
                Log.i("getLocationsListDayExp: ",e.getMessage());
            }
        });
    }
    @SuppressLint("LogNotTimber")
    public void getLocationsListWeek(String email) {

        List<LocationsRecordModel> locations = new ArrayList<>();

        long startInterval = System.currentTimeMillis();
        long endInterval = startInterval - (7 * 24 * 60 * 60 * 1000);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                    .document(email)
                    .collection(Constants.LOCATION_COLLECTION)
                    .whereLessThanOrEqualTo(Constants.LOC_TIMESTAMP, startInterval)
                    .whereGreaterThanOrEqualTo(Constants.LOC_TIMESTAMP, endInterval)
                    .limit(100)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {

                        for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments())
                            locations.add(new LocationsRecordModel(new LatLng(snap.getDouble("lat"), snap.getDouble("lng"))));


                        weekLocationsLiveData.postValue(locations);
                        executor.shutdown();

                        Log.d("getLocationsListWeek: ", "" + queryDocumentSnapshots.size());
                        Log.d("getLocationsListWeek: ", "" +
                                Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));


                    }).addOnFailureListener(e -> {

                        Log.d("getLocationsListWeek: ", "" + e.getMessage());

                    });
        });

    }
    @SuppressLint("LogNotTimber")
    public void getLocationsListMonth(String email) {

        List<LocationsRecordModel> locations = new ArrayList<>();

        long startInterval = System.currentTimeMillis();
        long endInterval = startInterval - (30L * 24 * 60 * 60 * 1000);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                    .document(email)
                    .collection(Constants.LOCATION_COLLECTION)
                    .whereLessThanOrEqualTo(Constants.LOC_TIMESTAMP, startInterval)
                    .whereGreaterThanOrEqualTo(Constants.LOC_TIMESTAMP, endInterval)
                    .limit(100)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
//                        Toast.makeText(App.getAppContext(), "ReadsMonth: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                        Log.d("getLocationsListMonth: ", "" + queryDocumentSnapshots.size());

                        for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments())
                            locations.add(new LocationsRecordModel(new LatLng(snap.getDouble("lat"), snap.getDouble("lng"))));


                        monthLocationsLiveData.postValue(locations);
                        executor.shutdown();

                        Log.d("getLocationsListMonth: ", "" +
                                Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));


                    }).addOnFailureListener(e -> {

                        Log.d("getLocationsListWeek: ", "" + e.getMessage());

                    });
        });
    }
    @SuppressLint("LogNotTimber")
    public void getLocationsListYear(String email) {
        List<LocationsRecordModel> locations = new ArrayList<>();

        long startInterval = System.currentTimeMillis();
        long endInterval = startInterval - (365L * 24 * 60 * 60 * 1000);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                    .document(email)
                    .collection(Constants.LOCATION_COLLECTION)
                    .whereLessThanOrEqualTo(Constants.LOC_TIMESTAMP, startInterval)
                    .whereGreaterThanOrEqualTo(Constants.LOC_TIMESTAMP, endInterval)
                    .limit(100)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {

                        Log.d("getLocationsListYear: ", "" + queryDocumentSnapshots.size());

                        for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments())
                            locations.add(new LocationsRecordModel(new LatLng(snap.getDouble("lat"), snap.getDouble("lng"))));

                        yearLocationsLiveData.postValue(locations);
                        executor.shutdown();

                        Log.d("getLocationsListWeek: ", "" +
                                Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));


                    }).addOnFailureListener(e -> {

                        Log.d("getLocationsListWeek: ", "" + e.getMessage());

                    });
        });
    }

    //Activity Data
    @SuppressLint("SimpleDateFormat")
    public void getActivityStateListDay(String email) {

        ArrayList<MovementRecordModel> dayActivityStateList = new ArrayList<>();

        long currentDayMilli = System.currentTimeMillis();
        long oneDaysMillis = currentDayMilli - (24 * 60 * 60 * 1000);

        Log.i("currentHourAndlasthourDay: ", " oneDaysMillis " + oneDaysMillis + " currentdayMillis " + currentDayMilli);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            Query query = FirebaseFirestore.getInstance()
                    .collection(Constants.USERS_COLLECTION)
                    .document(email)
                    .collection(Constants.ACTIVITY_COLLECTION)
                    .whereGreaterThan(Constants.ACTIVITY_TIMESTAMP, oneDaysMillis)
                    .whereLessThan(Constants.ACTIVITY_TIMESTAMP, currentDayMilli)
                    .orderBy(Constants.ACTIVITY_TIMESTAMP, Query.Direction.ASCENDING);

            try {

                query.get()
                        .addOnSuccessListener(querySnapshots -> {

                            Log.i("movementListDay ", querySnapshots.getDocuments().toString());

                            for (DocumentSnapshot snapshot : querySnapshots.getDocuments()) {

                                dayActivityStateList.add(new MovementRecordModel(
                                        snapshot.getString("activity_name"),
                                        snapshot.getString("activity_duration"),
                                        snapshot.getString("activity_time"),
                                        snapshot.getLong("activity_timestamp")
                                ));

                            }

                            //logging all of the day data
                            Log.i("ActivitiesListDay ", dayActivityStateList.toString());


                            //setting all of the day data to live data
                            movementsDayLiveData.postValue(dayActivityStateList);
                            executor.shutdown();

                        }).addOnFailureListener(e -> {

                            Log.i("ACTIVITIESListDAYFailure: ", "" + e.getMessage());

                        });
            } catch (Exception e) {
                Log.i("querySnapshotsDay: ", e.getMessage());
            }
        });
    }
    @SuppressLint("SimpleDateFormat")
    public void getActivityStateListWeek(String email) {

        ArrayList<MovementRecordModel> weekActivityStateList = new ArrayList<>();

        long currentDayMilli = System.currentTimeMillis();
        long weekMilli = currentDayMilli - (7*(1000*60*60*24));

        //seven day millis
        long sevenDaysMillis = currentDayMilli - weekMilli;

        Log.i("currentHourAndlasthourVehicleWeek: ", " sevenDaysMillis " + sevenDaysMillis + " currentdayMillis " + currentDayMilli);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            Query query = FirebaseFirestore.getInstance()
                    .collection(Constants.USERS_COLLECTION)
                    .document(email)
                    .collection(Constants.ACTIVITY_COLLECTION)
                    .whereGreaterThan(Constants.ACTIVITY_TIMESTAMP, sevenDaysMillis)
                    .whereLessThan(Constants.ACTIVITY_TIMESTAMP, currentDayMilli)
                    .orderBy(Constants.ACTIVITY_TIMESTAMP, Query.Direction.ASCENDING);

            try {
                query.get()
                        .addOnSuccessListener(querySnapshots -> {

//                            Toast.makeText(App.getAppContext(), "ReadsDay: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                            Log.i("movementListWeek ", querySnapshots.getDocuments().toString());

                            for (DocumentSnapshot snapshot : querySnapshots.getDocuments()) {

                                weekActivityStateList.add(new MovementRecordModel(
                                        snapshot.getString("activity_name"),
                                        snapshot.getString("activity_duration"),
                                        snapshot.getString("activity_time"),
                                        snapshot.getLong("activity_timestamp")
                                ));

                            }

                            //logging all of the day data
                            Log.i("ActivitiesListWeek ", weekActivityStateList.toString());


                            //setting all of the day data to live data
                            movementsWeekLiveData.postValue(weekActivityStateList);
                            executor.shutdown();

                        }).addOnFailureListener(e -> {

                            Log.i("ACTIVITIESListDAYFailure: ", "" + e.getMessage());

                        });
            } catch (Exception e) {
                Log.i("querySnapshotsDay: ", e.getMessage());
            }
        });
    }
    @SuppressLint("SimpleDateFormat")
    public void getActivityStateListMonth(String email) {

        ArrayList<MovementRecordModel> monthActivityStateList = new ArrayList<>();

        long currentDayMilli = System.currentTimeMillis();
        long monthMilli = currentDayMilli - (30L *1000*60*60*24);

        //seven day millis
        long oneMonthMillis = currentDayMilli - monthMilli;

        Log.i("currentHourAndlasthourVehicleWeek: ", " sevenDaysMillis " + oneMonthMillis + " currentdayMillis " + currentDayMilli);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            Query query = FirebaseFirestore.getInstance()
                    .collection(Constants.USERS_COLLECTION)
                    .document(email)
                    .collection(Constants.ACTIVITY_COLLECTION)
                    .whereGreaterThan(Constants.ACTIVITY_TIMESTAMP, oneMonthMillis)
                    .whereLessThan(Constants.ACTIVITY_TIMESTAMP, currentDayMilli)
                    .orderBy(Constants.ACTIVITY_TIMESTAMP, Query.Direction.ASCENDING);

            try {
                query.get()
                        .addOnSuccessListener(querySnapshots -> {

//                            Toast.makeText(App.getAppContext(), "ReadsDay: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                            Log.i("MovementListMonth ", querySnapshots.getDocuments().toString());

                            for (DocumentSnapshot snapshot : querySnapshots.getDocuments()) {

                                monthActivityStateList.add(new MovementRecordModel(
                                        snapshot.getString("activity_name"),
                                        snapshot.getString("activity_duration"),
                                        snapshot.getString("activity_time"),
                                        snapshot.getLong("activity_timestamp")
                                ));

                            }

                            //logging all of the day data
                            Log.i("ActivitiesListMonth ", monthActivityStateList.toString());

                            //setting all of the day data to live data
                            movementsMonthLiveData.postValue(monthActivityStateList);
                            executor.shutdown();

                        }).addOnFailureListener(e -> {

                            Log.i("ACTIVITIESListDAYFailure: ", "" + e.getMessage());

                        });
            } catch (Exception e) {
                Log.i("querySnapshotsDay: ", e.getMessage());
            }
        });

    }
    @SuppressLint("SimpleDateFormat")
    public void getActivityStateListYear(String email) {

        ArrayList<MovementRecordModel> yearActivityStateList = new ArrayList<>();

        long currentDayMilli = System.currentTimeMillis();
        long yearMilli = currentDayMilli - (362L*1000*60*60*24);

        //seven day millis
        long oneYearMillis = currentDayMilli - yearMilli;

        Log.i("currentHourAndlasthourVehicleYear: ", " 365DaysMillis " + oneYearMillis + " currentdayMillis " + currentDayMilli);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            Query query = FirebaseFirestore.getInstance()
                    .collection(Constants.USERS_COLLECTION)
                    .document(email)
                    .collection(Constants.ACTIVITY_COLLECTION)
                    .whereGreaterThan(Constants.ACTIVITY_TIMESTAMP, oneYearMillis)
                    .whereLessThan(Constants.ACTIVITY_TIMESTAMP, currentDayMilli)
                    .orderBy(Constants.ACTIVITY_TIMESTAMP, Query.Direction.ASCENDING);

            try {
                query.get()
                        .addOnSuccessListener(querySnapshots -> {

//                            Toast.makeText(App.getAppContext(), "ReadsDay: "+ App.readCount++, Toast.LENGTH_SHORT).show();

                            Log.i("MovementListYear ", querySnapshots.getDocuments().toString());

                            for (DocumentSnapshot snapshot : querySnapshots.getDocuments()) {

                                yearActivityStateList.add(new MovementRecordModel(
                                        snapshot.getString("activity_name"),
                                        snapshot.getString("activity_duration"),
                                        snapshot.getString("activity_time"),
                                        snapshot.getLong("activity_timestamp")
                                ));

                            }

                            //logging all of the day data
                            Log.i("ActivitiesListYear ", yearActivityStateList.toString());

                            //setting all of the day data to live data
                            movementsYearLiveData.postValue(yearActivityStateList);
                            executor.shutdown();

                        }).addOnFailureListener(e -> {

                            Log.i("ACTIVITIESListDAYFailure: ", "" + e.getMessage());

                        });
            } catch (Exception e) {
                Log.i("querySnapshotsDay: ", e.getMessage());
            }
        });
    }

}