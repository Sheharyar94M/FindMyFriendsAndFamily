package com.care360.findmyfamilyandfriends.HomeScreen.receiver;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.care360.findmyfamilyandfriends.Application.App;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.model.MovementRecordModel;
import com.care360.findmyfamilyandfriends.R;
import com.care360.findmyfamilyandfriends.SharedPreference.SharedPreference;
import com.care360.findmyfamilyandfriends.StartScreen.StartScreenActivity;
import com.care360.findmyfamilyandfriends.Util.ActivityTransitionUtils;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class ActivityTransitionReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 100;
    long currentTime;
    long previousTime;
    private Context context;
    private NotificationManager nMN;
    private NotificationChannel channel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    public void onReceive(@NotNull Context context, @NotNull Intent intent) {

        this.context = context;

        Map<String, Object> activityDataMap = new HashMap<>();
//        Calendar calendar = Calendar.getInstance();

//        int currentYear = calendar.get(Calendar.YEAR);
//        int currentWeek = calendar.get(Calendar.WEEK_OF_MONTH);
//        int currentMonth = calendar.get(Calendar.MONTH);

        nMN = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("com.care360.findmyfamilyandfriends", "Activity Detected!", NotificationManager.IMPORTANCE_HIGH);
        }
        Log.i("received_intent: ", " " + intent);

        if (ActivityTransitionResult.hasResult(intent)) {

            ActivityTransitionResult results = ActivityTransitionResult.extractResult(intent);

            //info to log
            assert results != null;
            for (ActivityTransitionEvent event : results.getTransitionEvents()) {

                String info = "Transition: " + ActivityTransitionUtils.toActivityString(event.getActivityType()) +
                        " (" + ActivityTransitionUtils.toTransitionType(event.getTransitionType()) + ") " + "  " +
                        new SimpleDateFormat("HH:mm", Locale.US).format(new Date());

                Log.i("onActivityRecognized: ", "" + info);

                //starting time of activity
                currentTime = System.currentTimeMillis();
                Log.i("currentDateM: ", "\n Date: " + currentTime);

                try {

                    previousTime = SharedPreference.getCurrentTime();
                    //add to firebase

                    //set values to preferences
                    SharedPreference.setActivityName(ActivityTransitionUtils.toActivityString(event.getActivityType()));
                    SharedPreference.setCurrentTime(currentTime);

                    if (previousTime > 0) {

                        //calculate duration
                        long durationMilli = currentTime - previousTime;

                        //duration of activity
                        long duration = durationMilli / 60000;


                        Log.i("durationActivity: ", " Start: " + previousTime + " " + "End: " + currentTime);
                        Log.i("durationActivityMinutes: ", String.valueOf(duration));
                        Log.i("durationActivityMilli: ", String.valueOf(durationMilli));

                        //Prepare Data
                        MovementRecordModel movement =
                                new MovementRecordModel(
                                        ActivityTransitionUtils.toActivityString(event.getActivityType()),
                                        String.valueOf(duration),
                                        new SimpleDateFormat("HH:mm", Locale.US).format(new Date()),
                                        currentTime);

                        //hasp map for sending data
                        activityDataMap.put(Constants.ACTIVITY_NAME, movement.getMovementName());
                        activityDataMap.put(Constants.ACTIVITY_TIME, movement.getMovementTime());
                        activityDataMap.put(Constants.ACTIVITY_DURATION, movement.getMovementDuration());
                        activityDataMap.put(Constants.ACTIVITY_TIMESTAMP, movement.getMovementTimeStamp());

//                        Toast.makeText(context, "WriteActivity: "+ App.writeCount++, Toast.LENGTH_SHORT).show();

                        FirebaseFirestore.getInstance()
                                .collection(Constants.USERS_COLLECTION)
                                .document(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()))
                                .collection(Constants.ACTIVITY_COLLECTION)
                                .document(String.valueOf(currentTime))
                                .set(activityDataMap)
                                .addOnSuccessListener(unused -> {
                                    Log.i("activityUser", "successful activity update");
                                })
                                .addOnFailureListener(e -> {
                                    Log.i("activityUser", "Unsuccessful activity update");
                                });

                        //notify the activity
//                        showNotification(info);

                    } else {
                        Log.i("unableToCalculateTime: ", "First run!");
                    }

                    Log.i("previousTime: ", String.valueOf(SharedPreference.getCurrentTime()));
                    Log.i("previousActivity: ", SharedPreference.getActivityName());

                } catch (Exception e) {
                    SharedPreference.setActivityName(ActivityTransitionUtils.toActivityString(event.getActivityType()));
                    SharedPreference.setCurrentTime(currentTime);
                }

                Log.i("onReceiveActivitySavedPref: ", SharedPreference.getActivityName());
            }
        } else {
            Toast.makeText(context, "Unable to get Activities!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void showNotification(@NotNull String info) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent resultIntent = new Intent(context, StartScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            n = new Notification.Builder(context)
                    .setContentTitle("Detected activity of your circle member")
                    .setContentText(info)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setVibrate(new long[]{1000, 1000})
                    .setLights(Color.RED, 700, 500)
                    .setSound(alarmSound)
                    .setPriority(Notification.PRIORITY_HIGH)
//                    .setAutoCancel(true)
//                    .setTimeoutAfter(5000)
                    .setChannelId("com.care360.findmyfamilyandfriends")
                    .setContentIntent(pendingIntent)
                    .build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nMN.createNotificationChannel(channel);
        }
        nMN.notify(NOTIFICATION_ID, n);
    }
}
