package com.wan.android.client;

import com.wan.android.bean.TreeListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author wzc
 * @date 2018/2/11
 */
public interface TreeListClient {
    // http://www.wanandroid.com/tree/json
    @GET("/tree/json")
    Call<TreeListResponse> getTree();
}
