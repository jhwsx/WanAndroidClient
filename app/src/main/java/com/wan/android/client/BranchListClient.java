package com.wan.android.client;

import com.wan.android.bean.BranchListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wzc on 2018/2/16.
 */

public interface BranchListClient {
    //    http://www.wanandroid.com/article/list/0/json?cid=60
    @GET("article/list/{page}/json")
    Call<BranchListResponse> getBranchList(
            @Path("page") int page,
            @Query("cid") int cid
    );
}
