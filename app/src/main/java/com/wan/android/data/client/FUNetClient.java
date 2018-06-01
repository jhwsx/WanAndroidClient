package com.wan.android.data.client;

import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.FUNetData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author wzc
 * @date 2018/5/31
 */
public interface FUNetClient {
   // http://wanandroid.com/lg/collect/usertools/json
    @GET("/lg/collect/usertools/json")
    Observable<CommonResponse<List<FUNetData>>> getFUNets();
}
