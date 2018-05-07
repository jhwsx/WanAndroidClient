package com.wan.android.data.client;

import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.CommonResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
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
    Observable<CommonResponse<ArrayList<BranchData>>> getTree();
}
