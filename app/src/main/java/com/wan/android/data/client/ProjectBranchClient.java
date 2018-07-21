package com.wan.android.data.client;

import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.CommonResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author wzc
 * @date 2018/7/20
 */
public interface ProjectBranchClient {
    // http://www.wanandroid.com/project/list/1/json?cid=294
    @GET("/project/list/{page}/json")
    Observable<CommonResponse<ArticleData>> getProjectBranch(
            @Path("page") int page,
            @Query("cid") int cid
    );
}
