package com.wan.android;

import android.app.Application;

import com.kingja.loadsir.core.LoadSir;
import com.wan.android.callback.CustomCallback;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.ErrorCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.callback.TimeoutCallback;
import com.wan.android.util.CrashHandler;

/**
 * @author wzc
 * @date 2018/2/11
 */
public class WanAndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);

        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }
}
