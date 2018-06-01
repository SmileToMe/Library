package com.p.library.utils;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.p.library.A;


public class SPUtil {

    public static final String KEY_TOKEN = "KEY_TOKEN_N1";
    public static final String KEY_USER_INFO = "KEY_USER_INFO_N1";

    public static final String KEY_COOKIE = "KEY_COOKIE";
    public static final String KEY_REGISTRATION = "KEY_REGISTRATION";

    /**
     * 存储SharedPreference
     */
    public static boolean saveSharedPreference(String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(A.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getSharedPreference(String key) {
        String result;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(A.getInstance());
        result = preferences.getString(key, "");
        return result;
    }


    public static boolean saveSharedPreference(String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(A.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBooleanSharedPreference(String key) {
        boolean result;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(A.getInstance());
        result = preferences.getBoolean(key, false);
        return result;
    }

    public static boolean getBooleanSharedPreference(String key, boolean defaultValue) {
        boolean result;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(A.getInstance());
        result = preferences.getBoolean(key, defaultValue);
        return result;
    }

    /**
     * 清空
     */
    public static boolean clearSharedPreference(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(A.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, "");
        return editor.commit();
    }


    public static String getDecodeString(String key) {
        String result;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(A.getInstance());
        result = preferences.getString(key, "");
        if (!TextUtils.isEmpty(result)) {
            result = DesUtil.decode(result);
        }
        return result;
    }

    public static void saveEncodeString(String key, String value) {
        if (TextUtils.isEmpty(value))
            saveSharedPreference(key, "");
        else
            saveSharedPreference(key, DesUtil.encode(value));
    }


    public static void clearCache() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationD.getInstance());
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(KEY_CONTACT_MOBILE, "");
//        editor.putBoolean(KEY_DOCTOR_HOME, false);
//        editor.putBoolean(KEY_USER_HOME, false);
//        editor.putBoolean(KEY_READ_SERVICE_INTRODUCE_OR_NO, false);
//        editor.commit();
    }
}
