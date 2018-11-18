package com.example.nguyenantin.bususingqrcode;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USERNAME = "keyUsername";
    private static final String KEY_EMAIL = "keyEmail";
    private static final String KEY_PASSWORD = "keyPassword";
    private static final String KEY_MONEY = "keyMoney";
    private static final String KEY_USER_TYPE = "keyUserType";
    private static final String KEY_QR_CODE = "keyQRCode";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(EndUser user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putInt(KEY_MONEY, user.getMoney());
        editor.putString(KEY_USER_TYPE, user.getUsertype());
        editor.putString(KEY_QR_CODE, user.getQrcode());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public EndUser getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new EndUser(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_USER_TYPE, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_QR_CODE, null),
                sharedPreferences.getInt(KEY_MONEY, -1)

        );
    }

    //this method will logout the user
    public void logout(Context context) throws RuntimeException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}