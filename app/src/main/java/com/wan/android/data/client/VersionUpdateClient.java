package com.wan.android.data.client;

import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.VersionUpdateData;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author wzc
 * @date 2018/5/29
 */
public interface VersionUpdateClient {
    //    http://wanandroid.com/tools/mockapi/833/wanandroid_versionupdate
    @GET("/tools/mockapi/833/wanandroid_versionupdate")
    Observable<CommonResponse<VersionUpdateData>> checkVersion();
}
