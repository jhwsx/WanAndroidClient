package com.wan.android;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.kingja.loadsir.core.LoadSir;
import com.umeng.commonsdk.UMConfigure;
import com.wan.android.callback.CustomCallback;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.ErrorCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.callback.TimeoutCallback;
import com.wan.android.util.CrashHandler;
import com.wan.android.util.UmengUtils;

/**
 * @author wzc
 * @date 2018/2/11
 */
public class WanAndroidApplication extends Application {
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        CrashHandler.getInstance().init(this);

        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();

        UmengUtils.initUmengAnalytics(this);
        // 友盟+设置组件化的Log开关
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        // 友盟+设置日志加密
        UMConfigure.setEncryptEnabled(!BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
