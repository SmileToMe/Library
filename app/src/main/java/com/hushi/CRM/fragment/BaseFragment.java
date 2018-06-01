package com.hushi.CRM.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.hushi.CRM.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BaseFragment extends Fragment {

    public BaseFragment getFragment() {
        return this;
    }


    private CompositeDisposable mDisposables;


    public CompositeDisposable getDisposables() {
        if (mDisposables == null) {
            mDisposables = new CompositeDisposable();
        }
        return mDisposables;
    }

    protected void addDisposable(Disposable disposable) {
        getDisposables().add(disposable);
    }

    protected void removeDispose(Disposable disposable) {
        if (mDisposables != null)
            mDisposables.remove(disposable);
    }

    @Override
    public void onDestroyView() {
        dismissProgressDialog();
        super.onDestroyView();
        if (mDisposables != null) {
            mDisposables.dispose();
            mDisposables = null; // 必需，Fragment 声明周期
        }
    }


    private ProgressDialog mProgressDialog;

    private Disposable mDialogDisposable;


    public void showProgressDialog() {
        if (isDetached())
            return;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
        }
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
            mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.waiting));
        mProgressDialog.show();
    }


    protected Consumer<Disposable> getSubscribeConsumer() {
        return new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                addDisposable(disposable);
            }
        };
    }


    public void dismissProgressDialog() {
        if (!isDetached() && mProgressDialog != null) {
            if (mDialogDisposable != null && !mDialogDisposable.isDisposed())
                mDialogDisposable.dispose();
            mProgressDialog.dismiss();
        }

    }

    protected boolean isProgressShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }
}
