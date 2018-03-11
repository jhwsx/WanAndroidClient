package com.wan.android.client;

import com.wan.android.bean.CollectOtherResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author wzc
 * @date 2018/3/11
 */
public interface CollectOtherClient {
    //    http://www.wanandroid.com/lg/collect/add/json
//
//    方法：POST
//    参数：
//    title，author，link
    @FormUrlEncoded
    @POST("/lg/collect/add/json")
    Call<CollectOtherResponse> collectOther(
            @Field("title") String title,
            @Field("author") String author,
            @Field("link") String link
    );
}
