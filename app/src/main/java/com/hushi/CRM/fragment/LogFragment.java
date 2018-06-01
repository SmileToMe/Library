package com.hushi.CRM.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hushi.CRM.R;
import com.p.library.utils.ToastUtil;
import com.p.library.widget.BaseTitle;

import java.util.ArrayList;
import java.util.List;

public class LogFragment extends BaseFragment {

    private View view;
    private BaseTitle mBaseTitle;

    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    private MViewPagerAdapter mAdapter;

    private String[] logTitles = {"我发出的", "我收到的"};

    private List<Fragment> fragmentList;

    public static LogFragment newInstance() {
        LogFragment fragment = new LogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_log, container, false);
        initView(mView);
        return mView;
    }


    private void initView(View mView) {
        mBaseTitle = (BaseTitle) mView.findViewById(R.id.logBaseTitle);
        mTabLayout = (TabLayout) mView.findViewById(R.id.logTabLayout);
        mViewPager = (ViewPager) mView.findViewById(R.id.logViewPager);

        mBaseTitle.disableClickBack();
        mBaseTitle.setEndClickListener(new BaseTitle.OnEndClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("新增");
            }
        });

        fragmentList = new ArrayList<>();
        fragmentList.add(SentFragment.newInstance());
        fragmentList.add(ReceivedFragment.newInstance());

        mAdapter = new MViewPagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(0);

        for (int i = 0; i < fragmentList.size(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(mAdapter.getTabView(i));
        }
    }


    private class MViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        public MViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        private View getTabView(int position) {
            View mTabView = LayoutInflater.from(context).inflate(R.layout.item_tab_log, null, false);
            TextView mTabTxt = (TextView) mTabView.findViewById(R.id.logTab_txt);
            mTabTxt.setText(logTitles[position]);
            mTabTxt.setTextColor(mTabLayout.getTabTextColors());
            return mTabView;
        }


    }


}