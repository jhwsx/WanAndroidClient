package com.wan.android.client;

import com.wan.android.bean.AccountData;
import com.wan.android.bean.CommonResponse;

import retrofit2.Call;
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
    Call<CommonResponse<AccountData>> login(
            @Field("username") String username,
            @Field("password") String password
    );
}
