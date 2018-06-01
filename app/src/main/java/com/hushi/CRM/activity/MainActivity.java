package com.hushi.CRM.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hushi.CRM.R;
import com.hushi.CRM.fragment.CustomerFragment;
import com.hushi.CRM.fragment.LogFragment;
import com.hushi.CRM.fragment.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQL on 2018/5/2.
 */

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private MViewPagerAdapter mAdapter;

    private List<Fragment> fragmentList;

    private String[] titles = {"日志", "客户", "统计"};

    private int images[] = {R.drawable.drawable_log_selector, R.drawable.drawable_customer_selector, R.drawable.drawable_statistics_selector};

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.main_viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabLayout);

        fragmentList = new ArrayList<>();
        fragmentList.add(LogFragment.newInstance());
        fragmentList.add(CustomerFragment.newInstance());
        fragmentList.add(StatisticsFragment.newInstance());

        mAdapter = new MViewPagerAdapter(getSupportFragmentManager(), getActivity());
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
            View mTabView = LayoutInflater.from(context).inflate(R.layout.item_tab, null, false);
            ImageView mTabImg = (ImageView) mTabView.findViewById(R.id.tab_img);
            TextView mTabTxt = (TextView) mTabView.findViewById(R.id.tab_txt);
            mTabImg.setImageResource(images[position]);
            mTabTxt.setText(titles[position]);
            mTabTxt.setTextColor(mTabLayout.getTabTextColors());
            return mTabView;
        }


    }


}
