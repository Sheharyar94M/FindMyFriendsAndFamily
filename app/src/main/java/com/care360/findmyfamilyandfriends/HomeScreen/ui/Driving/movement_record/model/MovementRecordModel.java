package com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MovementRecordModel implements Serializable {

    private String movementName;
    private String movementDuration;
    private String MovementTime;
    private long MovementTimeStamp;

    public MovementRecordModel(String movementName, String movementDuration, String MovementTime, long MovementTimeStamp) {
        this.movementName = movementName;
        this.movementDuration = movementDuration;
        this.MovementTime = MovementTime;
        this.MovementTimeStamp = MovementTimeStamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovementRecordModel{" +
                "movementName='" + movementName + '\'' +
                ", movementDuration='" + movementDuration + '\'' +
                ", MovementTime='" + MovementTime + '\'' +
                ", MovementTimeStamp='" + MovementTimeStamp + '\'' +
                '}';
    }

    public String getMovementName() {
        return movementName;
    }

    public void setMovementName(String movementName) {
        this.movementName = movementName;
    }

    public String getMovementDuration() {
        return movementDuration;
    }

    public void setMovementDuration(String movementDuration) {
        this.movementDuration = movementDuration;
    }

    public String getMovementTime() {
        return MovementTime;
    }

    public void setMovementTime(String MovementTime) {
        this.MovementTime = MovementTime;
    }

    public long getMovementTimeStamp() {
        return MovementTimeStamp;
    }

    public void setMovementTimeStamp(long MovementTimeStamp) {
        this.MovementTimeStamp = MovementTimeStamp;
    }

}
