package com.p.library.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonUtil {
    private static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }

    public static <T> T fromJson(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            LogUtil.w(type.toString(), e);
            return null;
        }
    }


    public static String toJson(Object src) {
        return gson.toJson(src);
    }

}
