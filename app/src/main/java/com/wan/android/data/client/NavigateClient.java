package com.wan.android.data.client;

import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.NavigationData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 导航信息
 * @author wzc
 * @date 2018/3/20
 */
public interface NavigateClient {
    // http://www.wanandroid.com/navi/json
    @GET("/navi/json")
    Call<CommonResponse<List<NavigationData>>> getNavigation();
}
