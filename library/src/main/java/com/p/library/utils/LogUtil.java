package com.p.library.utils;

import android.util.Log;

/**
 * Created by Jiang on 2016/4/20.
 * <p>
 */
public class LogUtil {

    public static boolean openLog = true;


    public static final String TAG = "Video_";

    private LogUtil() {
    }


    private static String generateTag() {
        String tag = "%s.%s(L:%d)";
        try {
            StackTraceElement caller = new Throwable().getStackTrace()[2];
            String callerClazzName = caller.getClassName();
            callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
            tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
            tag = TAG + ":" + tag;
        } catch (Exception e) {
            LogUtil.w("generateTag", e);
        }
        return tag;
    }

    public static void d(String content) {
        if (!openLog) return;
        String tag = generateTag();

        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        if (!openLog) return;
        String tag = generateTag();

        Log.d(tag, content, tr);
    }

    public static void w(String content) {
        if (!openLog) return;
        String tag = generateTag();

        Log.w(tag, content);
    }

    public static void w(String content, Throwable tr) {
        if (!openLog) return;
        String tag = generateTag();

        Log.w(tag, content, tr);
    }

    public static void i(String content) {
        if (!openLog) return;
        String tag = generateTag();

        Log.i(tag, content);
    }


}
