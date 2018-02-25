package com.wan.android.retrofit;

import com.wan.android.WanAndroidApplication;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * @author wzc
 * @date 2018/2/24
 */
public class OkHttpClientManager {
    private static OkHttpClient sOkHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (OkHttpClientManager.class) {
                if (sOkHttpClient == null) {
                    Cache httpCache = new Cache(new File(WanAndroidApplication.getContext().getExternalFilesDir(null), "HttpCache"), 1024 * 1024 * 50);
                    sOkHttpClient = new OkHttpClient.Builder()
                            .cache(httpCache)
                            .cookieJar(new CookiesManager())
                            .build();
                }
            }
        }
        return sOkHttpClient;
    }
}
