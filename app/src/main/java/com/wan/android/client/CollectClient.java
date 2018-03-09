package com.wan.android.client;

import com.wan.android.bean.CollectRepsonse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author wzc
 * @date 2018/3/5
 */
public interface CollectClient {
    //    http://www.wanandroid.com/lg/collect/1165/json
    @POST("/lg/collect/{id}/json")
    Call<CollectRepsonse> collect(
            @Path("id") int id
    );
}
