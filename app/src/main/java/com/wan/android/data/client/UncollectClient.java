package com.wan.android.data.client;


import com.wan.android.data.bean.CommonResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 *  取消收藏-文章列表
 * @author wzc
 * @date 2018/3/5
 */
public interface UncollectClient {
    //    http://www.wanandroid.com/lg/uncollect_originId/2333/json
    @POST("/lg/uncollect_originId/{id}/json")
    Call<CommonResponse<String>> uncollect(
            @Path("id") int id
    );
}
