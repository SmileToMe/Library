package com.p.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

/**
 * 权限申请通用类
 *
 * @author JH
 * @since 2017/7/4
 */
public class PermissionUtil {

    /**
     * 无权限时去申请，有权限时返回 true
     */
    public static boolean requestPermission(Activity activity, String... permissions) {
        boolean hasPermission = hasPermission(permissions, activity);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(activity, permissions,
                    Constant.REQUEST_PERMISSION);
        }
        return hasPermission;
    }

    /**
     * 无权限时去申请，有权限时返回 true
     */
    public static boolean requestPermission(Fragment fragment, String... permissions) {
        boolean hasPermission = hasPermission(permissions, fragment.getContext());
        if (!hasPermission) {
            fragment.requestPermissions(permissions, Constant.REQUEST_PERMISSION);
        }
        return hasPermission;
    }

    private static boolean hasPermission(String[] permissions, Context context) {
        boolean hasPermission = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
            }
        }
        return hasPermission;
    }
}
