package com.wan.android.data.network;

import com.wan.android.data.network.api.ApiCall;
import com.wan.android.data.network.model.AccountData;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.data.network.model.HotkeyData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author wzc
 * @date 2018/8/2
 */
@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiCall mApiCall;

    @Inject
    public AppApiHelper(ApiCall apiCall) {
        mApiCall = apiCall;
    }

    @Override
    public Observable<CommonResponse<ArticleData>> getHomeList(int page) {
        return mApiCall.getHomeList(page);
    }

    @Override
    public Observable<CommonResponse<AccountData>> login(String username, String password) {
        return mApiCall.login(username, password);
    }

    @Override
    public Observable<CommonResponse<AccountData>> register(String username, String password, String repassword) {
        return mApiCall.register(username, password, repassword);
    }

    @Override
    public Observable<CommonResponse<List<BannerData>>> getBanner() {
        return mApiCall.getBanner();
    }

    @Override
    public Observable<CommonResponse<List<HotkeyData>>> getHotkey() {
        return mApiCall.getHotkey();
    }

    @Override
    public Observable<CommonResponse<ArticleData>> search(int page, String k) {
        return mApiCall.search(page, k);
    }
}
