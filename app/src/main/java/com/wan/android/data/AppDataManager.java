package com.wan.android.data;

import com.wan.android.data.network.ApiHelper;
import com.wan.android.data.network.model.AccountData;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.data.pref.PreferencesHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author wzc
 * @date 2018/8/2
 */
@Singleton
public class AppDataManager implements DataManager {
    private final ApiHelper mApiHelper;
    private final PreferencesHelper mPreferencesHelper;
    @Inject
    public AppDataManager(ApiHelper apiHelper, PreferencesHelper preferencesHelper) {
        mApiHelper = apiHelper;
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public Observable<CommonResponse<ArticleData>> getHomeList(int page) {
        return mApiHelper.getHomeList(page);
    }

    @Override
    public Observable<CommonResponse<AccountData>> login(String username, String password) {
        return mApiHelper.login(username, password);
    }

    @Override
    public Observable<CommonResponse<AccountData>> register(String username, String password, String repassword) {
        return mApiHelper.register(username, password, repassword);
    }

    @Override
    public Observable<CommonResponse<List<BannerData>>> getBanner() {
        return mApiHelper.getBanner();
    }

    @Override
    public String getUsername() {
        return mPreferencesHelper.getUsername();
    }

    @Override
    public void setUsername(String username) {
        mPreferencesHelper.setUsername(username);
    }

    @Override
    public boolean getLoginStatus() {
        return mPreferencesHelper.getLoginStatus();
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        mPreferencesHelper.setLoginStatus(isLogin);
    }
}
