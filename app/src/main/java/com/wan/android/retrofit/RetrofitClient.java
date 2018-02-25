package com.wan.android.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wzc
 * @date 2018/2/24
 */
public class RetrofitClient {
    private static final String API_BASE_URL = "http://wanandroid.com/";

    private RetrofitClient() {
        //no instance
    }

    public static <T> T create(Class<T> clazz) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder
                .client(OkHttpClientManager.getOkHttpClient())
                .build();
        return retrofit.create(clazz);
    }
}
