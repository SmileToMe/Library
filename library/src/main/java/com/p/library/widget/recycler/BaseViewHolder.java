package com.p.library.widget.recycler;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.p.library.utils.ViewUtil;


/**
 * Created by Jiang on 2016/4/21.
 * <p/>
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public TextView setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return textView;
    }

    public TextView setText(int viewId, @StringRes int textId) {
        TextView textView = getView(viewId);
        textView.setText(textId);
        return textView;
    }

    public ImageView loadImg(int viewId, Object url) {
        ImageView imgView = getView(viewId);
        ViewUtil.loadImage(imgView, url);
        return imgView;
    }

    public ImageView loadHeadImg(int viewId, Object url) {
        ImageView imgView = getView(viewId);
        ViewUtil.loadHeadImg(imgView, url);
        return imgView;
    }

    public View setViewVisible(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return view;
    }
}
