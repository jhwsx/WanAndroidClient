package com.wan.android.data.client;

import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.CommonResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 登录
 * @author wzc
 * @date 2018/2/20
 */
public interface LoginClient {

    @FormUrlEncoded
    @POST("/user/login")
    Observable<CommonResponse<AccountData>> login(
            @Field("username") String username,
            @Field("password") String password
    );
}
