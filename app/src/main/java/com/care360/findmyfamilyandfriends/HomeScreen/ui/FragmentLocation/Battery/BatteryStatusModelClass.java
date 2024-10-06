package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Battery;

public class BatteryStatusModelClass {

    private final boolean isCharging;
    private final int batteryPercentage;

    public BatteryStatusModelClass(boolean isCharging, int batteryPercentage) {
        this.isCharging = isCharging;
        this.batteryPercentage = batteryPercentage;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

}
