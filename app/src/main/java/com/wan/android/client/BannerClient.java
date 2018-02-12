package com.wan.android.client;

import com.wan.android.bean.BannerResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author wzc
 * @date 2018/2/1
 */
public interface BannerClient {
    @GET("/banner/json")
    Call<BannerResponse> getBanner();
}
