package com.hushi.CRM.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hushi.CRM.R;
import com.p.library.utils.AppManager;
import com.p.library.utils.Tools;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZQL on 2018/5/3.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private CompositeDisposable mDisposables;


    protected BaseActivity getActivity() {
        return this;
    }


    protected void clickBack() {
        onBackPressed();

    }


    public void addDisposable(Disposable disposable) {
        getDisposables().add(disposable);
    }

    public CompositeDisposable getDisposables() {
        if (mDisposables == null) {
            mDisposables = new CompositeDisposable();
        }
        return mDisposables;
    }

    /**
     * 移出 RxJava请求
     */
    protected void removeDisposable() {
        if (mDisposables != null) {
            mDisposables.dispose();
            mDisposables = null;
        }
    }

    /**
     * 隐藏  title
     */

    @Override
    public void onBackPressed() {
        Tools.hideInputMethod(getActivity());
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
    }


    @Override
    protected void onDestroy() {
        removeDisposable();
        dismissProgressDialog();
        AppManager.getInstance().finishActivity(this);
        super.onDestroy();
    }


    protected ProgressDialog mProgressDialog;

    private Disposable mDialogDisposable;


    public void showProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.waiting));
        if (mProgressDialog.isShowing())
            return;
        if (mDialogDisposable != null && !mDialogDisposable.isDisposed())
            mDialogDisposable.dispose();
        mDialogDisposable = Schedulers.io().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                mDialogDisposable = AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null)
                            mProgressDialog.show();
                    }
                });
            }
        }, 400, TimeUnit.MILLISECONDS);
    }


    public void showProgressNow() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.waiting));
        mProgressDialog.show();
    }


    public void dismissProgressDialog() {
        if (mDialogDisposable != null && !mDialogDisposable.isDisposed())
            mDialogDisposable.dispose();
        if (!isFinishing() && mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected Consumer<Disposable> getSubscribeConsumer() {
        return new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                addDisposable(disposable);
            }
        };
    }

}

