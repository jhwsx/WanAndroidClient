package com.wan.android.client;

import com.wan.android.bean.BranchData;
import com.wan.android.bean.CommonResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 体系数据
 *
 * @author wzc
 * @date 2018/2/11
 */
public interface TreeListClient {
    // http://www.wanandroid.com/tree/json
    @GET("/tree/json")
    Call<CommonResponse<ArrayList<BranchData>>> getTree();
}
