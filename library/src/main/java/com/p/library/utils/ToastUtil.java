package com.p.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.p.library.A;
import com.p.library.R;
import com.p.library.widget.AlertDialogBuilder;


/**
 * Created by Jiang on 2016/4/20.
 * <p>
 */
public class ToastUtil {

    public static void showDialog(Activity activity, String message) {
        if (activity == null || activity.isFinishing())
            return;
        new AlertDialogBuilder(activity).setMessage(message).setPositiveButton(R.string.ok, null).show();
    }

    public static void showDialog(Context activity, String message, boolean showCancel, DialogInterface.OnClickListener listener) {
        if (activity == null || activity.isRestricted())
            return;
        if (showCancel)
            new AlertDialogBuilder(activity).setMessage(message).
                    setPositiveButton(R.string.ok, listener).setNegativeButton(R.string.cancel, null)
                    .show();
        else
            new AlertDialogBuilder(activity).setMessage(message).
                    setPositiveButton(R.string.ok, listener).show();
    }

    public static void showDialogC(Context context, String message, DialogInterface.OnClickListener listener) {
        new AlertDialogBuilder(context).setMessage(message).
                setPositiveButton(R.string.ok, listener).setNegativeButton(R.string.cancel, null)
                .show();
    }

    public static void showDialog2(Activity activity, String message,
                                   boolean showCancel, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener negativeListener) {
        if (activity == null || activity.isFinishing())
            return;
        if (showCancel)
            new AlertDialogBuilder(activity).setMessage(message).
                    setPositiveButton(R.string.ok, listener).setNegativeButton(R.string.cancel, negativeListener).show();
        else
            new AlertDialogBuilder(activity).setMessage(message).
                    setPositiveButton(R.string.ok, listener).show();
    }

    public static void showDialogN(Activity activity, String message, boolean showCancel, DialogInterface.OnClickListener listener,
                                   DialogInterface.OnClickListener negativeListener, boolean outsideCancel) {
        if (activity == null || activity.isFinishing())
            return;
        if (showCancel)
            new AlertDialogBuilder(activity).setMessage(message).
                    setPositiveButton(R.string.ok, listener).setNegativeButton(R.string.cancel, negativeListener).setCancelable(outsideCancel).show();
        else
            new AlertDialogBuilder(activity).setMessage(message).
                    setPositiveButton(R.string.ok, listener).setCancelable(outsideCancel).show();
    }

    private static Toast toast;

    public static void showToast(CharSequence string) {
        if (TextUtils.isEmpty(string))
            return;
        if (toast == null || toast.getDuration() == Toast.LENGTH_LONG) {
            toast = Toast.makeText(A.getInstance(), string, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(string);
        toast.show();
    }

    public static void showToastLong(CharSequence string) {
        if (TextUtils.isEmpty(string))
            return;
        if (toast == null || toast.getDuration() == Toast.LENGTH_SHORT) {
            toast = Toast.makeText(A.getInstance(), string, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(string);
        toast.show();
    }

    public static void setDialogWidth(AlertDialog dialog, int width) {

        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        params.width = width;
//        params.height = 200 ;
        dialog.getWindow().setAttributes(params);
    }


}

