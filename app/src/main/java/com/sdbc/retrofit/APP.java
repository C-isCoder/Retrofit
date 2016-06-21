package com.sdbc.retrofit;

import android.app.Application;

/**
 * Created by iscod.
 * Time:2016/6/21-17:55.
 */
public class APP extends Application {
    public static APP instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static APP getInstance() {
        return instance;
    }

}
