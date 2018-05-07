package com.wan.android.data.client;

import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.FriendData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 常用网站
 *
 * @author wzc
 * @date 2018/2/27
 */
public interface FriendClient {

    @GET("/friend/json")
    Observable<CommonResponse<List<FriendData>>> getFriend();
}
