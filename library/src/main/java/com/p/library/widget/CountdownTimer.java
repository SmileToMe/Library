package com.p.library.widget;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.TextView;

import com.p.library.R;

/**
 * Created by ZQL on 2017/7/5.
 */

public class CountdownTimer extends CountDownTimer {

    private TextView mView;

    private String mBeginText;

    private int startTextSize = 0;

    private int endTextSize = 0;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountdownTimer(long millisInFuture, long countDownInterval, TextView mView) {
        super(millisInFuture, countDownInterval);

        mBeginText = mView.getText().toString();
        this.mView = mView;
    }

    public CountdownTimer(long millisInFuture, long countDownInterval, TextView mView,
                          String beginText, int startSize, int endSize) {
        super(millisInFuture, countDownInterval);
        mBeginText = beginText;
        startTextSize = startSize;
        endTextSize = endSize;
        this.mView = mView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        mView.setText("" + millisUntilFinished / 1000 + "秒后重新获取");
        if (startTextSize == 0) {
            mView.setTextSize(14f);
        } else {
            mView.setTextSize(TypedValue.COMPLEX_UNIT_PX, startTextSize);
        }
        mView.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.gray_b2b2b2));
//        mView.setBackground(ContextCompat.getDrawable(mView.getContext(), R.drawable.bg_gray));
        mView.setClickable(false);
    }

    @Override
    public void onFinish() {
        mView.setClickable(true);
        mView.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.orange_ff921d));
//        mView.setBackground(ContextCompat.getDrawable(mView.getContext(), R.drawable.bg_blue_shape));
        mView.setText(mBeginText);
        if (endTextSize == 0) {
            mView.setTextSize(17f);
        } else {
            mView.setTextSize(TypedValue.COMPLEX_UNIT_PX, endTextSize);
        }
    }
}