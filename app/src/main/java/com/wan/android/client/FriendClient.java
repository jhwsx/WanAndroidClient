package com.wan.android.client;

import com.wan.android.bean.CommonResponse;
import com.wan.android.bean.FriendData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 常用网站
 *
 * @author wzc
 * @date 2018/2/27
 */
public interface FriendClient {

    @GET("/friend/json")
    Call<CommonResponse<List<FriendData>>> getFriend();
}
