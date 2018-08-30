package com.wan.android;

import android.app.Application;
import android.content.Context;

import com.kingja.loadsir.core.LoadSir;
import com.wan.android.di.component.ApplicationComponent;
import com.wan.android.di.component.DaggerApplicationComponent;
import com.wan.android.di.module.ApplicationModule;
import com.wan.android.ui.loadcallback.EmptyCallback;
import com.wan.android.ui.loadcallback.LoadingCallback;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;
import com.wan.android.util.MyDebugTree;

import timber.log.Timber;

/**
 * @author wzc
 * @date 2018/8/2
 */
public class App extends Application {

    private ApplicationComponent mApplicationComponent;
    private static Context sContext;
    private static boolean sIsColdStart = false;

    public static Context getContext() {
        return sContext;
    }

    public static boolean isColdStart() {
        return sIsColdStart;
    }

    public static void setColdStart(boolean isColdStart) {
        sIsColdStart = isColdStart;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setColdStart(true);
        sContext = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new MyDebugTree());
        }

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        initLoaderSir();

    }


    private void initLoaderSir() {
        LoadSir.beginBuilder()
                .addCallback(new NetworkErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}
