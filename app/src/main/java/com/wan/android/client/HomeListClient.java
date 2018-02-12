package com.wan.android.client;

import com.wan.android.bean.HomeListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author wzc
 * @date 2018/2/1
 */
public interface HomeListClient {
    // http://www.wanandroid.com/article/list/1/json
    @GET("/article/list/{page}/json")
    Call<HomeListResponse> getHomeFeed(
            @Path("page") int page
    );
}
