package com.sdbc.retrofit;

import android.app.Application;

/**
 * Created by iscod.
 * Time:2016/6/21-17:55.
 */
public class APP extends Application {
    public static APP instance;
    public static String TOKEN = "";
    public static String MKID = "";
    private static String VillageKid = "";
    private static String commKid = "";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static APP getInstance() {
        return instance;
    }

    public static String getToken() {
        return TOKEN;
    }

    public static void setToken(String Token) {
        TOKEN = Token;
    }

    public static void setMkid(String mkid) {
        MKID = mkid;
    }

    public static String getMkid() {
        return MKID;
    }

    public static void setVillageKid(String kid) {
        VillageKid = kid;
    }

    public static String getVillageKid() {
        return VillageKid;
    }

    public static void setCommKid(String kid) {
        commKid = kid;
    }

    public static String getCommKid() {
        return commKid;
    }
}
