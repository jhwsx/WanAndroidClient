package com.wan.android.client;

import com.wan.android.bean.CommonResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 收藏站内文章
 *
 * @author wzc
 * @date 2018/3/5
 */
public interface CollectClient {
    //    http://www.wanandroid.com/lg/collect/1165/json
    @POST("/lg/collect/{id}/json")
    Call<CommonResponse<String>> collect(
            @Path("id") int id
    );
}
