package com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.models;

import androidx.annotation.NonNull;

import com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.BottomSheetMembers.MemberDetail;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

public class TabsData implements Serializable {

    private final TabLayout.Tab tab;
    private MemberDetail member;
    private boolean isSelected;

    public TabsData(TabLayout.Tab tab, MemberDetail member, boolean isSelected) {
        this.tab = tab;
        this.member = member;
        this.isSelected = isSelected;
    }

    @NonNull
    @Override
    public String toString() {
        return "TabsData{" +
                "tab=" + tab +
                ", member=" + member +
                ", isSelected=" + isSelected +
                '}';
    }

    public TabLayout.Tab getTab() {
        return tab;
    }

    public MemberDetail getMember() {
        return member;
    }

    public void setMember(MemberDetail member) {
        this.member = member;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
