package com.wan.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.kingja.loadsir.core.LoadSir;
import com.wan.android.di.component.ApplicationComponent;
import com.wan.android.di.component.DaggerApplicationComponent;
import com.wan.android.di.module.ApplicationModule;
import com.wan.android.ui.content.X5InitService;
import com.wan.android.ui.loadcallback.EmptyCallback;
import com.wan.android.ui.loadcallback.LoadingCallback;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;

import timber.log.Timber;

/**
 * @author wzc
 * @date 2018/8/2
 */
public class App extends Application {

    private ApplicationComponent mApplicationComponent;
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        Timber.plant(new Timber.DebugTree());

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        initLoaderSir();

        initX5WebView();
    }

    private void initLoaderSir() {
        LoadSir.beginBuilder()
                .addCallback(new NetworkErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }

    private void initX5WebView() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Intent intent = new Intent(this, X5InitService.class);
            startService(intent);
        } else {
            X5InitService.initX5WebView(this);
        }
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}
