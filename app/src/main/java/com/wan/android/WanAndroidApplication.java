package com.wan.android;

import android.app.Application;

import com.wan.android.di.component.ApplicationComponent;
import com.wan.android.di.component.DaggerApplicationComponent;
import com.wan.android.di.module.ApplicationModule;

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
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}
