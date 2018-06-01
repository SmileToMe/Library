package com.hushi.CRM.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hushi.CRM.R;
import com.p.library.utils.LogUtil;
import com.p.library.utils.ToastUtil;
import com.p.library.widget.round.RoundTextView;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import hushi.com.ushare.ShareUtil;

/**
 * Created by ZQL on 2018/5/3.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 微信登录
     */
    private RoundTextView mLoginBtn;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }


    private void initView() {
        mLoginBtn = (RoundTextView) findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.login_btn:
                weChatLogin();

                break;
        }
    }


    private void weChatLogin() {
        Config.DEBUG = true;
        UMShareAPI.get(getActivity()).deleteOauth(getActivity(), SHARE_MEDIA.WEIXIN, null);
        PlatformConfig.setWeixin(ShareUtil.WX_KEY, ShareUtil.WX_SECRET);
        UMShareAPI.get(getActivity()).doOauthVerify(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, final Map<String, String> map) {
                String openId = map.get("openid");
                String unionid = map.get("unionid");
                String access_token = map.get("access_token");
                LogUtil.i("wxLogin: " + map);
//                wxLogin(unionid, openId, access_token);
                MainActivity.startActivity(getActivity());
                finish();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                ToastUtil.showToast(throwable.getMessage());
                finish();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                ToastUtil.showToast("用户取消");
                finish();
            }
        });
    }
}
