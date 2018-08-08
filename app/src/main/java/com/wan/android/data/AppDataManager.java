package com.wan.android.data;

import com.wan.android.data.db.DbHelper;
import com.wan.android.data.network.ApiHelper;
import com.wan.android.data.network.model.AccountData;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.ArticleDatas;
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
    private final DbHelper mDbHelper;
    @Inject
    public AppDataManager(ApiHelper apiHelper, DbHelper dbHeleper, PreferencesHelper preferencesHelper) {
        mApiHelper = apiHelper;
        mDbHelper = dbHeleper;
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

    @Override
    public Observable<List<ArticleDatas>> getDbHomeArticles() {
        return mDbHelper.getDbHomeArticles();
    }

    @Override
    public Observable<Boolean> saveHomeArticles2Db(List<ArticleDatas> data) {
        return mDbHelper.saveHomeArticles2Db(data);
    }

    @Override
    public Observable<List<BannerData>> getDbBanner() {
        return mDbHelper.getDbBanner();
    }

    @Override
    public Observable<Boolean> saveBanner2Db(List<BannerData> data) {
        return mDbHelper.saveBanner2Db(data);
    }
}
