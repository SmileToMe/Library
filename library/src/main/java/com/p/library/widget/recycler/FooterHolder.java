package com.p.library.widget.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.p.library.R;


/**
 * Created by Jiang on 2015/12/15.
 * <p/>
 */
public class FooterHolder {

    private ProgressBar progress;
    private TextView mHint;

    private View view;


    public FooterHolder(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_load_more, parent, false);
        progress = (ProgressBar) view.findViewById(R.id.load_more_progress);
        mHint = (TextView) view.findViewById(R.id.load_more_text);

    }

    public View getView() {
        return view;
    }


    public void setFooterNoMore() {
        progress.setVisibility(View.GONE);
        mHint.setVisibility(View.VISIBLE);
        mHint.setText(R.string.no_more_data);
    }


    public void reSetFooter() {
        progress.setVisibility(View.VISIBLE);
//        mHint.setText(R.string.loading);
        mHint.setVisibility(View.GONE);

    }


    public void setFootMessage(String msg) {
        progress.setVisibility(View.GONE);
        mHint.setVisibility(View.VISIBLE);
        mHint.setText(msg);
    }

    public void hideFooter() {
        progress.setVisibility(View.GONE);
        mHint.setVisibility(View.GONE);
    }

}
