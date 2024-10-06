package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Settings.CircleManagement.RemoveMember;

public class MemberModel {

    private final String memberName;
    private final String memberEmail;

    public MemberModel(String memberName, String memberEmail) {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

}
