package com.wan.android.data.client;

import com.wan.android.data.bean.BannerData;
import com.wan.android.data.bean.CommonResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 首页banner
 * @author wzc
 * @date 2018/2/1
 */
public interface BannerClient {
    @GET("/banner/json")
    Observable<CommonResponse<List<BannerData>>> getBanner();
}
