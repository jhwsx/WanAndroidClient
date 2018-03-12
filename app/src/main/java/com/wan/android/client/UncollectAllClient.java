package com.wan.android.client;

import com.wan.android.bean.CommonResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 取消收藏-我的收藏页面（该页面包含自己录入的内容）
 *
 * @author wzc
 * @date 2018/3/11
 */
public interface UncollectAllClient {
    // http://www.wanandroid.com/lg/uncollect/2805/json
    @FormUrlEncoded
    @POST("/lg/uncollect/{id}/json")
    Call<CommonResponse<String>> uncollectAll(
            @Path("id") int id,
            @Field("originId") int originId
    );
}
