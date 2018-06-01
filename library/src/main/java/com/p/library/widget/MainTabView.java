package com.p.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.p.library.R;
import com.p.library.widget.round.RoundTextView;

/**
 * 首页 TabView
 *
 * @author JH
 * @since 2017/7/5
 */
public class MainTabView extends FrameLayout {

    private ImageView mTabImg;
    private TextView mTabText;
    private RoundTextView mTabUnread;

    public MainTabView(@NonNull Context context) {
        this(context, null);
    }

    public MainTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_main_tab, this, false);

        addView(inflate, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mTabImg = (ImageView) inflate.findViewById(R.id.tab_img);
        mTabText = (TextView) inflate.findViewById(R.id.tab_text);
        mTabUnread = (RoundTextView) inflate.findViewById(R.id.tab_unread);
        mTabUnread.setVisibility(GONE);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainTabView);
        String title = ta.getString(R.styleable.MainTabView_tab_text);
        mTabText.setText(title);


        int resourceId = ta.getResourceId(R.styleable.MainTabView_tab_resource, -1);
        if (resourceId != -1) {
            mTabImg.setImageResource(resourceId);
        }

        ta.recycle();
    }

    public void setNumber(int number) {
        mTabUnread.setText(number > 99 ? "99+" : String.valueOf(number));
        if (number <= 0) {
            mTabUnread.setVisibility(GONE);
        } else {
            mTabUnread.setVisibility(VISIBLE);
        }
    }

//    @Override
//    public void setSelected(boolean selected) {
//        super.setSelected(selected);
//        mTabImg.setSelected(selected);
//        mTabText.setSelected(selected);
//    }
}
