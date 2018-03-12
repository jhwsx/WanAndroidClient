package com.wan.android.client;


import com.wan.android.bean.CommonResponse;
import com.wan.android.bean.ArticleData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 搜索
 * @author wzc
 * @date 2018/3/6
 */
public interface SearchClient {
    /**
     * @param page 页码：拼接在链接上，从0开始。
     * @param k 搜索关键词
     * @return
     */
    @FormUrlEncoded
    @POST("/article/query/{page}/json")
    Call<CommonResponse<ArticleData>> search(
            @Path("page") int page,
            @Field("k") String k);
}
