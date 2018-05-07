package com.wan.android.data.client;

import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.CommonResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author wzc
 * @date 2018/3/30
 */
public interface AuthorClient {
    // http://www.wanandroid.com/article/list/0/json?author=%E9%83%AD%E9%9C%96
    @GET("/article/list/{page}/json")
    Observable<CommonResponse<ArticleData>> getAuthorFilter(
            @Path("page") int page,
            @Query("author") String author
    );
}
