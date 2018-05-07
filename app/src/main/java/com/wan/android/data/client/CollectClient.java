package com.wan.android.data.client;

import com.wan.android.data.bean.CommonResponse;

import io.reactivex.Observable;
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
    Observable<CommonResponse<String>> collect(
            @Path("id") int id
    );
}
