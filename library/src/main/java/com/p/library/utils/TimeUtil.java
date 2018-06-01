package com.p.library.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 时间转换工具类
 *
 * @author JH
 * @since 1.0.0  2017/1/9
 */
public class TimeUtil {


    private static final long ONE_DAY = 1000 * 60 * 60 * 24;
    private static final long ONE_HOUR = 1000 * 60 * 60;
    private static final long ONE_MINUTE = 1000 * 60;

    private TimeUtil() {
    }


    public static SimpleDateFormat getSimpleFormat(String type) {
        return new SimpleDateFormat(type, Locale.CHINA);
    }

    /**
     * 时间显示规则：
     * 1小时以内 ：**分钟前
     * 1-24小时：*小时前
     * 24-72小时：*天前  (1/2) 1：24<X<48，2：48≤X<72
     * 超过72小时：2017-12-30(显示日期)
     */
    public static String fromToday(long currentTime) {
        long now = Calendar.getInstance().getTimeInMillis();
        long ago = now - currentTime;
        if (ago < ONE_HOUR) {
            long minute = (ago / ONE_MINUTE);
            if (minute <= 0) {
                minute = 1;
            }
            return minute + "分钟前";
        } else if (ago >= ONE_HOUR && ago < 24 * ONE_HOUR) {
            return ((long) ago / ONE_HOUR) + "小时前";
        } else if (ago >= 24 * ONE_HOUR && ago < 72 * ONE_HOUR) {
            return (ago / ONE_DAY) + "天前";
        }
        return getSimpleFormat("yyyy-MM-dd").format(currentTime);
    }


    /**
     * 秒 转 时分秒
     */

    public static String second2Time(long second) {

        int hour = (int) (second / 3600);
        int mm = (int) ((second - hour * 3600) / 60);
        int ss = (int) ((second - hour * 3600) % 60);

        return (hour > 0 ? hour + ":" : "") + (mm > 9 ? mm : "0" + mm) + ":" + (ss > 9 ? ss : "0" + ss);
    }

    /**
     * 毫秒 转 时分秒
     */
    public static String millisecond2Time(long millisecond) {
        long second = (long) Math.ceil(((double) millisecond) / 1000);
        return second2Time(second);
    }

    /**
     * @param millisecond 时间毫秒
     * @return 不是同一年返回年年年年-月月-日日 时时-分分 格式的时间  同一年返回月月-日日 时时-分分 格式的时间
     */
    public static String millisecond2Data(long millisecond) {
        if (millisecond <= 0) {
            return "";
        }
        String nowTime = TimeUtil.getSimpleFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis());
        String str = TimeUtil.getSimpleFormat("yyyy-MM-dd HH:mm").format(millisecond);
        String strTemp = str.substring(0, 4);
        String strTempNow = nowTime.substring(0, 4);
        if (TextUtils.equals(strTemp, strTempNow)) {
            return TimeUtil.getSimpleFormat("MM-dd HH:mm").format(millisecond);
        } else {
            return str;
        }
    }

}
