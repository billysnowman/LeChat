package com.example.andresr.lechat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Manage user related storage such as email.
 * <p>
 * Created by Hugo Gresse on 03/12/2017.
 */

public class UserStorage {

    private static final String USER_NAME  = "user_name";
    private static final String USER_EMAIL = "user_email";

    public static void saveUserInfo(Context context, String name, String email) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(USER_NAME, name);
        editor.putString(USER_EMAIL, email);
        editor.apply();
    }

    public static boolean isUserLoggedIn(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(sharedPreferences.getString(USER_NAME, null) != null &&
                sharedPreferences.getString(USER_EMAIL, null) != null){
            return true;
        }
        return false;
    }

    @Nullable
    public static User getUserInfo(Context context) {
        if(!isUserLoggedIn(context)){
            return null;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return new User(sharedPreferences.getString(USER_NAME, ""), sharedPreferences.getString(USER_EMAIL, ""));
    }

}
