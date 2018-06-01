package com.p.library.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.p.library.R;
import com.p.library.widget.ImageSpanC;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by Jiang on 2016/4/21.
 * <p/>
 */
public class ViewUtil {

    public static void loadImage(ImageView imageView, Object url) {
        Glide.with(imageView.getContext()).load(url).dontAnimate()
                .placeholder(R.drawable.ic_place_holder).into(imageView);
    }

    public static void loadImage(ImageView imageView, Object url, int placeholder) {
        Glide.with(imageView.getContext()).load(url).dontAnimate().placeholder(placeholder).into(imageView);
    }


    public static void loadBlurTransformationImg(ImageView imageView, Object url) {
        Glide.with(imageView.getContext()).load(url).dontAnimate()
                .bitmapTransform(new BlurTransformation(imageView.getContext()))
                .into(imageView);
    }

    public static void loadBlurTransformationImgCenter(ImageView imageView, Object url) {
        Glide.with(imageView.getContext()).load(url).dontAnimate()
                .bitmapTransform(new BlurTransformation(imageView.getContext(), 25), new CenterCrop(imageView.getContext()))
                .into(imageView);
    }


    public static void loadHeadImg(ImageView imageView, Object url) {
        Glide.with(imageView.getContext()).load(url).dontAnimate().placeholder(R.mipmap.ic_head)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

//    public static void loadCircleImage(ImageView imageView, Object url) {
//        Glide.with(imageView.getContext()).load(url).dontAnimate().placeholder(R.drawable.ic_place_holder_round)
//                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
//                .into(imageView);
//    }

    public static void loadCircleImage(ImageView imageView, Object url, @DrawableRes int placeholder) {
        Glide.with(imageView.getContext()).load(url).dontAnimate().placeholder(placeholder)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    public static void loadRoundImage(ImageView imageView, Object url, @DrawableRes int placeholder) {
        Context context = imageView.getContext();
        int dp4 = context.getResources().getDimensionPixelOffset(R.dimen.dp4);
        Glide.with(context).load(url).dontAnimate().placeholder(placeholder).bitmapTransform(new RoundedCornersTransformation(context, dp4, dp4))
                .into(imageView);
    }


    public static void initSwipeRefresh(SwipeRefreshLayout refreshLayout,
                                        SwipeRefreshLayout.OnRefreshListener listener) {
        refreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3);
        refreshLayout.setOnRefreshListener(listener);
        int dimen = ScreenUtils.px2dip(refreshLayout.getResources().getDimension(R.dimen.dp360_0), refreshLayout.getContext());
        if (dimen >= 600) {
            refreshLayout.setSize(SwipeRefreshLayout.LARGE);
        }
    }


    public static FrameLayout getDialogLayout(View view) {
        FrameLayout layout = new FrameLayout(view.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int size = ScreenUtils.dip2px(16);
        int left = ScreenUtils.dip2px(24);
        params.setMargins(left, size, left, size / 2);
        layout.addView(view, params);
        return layout;
    }


    public static View getRecyclerTopView(Context context) {
        View view = new View(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                context.getResources().getDimensionPixelOffset(R.dimen.dp16)));
        return view;
    }


    /**
     * 搜索框 设置hint 第一个图标
     */
    public static SpannableString getSearchHintSpan(@DrawableRes int resId, String hint, Context context) {
        SpannableString spanString = new SpannableString("_IMG_  " + hint);
        ImageSpanC imageSpan = new ImageSpanC(context, resId, ImageSpan.ALIGN_BASELINE);
        spanString.setSpan(imageSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

}
