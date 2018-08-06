package com.wan.android.data.network;

import com.wan.android.data.network.api.ApiCall;
import com.wan.android.data.network.model.AccountData;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.CommonResponse;

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
        return mApiCall.getHomeFeed(page);
    }

    @Override
    public Observable<CommonResponse<AccountData>> login(String username, String password) {
        return mApiCall.login(username, password);
    }

    @Override
    public Observable<CommonResponse<AccountData>> register(String username, String password, String repassword) {
        return mApiCall.register(username, password, repassword);
    }
}
