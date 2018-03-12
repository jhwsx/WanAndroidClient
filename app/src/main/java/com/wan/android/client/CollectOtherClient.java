package com.wan.android.client;

import com.wan.android.bean.CollectDatas;
import com.wan.android.bean.CommonResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 收藏站外文章
 *
 * @author wzc
 * @date 2018/3/11
 */
public interface CollectOtherClient {
    @FormUrlEncoded
    @POST("/lg/collect/add/json")
    Call<CommonResponse<CollectDatas>> collectOther(
            @Field("title") String title,
            @Field("author") String author,
            @Field("link") String link
    );
}
