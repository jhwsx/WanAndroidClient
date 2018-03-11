package com.wan.android.client;

import com.wan.android.bean.UncollectRepsonse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author wzc
 * @date 2018/3/11
 */
public interface UncollectAllClient {
    // http://www.wanandroid.com/lg/uncollect/2805/json
    @FormUrlEncoded
    @POST("/lg/uncollect/{id}/json")
    Call<UncollectRepsonse> uncollectAll(
            @Path("id") int id,
            @Field("originId") String originId
    );
}
