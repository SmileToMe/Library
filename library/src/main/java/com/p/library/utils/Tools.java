package com.p.library.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.p.library.A;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.p.library.utils.ToastUtil.showToast;

/**
 * Created by Jiang on 2016/4/27.
 * <p/>
 */
public class Tools {

    public static void setTranslucentStatus(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }/* else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }*/
    }


    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {
//                LogUtil.d(e.toString());
            }
        }
        return result;
    }


    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(activity, colorResId));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusTextColor(Activity activity, boolean bDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }


    public static void setDialogStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void installAPK(String filePath, String webUrl) {
        try {
            if (!TextUtils.isEmpty(filePath) && filePath.length() > 4
                    && TextUtils.equals(filePath.substring(filePath.length() - 4, filePath.length()), ".apk")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(filePath))/*Uri.parse("file://" + filePath)*/, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                A.getInstance().startActivity(intent);
            }
        } catch (Exception e) {
            LogUtil.w("filePath ", e);
            try {
                ToastUtil.showToast("使用浏览器下载");
                Uri uri = Uri.parse(webUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                A.getInstance().startActivity(intent);
            } catch (Exception e2) {
                showToast("下载失败，请到应用市场下载最新版");
            }
        }
    }


    /**
     * @param context 到应用市场
     */
    public static void goMarket(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" +
                    context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtil.i("goMarket error");
            showToast("无应用市场");
        }
    }

    public static void hideInputMethod(Activity context) {
        try {
            if (context == null || context.getCurrentFocus() == null) {
                return;
            }
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideInputMethod(View view) {
        try {
            InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null)
                manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //显示虚拟键盘
    public static void showInputMethod(final View v) {
        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                try {
                    v.requestFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 50, TimeUnit.MILLISECONDS);
    }

    public static void call(Context context, String phoneNum) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断是不是手机号码
     *
     * @param phoneNum
     * @return
     */
    public static boolean isMobileNumber(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum))
            return false;
        Pattern pattern = Pattern.compile("^[1][0-9]{10}$"); //联系电话：11位纯数字，首位为1
//        Pattern pattern = Pattern.compile("^[1][3,5,7,6,8,9][0-9]{9}$");
//        Pattern pattern = Pattern.compile("^((1[0-9])|(15[^4,\\\\D])|(18[0,5-9]))\\\\d{8}$");
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
    }

    /**
     * 获取渠道名
     */
    public static String getChannelName(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null) {
                return info.metaData.getString("UMENG_CHANNEL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void copyToClipboard(String content) {
        try {
            ClipboardManager cm = (ClipboardManager) A.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("text", content));
            showToast("复制成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 震动
     */
    public static void vibrator(Context context) {
//        try {
//            //获取系统震动服务
//            Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
//            //震动30毫秒
//            vib.vibrate(30);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getSystemName() {
        String systemMsgAll = "Android|";
        try {
            systemMsgAll += Build.MODEL + ";" + Build.VERSION.RELEASE + ";" + Build.VERSION.SDK_INT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return systemMsgAll;
    }

    public static int getChannelId(Context context) {
        String channelName = getChannelName(context);
        if (TextUtils.equals(channelName, "update"))
            return 1;
        else if (TextUtils.equals(channelName, "HSXianXia"))
            return 38;
        else if (TextUtils.equals(channelName, "biaozhun"))
            return 0;
        return -1;
    }


    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

}
