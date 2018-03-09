package com.wan.android.client;


import com.wan.android.bean.UncollectRepsonse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author wzc
 * @date 2018/3/5
 */
public interface UncollectClient {
    //    http://www.wanandroid.com/lg/uncollect_originId/2333/json
    @POST("/lg/uncollect_originId/{id}/json")
    Call<UncollectRepsonse> uncollect(
            @Path("id") int id
    );
}
