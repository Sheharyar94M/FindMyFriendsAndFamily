package com.care360.findmyfamilyandfriends.Util;

import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActivityTransitionUtils {

    private static List<ActivityTransition> getTransitions() {

        List<ActivityTransition> transitions = new ArrayList<>();

        //vehicle
        ActivityTransition VehicleEnter = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build();

        transitions.add(VehicleEnter);

        ActivityTransition VehicleExit = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build();

        transitions.add(VehicleExit);

        //walking
        ActivityTransition WalkingEnter = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build();

        transitions.add(WalkingEnter);

        ActivityTransition WalkingExit = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build();

        transitions.add(WalkingExit);

        //running
        ActivityTransition RunningEnter = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build();

        transitions.add(RunningEnter);

        ActivityTransition RunningExit = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build();

        transitions.add(RunningExit);

        //still
        ActivityTransition StillEnter = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build();

        transitions.add(StillEnter);

        ActivityTransition StillExit = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build();

        transitions.add(StillExit);

        //On Bicycle
        ActivityTransition BicycleEnter = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build();

        transitions.add(BicycleEnter);

        ActivityTransition BicycleExit = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build();

        transitions.add(BicycleExit);

        //On Foot
        ActivityTransition FootEnter = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build();

        transitions.add(FootEnter);

        ActivityTransition FootExit = new ActivityTransition
                .Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build();

        transitions.add(FootExit);

        return transitions;
    }

    public static ActivityTransitionRequest getActivityTransitionRequest() {
        return new ActivityTransitionRequest(getTransitions());
    }

    @NotNull
    public static String toActivityString(int activity) {
        String activityType;

        switch (activity) {
            case DetectedActivity.IN_VEHICLE:
                activityType = "IN VEHICLE";
                break;
            case DetectedActivity.ON_BICYCLE:
                activityType = "ON BICYCLE";
                break;
            case DetectedActivity.WALKING:
                activityType = "WALKING";
                break;
            case DetectedActivity.RUNNING:
                activityType = "RUNNING";
                break;
            case DetectedActivity.STILL:
                activityType = "STILL";
                break;
            case DetectedActivity.ON_FOOT:
                activityType = "ON FOOT";
                break;

            default:
                activityType = "UNKNOWN";
        }

        return activityType;
    }

    @NotNull
    public static String toTransitionType(int transitionType) {

        String activityState;

        switch (transitionType) {
            case ActivityTransition.ACTIVITY_TRANSITION_ENTER:
                activityState = "ENTER";
                break;
            case ActivityTransition.ACTIVITY_TRANSITION_EXIT:
                activityState = "EXIT";
                break;

            default:
                activityState = "UNKNOWN";
        }
        return activityState;
    }
}
