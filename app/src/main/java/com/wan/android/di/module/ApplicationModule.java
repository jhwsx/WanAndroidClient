package com.wan.android.di.module;

import android.app.Application;
import android.content.Context;

import com.wan.android.di.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * @author wzc
 * @date 2018/8/2
 */
@Module(includes = DataManagerModule.class)
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

}
