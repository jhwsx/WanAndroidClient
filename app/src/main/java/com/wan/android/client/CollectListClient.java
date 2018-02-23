package com.wan.android.client;


import com.wan.android.bean.CollectListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by wzc on 2018/2/22.
 */

public interface CollectListClient {
    // http://www.wanandroid.com/lg/collect/list/0/json
    @GET("/lg/collect/list/{page}/json")
    Call<CollectListResponse> getCollect(
            @Path("page") int page
    );
}
