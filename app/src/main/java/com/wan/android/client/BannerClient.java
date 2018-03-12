package com.wan.android.client;

import com.wan.android.bean.BannerData;
import com.wan.android.bean.CommonResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 首页banner
 * @author wzc
 * @date 2018/2/1
 */
public interface BannerClient {
    @GET("/banner/json")
    Call<CommonResponse<List<BannerData>>> getBanner();
}
