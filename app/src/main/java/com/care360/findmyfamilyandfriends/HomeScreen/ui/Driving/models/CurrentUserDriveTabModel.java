package com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class CurrentUserDriveTabModel implements Serializable {

    private String name;
    private String image;

    public CurrentUserDriveTabModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return "CurrentUserDriveTabModel{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
