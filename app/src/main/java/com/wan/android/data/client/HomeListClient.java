package com.wan.android.data.client;

import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ArticleData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 首页文章列表
 * @author wzc
 * @date 2018/2/1
 */
public interface HomeListClient {
    // http://www.wanandroid.com/article/list/1/json
    @GET("/article/list/{page}/json")
    Call<CommonResponse<ArticleData>> getHomeFeed(
            @Path("page") int page
    );
}