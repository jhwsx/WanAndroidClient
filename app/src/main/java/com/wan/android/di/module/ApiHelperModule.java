package com.wan.android.di.module;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wan.android.data.network.ApiHelper;
import com.wan.android.data.network.AppApiHelper;
import com.wan.android.data.network.api.ApiCall;
import com.wan.android.di.ApplicationContext;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;

/**
 * @author wzc
 * @date 2018/8/3
 */
@Module
public class ApiHelperModule {
    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    ApiCall provideApiCall(Cache cache, ClearableCookieJar cookieJar) {
        return ApiCall.Factory.create(cache, cookieJar);
    }

    @Provides
    @Singleton
    Cache provideCache(@ApplicationContext Context context) {
        return new Cache(new File(context.getExternalFilesDir(null), "HttpCache"), 1024 * 1024 * 50);
    }

    @Provides
    @Singleton
    ClearableCookieJar provideCookieJar(@ApplicationContext Context context) {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }
}
