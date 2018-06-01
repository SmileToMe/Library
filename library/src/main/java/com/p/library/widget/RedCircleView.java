package com.p.library.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;

import com.p.library.R;
import com.p.library.widget.round.RoundTextView;
import com.p.library.widget.round.RoundViewDelegate;


public class RedCircleView extends RoundTextView {


    private int number;

    public RedCircleView(Context context) {
        this(context, null);
    }

    public RedCircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public RedCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setVisibility(INVISIBLE);
        RoundViewDelegate delegate = getDelegate();
        delegate.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        delegate.setIsRadiusHalfHeight(true);
        setGravity(Gravity.CENTER);
    }


    /**
     * 红点内数字
     */
    public void setNumber(int number) {
        this.number = number;
        setText(number > 99 ? "99+" : String.valueOf(number));
        if (number <= 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
//        LogUtil.d("RedCircleView number " + number);
    }

    public int getNumber() {
        return number;
    }


}
