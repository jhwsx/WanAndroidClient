package com.wan.android.client;


import com.wan.android.bean.SearchResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author wzc
 * @date 2018/3/6
 */
public interface SearchClient {
    // http://www.wanandroid.com/article/query/0/json
//    方法：POST
//    参数：
//    页码：拼接在链接上，从0开始。
//    k ： 搜索关键词
    @FormUrlEncoded
    @POST("/article/query/{page}/json")
    Call<SearchResponse> search(
            @Path("page") int page,
            @Field("k") String k);
}
