package com.wan.android.client;


import com.wan.android.bean.CommonResponse;
import com.wan.android.bean.ArticleData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 知识体系下的文章
 *
 * @author wzc
 * @date 2018/2/16
 */

public interface BranchListClient {
    //    http://www.wanandroid.com/article/list/0/json?cid=60
    @GET("article/list/{page}/json")
    Call<CommonResponse<ArticleData>> getBranchList(
            @Path("page") int page,
            @Query("cid") int cid
    );
}
