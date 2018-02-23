package com.wan.android.client;

import com.wan.android.bean.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by wzc on 2018/2/21.
 */

public interface RegisterClient {
    // http://www.wanandroid.com/user/register
    @FormUrlEncoded
    @POST("/user/register")
    Call<RegisterResponse> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("repassword") String repassword

    );
}
