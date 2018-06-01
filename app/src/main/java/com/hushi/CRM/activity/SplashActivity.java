package com.hushi.CRM.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hushi.CRM.R;

/**
 * Created by ZQL on 2018/5/3.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity.startActivity(getActivity());
                finish();
            }
        }, 3 * 1000);
    }
}
