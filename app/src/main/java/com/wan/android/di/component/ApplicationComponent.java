package com.wan.android.di.component;

import android.app.Application;
import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.wan.android.App;
import com.wan.android.data.DataManager;
import com.wan.android.di.ApplicationContext;
import com.wan.android.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author wzc
 * @date 2018/8/2
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(App app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();

    ClearableCookieJar getClearableCookieJar();
}
