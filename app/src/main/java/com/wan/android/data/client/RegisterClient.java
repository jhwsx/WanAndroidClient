package com.wan.android.data.client;

import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.CommonResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 注册
 * @author wzc
 * @date 2018/2/21
 */
public interface RegisterClient {
    // http://www.wanandroid.com/user/register
    @FormUrlEncoded
    @POST("/user/register")
    Observable<CommonResponse<AccountData>> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("repassword") String repassword

    );
}
