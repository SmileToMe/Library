package com.p.library.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.p.library.A;


/**
 * Created by Jiang on 2015/12/4.
 * <p>
 */
public class NetUtils {
    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * @return true 网络已连接 false 网络未连接
     */
    public static boolean checkNetConnect() {
        if (A.getInstance() != null) {
            ConnectivityManager var1 = (ConnectivityManager) A.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo var2 = var1.getActiveNetworkInfo();
            return var2 != null && var2.isAvailable();
        } else {
            return false;
        }
    }

    public static boolean checkNetConnect(Context context) {
        ConnectivityManager var1 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo var2 = var1.getActiveNetworkInfo();
        return var2 != null && var2.isAvailable();
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * @return 1 wifi  4 4g
     */
    public static boolean isWifiOr4g() {
        ConnectivityManager cm = (ConnectivityManager) A.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            LogUtil.d("isWifiConnected");
            return true;
        }
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int sub = info.getSubtype();
            switch (sub) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA://电信的2G
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    //以上的都是2G网络
                    return false;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    //以上的都是3G网络
                    return false;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    LogUtil.d("4g");
                    return true;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }
}
