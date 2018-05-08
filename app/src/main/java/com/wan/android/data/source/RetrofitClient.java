package com.wan.android.data.source;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String API_BASE_URL = "http://wanandroid.com/";
    private  Retrofit mRetrofit;
    private RetrofitClient() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        mRetrofit = builder
                .client(OkHttpClientManager.getOkHttpClient())
                .build();
    }

    public  <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    public static RetrofitClient getInstance() {
        return RetrofitClientHolder.sInstance;
    }

    private static class RetrofitClientHolder {
        private static RetrofitClient sInstance = new RetrofitClient();
    }
}