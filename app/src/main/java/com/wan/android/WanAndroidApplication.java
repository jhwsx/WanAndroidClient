package com.wan.android;

import android.app.Application;

import com.kingja.loadsir.core.LoadSir;
import com.wan.android.di.component.ApplicationComponent;
import com.wan.android.di.component.DaggerApplicationComponent;
import com.wan.android.di.module.ApplicationModule;
import com.wan.android.ui.loadcallback.EmptyCallback;
import com.wan.android.ui.loadcallback.LoadingCallback;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;

import timber.log.Timber;

/**
 * @author wzc
 * @date 2018/8/2
 */
public class WanAndroidApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

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
