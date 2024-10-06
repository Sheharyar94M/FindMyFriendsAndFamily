package com.care360.findmyfamilyandfriends.SharedPreference;

import static com.care360.findmyfamilyandfriends.Util.Constants.EMAIL;
import static com.care360.findmyfamilyandfriends.Util.Constants.FIRST_NAME;
import static com.care360.findmyfamilyandfriends.Util.Constants.LAST_NAME;
import static com.care360.findmyfamilyandfriends.Util.Constants.NULL;
import static com.care360.findmyfamilyandfriends.Util.Constants.PASSWORD;
import static com.care360.findmyfamilyandfriends.Util.Constants.PHONE_NO;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.care360.findmyfamilyandfriends.Application.App;
import com.care360.findmyfamilyandfriends.Util.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPreference {

    private static final SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());

    public static String getPhoneNoPref(){
        return sharedPreference.getString(PHONE_NO,NULL);
    }

    public static void setPhoneNoPref(String phoneNo) {
        sharedPreference.edit().putString(Constants.PHONE_NO, phoneNo).apply();
    }

    public static String getFirstNamePref(){
        return sharedPreference.getString(FIRST_NAME,NULL);
    }

    public static void setFirstNamePref(String firstName){
        sharedPreference.edit().putString(Constants.FIRST_NAME,firstName).apply();
    }

    public static String getLastNamePref(){
        return sharedPreference.getString(LAST_NAME,NULL);
    }

    public static void setLastNamePref(String lastName){
        sharedPreference.edit().putString(Constants.LAST_NAME,lastName).apply();
    }

    public static String getEmailPref(){
        return sharedPreference.getString(EMAIL,NULL);
    }

    public static void setEmailPref(String email){
        sharedPreference.edit().putString(Constants.EMAIL,email).apply();
    }

    public static String getPasswordPref(){
        return sharedPreference.getString(PASSWORD,NULL);
    }

    public static void setPasswordPref(String password){
        sharedPreference.edit().putString(Constants.PASSWORD,password).apply();
    }

    public static String getCircleName(){
        return sharedPreference.getString(Constants.CIRCLE_NAME, Constants.NULL);
    }

    public static void setCircleName(String circleName){
        sharedPreference.edit().putString(Constants.CIRCLE_NAME,circleName).apply();
    }

    public static String getCircleInviteCode(){
        return sharedPreference.getString(Constants.CIRCLE_JOIN_CODE, Constants.NULL);
    }

    public static void setCircleInviteCode(String circleCode){
        sharedPreference.edit().putString(Constants.CIRCLE_JOIN_CODE,circleCode).apply();
    }

    public static int getMapType() {
        return sharedPreference.getInt(Constants.MAP_TYPE,0);
    }

    public static void setMapType(int type) {
        sharedPreference.edit().putInt(Constants.MAP_TYPE,type).apply();
    }

    public static boolean getEmergencyContactsStatus() {
        return sharedPreference.getBoolean(Constants.ARE_EMERG_CONTACTS_ADDED,false);
    }

    public static void setEmergencyContactsStatus(boolean isContactAdded) {
        sharedPreference.edit().putBoolean(Constants.ARE_EMERG_CONTACTS_ADDED,isContactAdded).apply();
    }

    public static String getCircleId() {
        return sharedPreference.getString(Constants.CIRCLE_ID, NULL);
    }

    public static void setCircleId(String circleId) {
        sharedPreference.edit().putString(Constants.CIRCLE_ID,circleId).apply();
    }

    public static String getCircleAdminId() {
        return sharedPreference.getString(Constants.CIRCLE_ADMIN, NULL);
    }

    public static void setCircleAdminId(String circleAdminId) {
        sharedPreference.edit().putString(Constants.CIRCLE_ADMIN,circleAdminId).apply();
    }

    public static String getFullName() {
        return sharedPreference.getString(Constants.FULL_NAME, NULL);
    }

    public static void setFullName(String fullName) {
        sharedPreference.edit().putString(Constants.FULL_NAME,fullName).apply();
    }

    public static int getPremiumState(){
        return sharedPreference.getInt("premium",0);
    }

    public static void setPremiumState(int premiumState){
        sharedPreference.edit().putInt("premium",premiumState).apply();
    }

    public static long getCurrentTime(){
        return sharedPreference.getLong("currentTime",0);
    }

    @SuppressLint("CommitPrefEdits")
    public static void setCurrentTime(Long currentTime){
        sharedPreference.edit().putLong("currentTime",currentTime).apply();
    }

    public static String getActivityName(){
        return sharedPreference.getString("activity", NULL);
    }

    @SuppressLint("CommitPrefEdits")
    public static void setActivityName(String activity){
        sharedPreference.edit().putString("activity",activity).apply();
    }

    public static List<String> getCircleMembersList(){

        String Circlemembers = sharedPreference.getString("circle_members"," ");

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> circleMembersList = gson.fromJson(Circlemembers, type);

        return circleMembersList;
    }

    public static void setCircleMembersList(List<String> circleMembersList) {

        Gson gsonCircleMembers = new Gson();
        String list = gsonCircleMembers.toJson(circleMembersList);
        sharedPreference.edit().putString("circle_members", list).apply();
    }

    public static String getLocationFetched() {
        return sharedPreference.getString("address", NULL);
    }

    public static void setLocationFetched(String address) {
        sharedPreference.edit().putString("address",address).apply();
    }

    public static void saveStringValue(String name, String value) {
        sharedPreference.edit().putString(name,value).apply();
    }

    public static void setFirstRun(boolean firstRun){
        sharedPreference.edit().putBoolean("first_run",firstRun).apply();
    }

    public static boolean getFirstRun(){
       return sharedPreference.getBoolean("first_run",true);
    }

    public static void setdoesPhoneNoAlreadyExist(boolean value){
        sharedPreference.edit().putBoolean("doesPhoneNoAlreadyExist",value).apply();
    }

    public static boolean doesPhoneNoAlreadyExist(){
        return sharedPreference.getBoolean("doesPhoneNoAlreadyExist",false);
    }

    public static void setPhoneCreds(String creds) {
        sharedPreference.edit().putString("phoneCreds",creds).apply();
    }

    public static String getPhoneCreds(){
        return sharedPreference.getString("phoneCreds", NULL);
    }
}
