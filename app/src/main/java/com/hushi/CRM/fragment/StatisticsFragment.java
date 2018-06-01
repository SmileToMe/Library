package com.hushi.CRM.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hushi.CRM.R;
import com.p.library.widget.BaseTitle;

public class StatisticsFragment extends BaseFragment {


    private View view;
    private BaseTitle mStatisticsBaseTitle;

    public static StatisticsFragment newInstance() {
        Bundle args = new Bundle();
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_statistics, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        mStatisticsBaseTitle = (BaseTitle) inflate.findViewById(R.id.statisticsBaseTitle);
        mStatisticsBaseTitle.disableClickBack();
    }
}
