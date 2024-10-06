package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers;

public class LocationInfo {

    private final String lat;
    private final String lng;
    private final String locAddress;
    private final long locTimestamp;

    public LocationInfo(String lat, String lng, String locAddress, long locTimestamp) {
        this.lat = lat;
        this.lng = lng;
        this.locAddress = locAddress;
        this.locTimestamp = locTimestamp;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getLocAddress() {
        return locAddress;
    }

    public long getLocTimestamp() {
        return locTimestamp;
    }
}
