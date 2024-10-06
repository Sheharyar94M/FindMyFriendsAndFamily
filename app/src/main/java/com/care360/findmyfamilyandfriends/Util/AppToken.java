package com.care360.findmyfamilyandfriends.Util;

public class AppToken {

    private String token;
    private long value;

    public AppToken(String token, Long value) {
        this.token = token;
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
