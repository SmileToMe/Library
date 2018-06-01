package com.p.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by JIANG on 2015/10/29.
 * ScrollView内嵌RecyclerView导致RecyclerView滑动困难问题
 * <p/>
 * 滑动到底部监听
 */
public class ScrollViewM extends ScrollView {

    private OnBorderListener onBorderListener;
    private OnScrollListener mOnScrollListener;
    private View contentView;

    private boolean isLoading = false;


    private int downY;
    private int mTouchSlop;


    public ScrollViewM(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public ScrollViewM(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public ScrollViewM(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldX, int oldY) {
        super.onScrollChanged(l, t, oldX, oldY);
        doOnBorderListener();
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(l, t, oldX, oldY);
        }
    }

    private void doOnBorderListener() {
        if (onBorderListener == null)
            return;
        if (contentView != null && contentView.getMeasuredHeight() - 20 <= getScrollY() + getHeight()) {
            if (isLoading)
                return;
            isLoading = true;
            onBorderListener.onBottom();
        } else if (getScrollY() == 0) {
            if (onBorderListener != null) {
                onBorderListener.onTop();
            }
        }
    }

    public void loadFinish() {
        isLoading = false;
    }


    public void setOnBorderListener(OnBorderListener onBorderListener) {

        this.onBorderListener = onBorderListener;
        if (contentView == null) {
            contentView = getChildAt(0);
        }
    }

    public void setOnScrollListener(OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }

    /**
     * OnBorderListener, Called when scroll to top or bottom
     */
    public interface OnBorderListener {

        /**
         * Called when scroll to bottom
         */
        void onBottom();

        /**
         * Called when scroll to top
         */
        void onTop();
    }

    public interface OnScrollListener {
        void onScroll(int l, int t, int oldX, int oldY);
    }
}
