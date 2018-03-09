package com.wan.android.client;

import com.wan.android.bean.FriendResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author wzc
 * @date 2018/2/27
 */
public interface FriendClient {

    @GET("/friend/json")
    Call<FriendResponse> getFriend();
}
