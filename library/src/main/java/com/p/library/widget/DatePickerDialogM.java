package com.p.library.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

/**
 * 时间选择
 * Created by ZQL on 2017/7/30.
 */
public class DatePickerDialogM extends DatePickerDialog {
    public DatePickerDialogM(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme, listener, year, monthOfYear, dayOfMonth);
    }

    public DatePickerDialogM(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    protected void onStop() {
//        super.onStop();
    }

    @Override
    public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
//        super.onDateChanged(view, year, month, dayOfMonth);
    }
}
