package com.wan.android.data.client;

import com.wan.android.data.bean.CollectDatas;
import com.wan.android.data.bean.CommonResponse;

import io.reactivex.Observable;
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
    Observable<CommonResponse<CollectDatas>> collectOther(
            @Field("title") String title,
            @Field("author") String author,
            @Field("link") String link
    );
}
