package com.rightpoint;

import android.app.Application;
import android.content.Context;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/6 2:58 PM 
 */
public class OKApplication extends Application {

    private static Application instance;

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
