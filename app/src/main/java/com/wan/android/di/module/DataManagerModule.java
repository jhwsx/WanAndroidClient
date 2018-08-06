package com.wan.android.di.module;

import com.wan.android.data.AppDataManager;
import com.wan.android.data.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author wzc
 * @date 2018/8/3
 */
@Module(includes = {ApiHelperModule.class, PreferencesHelperModule.class})
public class DataManagerModule {
    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }
}
