package com.wan.android.client;


import com.wan.android.bean.CollectData;
import com.wan.android.bean.CommonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 收藏文章列表
 * @author wzc
 * @date 2018/2/22
 */
public interface CollectListClient {
    // http://www.wanandroid.com/lg/collect/list/0/json
    @GET("/lg/collect/list/{page}/json")
    Call<CommonResponse<CollectData>> getCollect(
            @Path("page") int page
    );
}
