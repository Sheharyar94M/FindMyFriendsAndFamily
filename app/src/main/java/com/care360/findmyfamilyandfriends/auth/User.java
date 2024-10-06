package com.care360.findmyfamilyandfriends.auth;

import androidx.annotation.NonNull;

public class User {

    private String firstName;
    private String phoneNo;
    private String email;

    public User(String firstName, String phoneNo, String email) {
        this.firstName = firstName;
        this.phoneNo = phoneNo;
        this.email = email;
    }

    public User() {
    }

    @NonNull
    @Override
    public String
    toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

}
