package com.care360.findmyfamilyandfriends.HomeScreen.ui;

import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.care360.findmyfamilyandfriends.Application.App;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers.MemberDetail;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.JoinCircle.CircleModel;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainSharedViewModel extends ViewModel {

    private final MutableLiveData<List<CircleModel>> circleModelMutableLiveData;
    private final MutableLiveData<List<MemberDetail>> circleMembersMutableLiveData;
    private final List<MemberDetail> membersDetails;
    private List<String> circleMembers;
    private final ArrayList<CircleModel> circles;

    public MainSharedViewModel() {
        circleModelMutableLiveData = new MutableLiveData<>();
        circleMembersMutableLiveData = new MutableLiveData<>();
        membersDetails = new ArrayList<>();
        circles = new ArrayList<>();
    }

    public MutableLiveData<List<CircleModel>> getCircleModelMutableLiveData() {
        return circleModelMutableLiveData;
    }

    public MutableLiveData<List<MemberDetail>> getCircleMembersMutableLiveData() {
        return circleMembersMutableLiveData;
    }

    public void setCircleMembers(List<String> circleMembers) {
        this.circleMembers = circleMembers;
        getCircleMembers();
    }

    public void getCirclesList(){

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            //current user email
            String currentUserEmail;

            try{
                if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() != null)
                    currentUserEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
                else
                    currentUserEmail = SharedPreference.getEmailPref();

                FirebaseFirestore.getInstance().collectionGroup(Constants.CIRCLE_COLLECTION)
                        .whereArrayContains(Constants.CIRCLE_MEMBERS, currentUserEmail)
                        .addSnapshotListener((value, error) -> {

                            if (value != null){
                                //got circle list
                                circles.clear();
                                for (DocumentSnapshot doc : value) {
                                    circles.add(new CircleModel(doc.getId(), Objects.requireNonNull(doc.get(Constants.CIRCLE_ADMIN)).toString(), doc.getString(Constants.CIRCLE_NAME),
                                            (List<String>) doc.get(Constants.CIRCLE_MEMBERS), doc.getString(Constants.CIRCLE_JOIN_CODE)));
                                }
                                circleModelMutableLiveData.postValue(circles);
                                executor.shutdown();
                            }
                        });

            }catch (Exception e){
                Log.i( "getCirclesList: ","no email found! neither from preference");
            }
        });
    }

    public void getCircleMembers(){

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try{
                if(!circleMembers.isEmpty()){
                    Log.i( "getCircleMembersSize: ", String.valueOf(circleMembers.size()));

                    membersDetails.clear();
                    // getting the members
                    for (String memberEmail : circleMembers) {
                        // getting the user info
                        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                                .document(memberEmail)
                                .get()
                                .addOnSuccessListener((valueUserInfo) -> {

//                                    Toast.makeText(App.getAppContext(), "ReadsMemberDetail: " + App.readCount++, Toast.LENGTH_SHORT).show();

                                    try {

                                        if (valueUserInfo != null){
                                            MemberDetail memberDetail = new MemberDetail();

                                            memberDetail.setMemberFirstName(valueUserInfo.getString(Constants.FIRST_NAME));
                                            memberDetail.setMemberLastName(valueUserInfo.getString(Constants.LAST_NAME));
                                            memberDetail.setMemberImageUrl(valueUserInfo.getString(Constants.IMAGE_PATH));
                                            memberDetail.setMemberEmail(valueUserInfo.getString(Constants.EMAIL));

                                            if (valueUserInfo.getDouble(Constants.LAT) != null && valueUserInfo.getDouble(Constants.LNG) != null) {
                                                memberDetail.setLocationLat((Objects.requireNonNull(valueUserInfo.getDouble(Constants.LAT)).toString()));
                                                memberDetail.setLocationLng((Objects.requireNonNull(valueUserInfo.getDouble(Constants.LNG)).toString()));
                                                memberDetail.setLocationAddress(valueUserInfo.getString(Constants.LOC_ADDRESS));
                                                memberDetail.setLocationTimestamp(Long.parseLong(String.valueOf(valueUserInfo.get(Constants.LOC_TIMESTAMP))));
                                                memberDetail.setBatteryPercentage(Math.toIntExact(valueUserInfo.getLong(Constants.BATTERY_PERCENTAGE)));
                                                memberDetail.setPhoneCharging(Boolean.TRUE.equals(valueUserInfo.getBoolean(Constants.IS_PHONE_CHARGING)));
                                            }

                                            // members recyclerview
                                            membersDetails.add(memberDetail);

                                            Log.i( "setBottomSheetMembers: ", String.valueOf(membersDetails.size()));

                                            circleMembersMutableLiveData.postValue(membersDetails);
                                            executor.shutdown();
                                        }

                                    }catch (Exception e){
                                        Log.i( "getCircleMembersException: ",e.getMessage());
                                    }
                                });
                    }
                }else{

                    circleMembers = circles.get(0).getCircleMembersList();

                    SharedPreference.setCircleId(circles.get(0).getCircleId());
                    SharedPreference.setCircleName(circles.get(0).getCircleName());
                    SharedPreference.setCircleInviteCode(circles.get(0).getCircleJoinCode());
                    SharedPreference.setCircleMembersList(circles.get(0).getCircleMembersList());

                    Log.i( "getCircleMembersSize: ", String.valueOf(circleMembers.size()));

                    membersDetails.clear();
                    // getting the members
                    for (String memberEmail : circleMembers) {
                        // getting the user info
                        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                                .document(memberEmail)
                                .get()
                                .addOnSuccessListener((valueUserInfo) -> {

//                                    Toast.makeText(App.getAppContext(), "ReadsMemberDetail: " + App.readCount++, Toast.LENGTH_SHORT).show();

                                    try {

                                        if (valueUserInfo != null){
                                            MemberDetail memberDetail = new MemberDetail();

                                            memberDetail.setMemberFirstName(valueUserInfo.getString(Constants.FIRST_NAME));
                                            memberDetail.setMemberLastName(valueUserInfo.getString(Constants.LAST_NAME));
                                            memberDetail.setMemberImageUrl(valueUserInfo.getString(Constants.IMAGE_PATH));
                                            memberDetail.setMemberEmail(valueUserInfo.getString(Constants.EMAIL));

                                            if (valueUserInfo.getDouble(Constants.LAT) != null && valueUserInfo.getDouble(Constants.LNG) != null) {
                                                memberDetail.setLocationLat((Objects.requireNonNull(valueUserInfo.getDouble(Constants.LAT)).toString()));
                                                memberDetail.setLocationLng((Objects.requireNonNull(valueUserInfo.getDouble(Constants.LNG)).toString()));
                                                memberDetail.setLocationAddress(valueUserInfo.getString(Constants.LOC_ADDRESS));
                                                memberDetail.setLocationTimestamp(Long.parseLong(String.valueOf(valueUserInfo.get(Constants.LOC_TIMESTAMP))));
                                                memberDetail.setBatteryPercentage(Math.toIntExact(valueUserInfo.getLong(Constants.BATTERY_PERCENTAGE)));
                                                memberDetail.setPhoneCharging(Boolean.TRUE.equals(valueUserInfo.getBoolean(Constants.IS_PHONE_CHARGING)));
                                            }

                                            // members recyclerview
                                            membersDetails.add(memberDetail);

                                            Log.i( "setBottomSheetMembers: ", String.valueOf(membersDetails.size()));

                                            circleMembersMutableLiveData.postValue(membersDetails);
                                            executor.shutdown();

                                        }

                                    }catch (Exception e){
                                        Log.i( "getCircleMembersException: ",e.getMessage());
                                    }
                                });
                    }
                }
            }catch (Exception e){
                Log.i( "getCircleMembers: ","Unable to get Data from database -> "+e.getMessage());
            }
        });
    }

}
