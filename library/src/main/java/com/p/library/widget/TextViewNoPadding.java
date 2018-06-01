package com.p.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 消除textView 内边距
 * 上下仍有 1dp 的距离,布局时注意计算
 *
 * @author JH
 * @since 2018/1/11
 */
public class TextViewNoPadding extends android.support.v7.widget.AppCompatTextView {

    public TextViewNoPadding(Context context) {
        super(context);
        init();
    }

    public TextViewNoPadding(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewNoPadding(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setIncludeFontPadding(false);
    }
}

