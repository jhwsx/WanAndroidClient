package com.wan.android.client;

import com.wan.android.bean.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by wzc on 2018/2/20.
 */

public interface LoginClient {

    @FormUrlEncoded
    @POST("/user/login")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );
}
