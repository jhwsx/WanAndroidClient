package com.wan.android.data;

import com.wan.android.data.db.DbHelper;
import com.wan.android.data.network.ApiHelper;
import com.wan.android.data.network.model.AccountData;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.BranchData;
import com.wan.android.data.network.model.CollectData;
import com.wan.android.data.network.model.CollectDatas;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.data.network.model.HotkeyData;
import com.wan.android.data.network.model.NavigationData;
import com.wan.android.data.network.model.ProjectData;
import com.wan.android.data.network.model.SearchHistoryData;
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
    public Observable<CommonResponse<List<HotkeyData>>> getHotkey() {
        return mApiHelper.getHotkey();
    }

    @Override
    public Observable<CommonResponse<ArticleData>> search(int page, String k) {
        return mApiHelper.search(page, k);
    }

    @Override
    public Observable<CommonResponse<List<BranchData>>> getTree() {
        return mApiHelper.getTree();
    }

    @Override
    public Observable<CommonResponse<ArticleData>> getLeafArticles(int page, int id) {
        return mApiHelper.getLeafArticles(page, id);
    }

    @Override
    public Observable<CommonResponse<List<NavigationData>>> getNavigation() {
        return mApiHelper.getNavigation();
    }

    @Override
    public Observable<CommonResponse<List<ProjectData>>> getProject() {
        return mApiHelper.getProject();
    }

    @Override
    public Observable<CommonResponse<ArticleData>> getProjectList(int page, int cid) {
        return mApiHelper.getProjectList(page, cid);
    }

    @Override
    public Observable<CommonResponse<CollectData>> getMyCollection(int page) {
        return mApiHelper.getMyCollection(page);
    }

    @Override
    public Observable<CommonResponse<String>> collectInSiteArticle(int id) {
        return mApiHelper.collectInSiteArticle(id);
    }

    @Override
    public Observable<CommonResponse<CollectDatas>> collectOutOfSiteArticle(String title,
                                                                            String author, String link) {
        return mApiHelper.collectOutOfSiteArticle(title, author, link);
    }

    @Override
    public Observable<CommonResponse<String>> uncollectArticleListArticle(int id) {
        return mApiHelper.uncollectArticleListArticle(id);
    }

    @Override
    public Observable<CommonResponse<String>> uncollectMyCollectionArticle(int id, int originId) {
        return mApiHelper.uncollectMyCollectionArticle(id, originId);
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
    public String getRoastOpenid() {
        return mPreferencesHelper.getRoastOpenid();
    }

    @Override
    public void setRoastOpenid(String openid) {
        mPreferencesHelper.setRoastOpenid(openid);
    }

    @Override
    public int getRoastHeadPicId() {
        return mPreferencesHelper.getRoastHeadPicId();
    }

    @Override
    public void setRoastHeadPicId(int headPicId) {
        mPreferencesHelper.setRoastHeadPicId(headPicId);
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

    @Override
    public Observable<Boolean> saveSearchHistory2Db(SearchHistoryData data) {
        return mDbHelper.saveSearchHistory2Db(data);
    }

    @Override
    public Observable<List<SearchHistoryData>> getDbSearchHistory() {
        return mDbHelper.getDbSearchHistory();
    }

    @Override
    public Observable<Boolean> deleteDbSearchHistory() {
        return mDbHelper.deleteDbSearchHistory();
    }
}
