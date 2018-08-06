package com.wan.android.di.module;

import com.wan.android.data.pref.AppPreferencesHelper;
import com.wan.android.data.pref.PreferencesHelper;
import com.wan.android.di.PrefsInfo;
import com.wan.android.util.constant.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author wzc
 * @date 2018/8/3
 */
@Module
public class PreferencesHelperModule {

    @Provides
    @PrefsInfo
    String providePrefsFileName() {
        return AppConstants.PREFS_FILE_NAME;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }
}
