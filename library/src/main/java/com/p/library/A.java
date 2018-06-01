package com.p.library;

import android.app.Application;

/**
 * 父类 application 方便library 调用
 * Created by Jiang on 2016/5/10.
 * <p>
 */
public abstract class A extends Application {

    protected static A application;

    public static A getInstance() {
        return application;
    }

    public abstract String getToken();

    public abstract void logout(boolean goLogin);

    public abstract boolean isOnline();


}
