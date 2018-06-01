package com.hushi.CRM;

import com.p.library.A;

public class ApplicationM extends A {


    public static ApplicationM getInstance() {
        return (ApplicationM) application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public void logout(boolean goLogin) {

    }

    @Override
    public boolean isOnline() {
        return false;
    }
}
