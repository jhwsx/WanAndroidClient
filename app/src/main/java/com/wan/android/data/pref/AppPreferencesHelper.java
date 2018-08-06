package com.wan.android.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.wan.android.di.ApplicationContext;
import com.wan.android.di.PrefsInfo;

import javax.inject.Inject;

/**
 * @author wzc
 * @date 2018/8/3
 */
public class AppPreferencesHelper implements PreferencesHelper {
    private static final String KEY_USER_NAME = "com.wan.android.key_user_name";
    private static final String KEY_LOGIN_STATUS = "com.wan.android.key_login_status";
    private final SharedPreferences mPreferences;
    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context, @PrefsInfo String prefsFileName) {
        mPreferences = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE);
    }
    @Override
    public String getUsername() {
        return mPreferences.getString(KEY_USER_NAME,null);
    }

    @Override
    public void setUsername(String username) {
        mPreferences.edit().putString(KEY_USER_NAME, username).apply();
    }

    @Override
    public boolean getLoginStatus() {
        return mPreferences.getBoolean(KEY_LOGIN_STATUS, false);
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        mPreferences.edit().putBoolean(KEY_LOGIN_STATUS, isLogin).apply();
    }
}
