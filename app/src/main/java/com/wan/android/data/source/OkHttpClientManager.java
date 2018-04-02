package com.wan.android.data.source;

import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wan.android.BuildConfig;
import com.wan.android.WanAndroidApplication;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author wzc
 * @date 2018/2/24
 */
public class OkHttpClientManager {
    private static final String TAG = OkHttpClientManager.class.getSimpleName();
    private static OkHttpClient sOkHttpClient;
    private static Interceptor sLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            }
            // 在没有本地缓存的情况下，每个拦截器都必须至少调用chain.proceed(request)一次，这个简单的方法实现了Http请求的发起以及从服务端获取响应。
            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            }
            return response;
        }
    };
    public static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (OkHttpClientManager.class) {
                if (sOkHttpClient == null) {
                    Cache httpCache = new Cache(new File(WanAndroidApplication.getContext().getExternalFilesDir(null), "HttpCache"), 1024 * 1024 * 50);
                    ClearableCookieJar cookieJar =
                            new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(com.wan.android.util.Utils.getContext()));
                    sOkHttpClient = new OkHttpClient.Builder()
                            .cache(httpCache)
                            .addInterceptor(sLoggingInterceptor)
                            .cookieJar(cookieJar)
                            .build();
                }
            }
        }
        return sOkHttpClient;
    }
}
