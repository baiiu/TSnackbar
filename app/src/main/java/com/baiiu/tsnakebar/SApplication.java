package com.baiiu.tsnakebar;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

/**
 * author: baiiu
 * date: on 16/2/22 11:13
 * description:
 */
public class SApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        context = getApplicationContext();


    }
}
