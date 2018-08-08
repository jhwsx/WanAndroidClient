package com.wan.android.data.network.api;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.wan.android.BuildConfig;
import com.wan.android.data.network.model.AccountData;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.CommonResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author wzc
 * @date 2018/8/2
 */
public interface ApiCall {
    /**
     * 获取首页列表页
     * @param page 页码
     * @return ArticleData 一页列表数据
     */
    @GET("/article/list/{page}/json")
    Observable<CommonResponse<ArticleData>> getHomeList(
            @Path("page") int page
    );

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return AccountData 账户数据
     */
    @FormUrlEncoded
    @POST("/user/login")
    Observable<CommonResponse<AccountData>> login(
            @Field("username") String username,
            @Field("password") String password
    );

    /**
     * 注册
     * @param username 用户名
     * @param password 用户密码
     * @param repassword 再次输入的用户密码
     * @return  AccountData 账户数据
     */
    @FormUrlEncoded
    @POST("/user/register")
    Observable<CommonResponse<AccountData>> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("repassword") String repassword
    );

    @GET("/banner/json")
    Observable<CommonResponse<List<BannerData>>> getBanner();


    class Factory {

        private Factory() {
            //no instance
        }

        private static final String API_BASE_URL = "http://wanandroid.com/";
        private static final int NETWORK_CALL_TIMEOUT = 60;

        public static ApiCall create(Cache cache, ClearableCookieJar cookieJar) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            logging.setLevel(
                    BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            builder.addInterceptor(logging);

            builder.readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS);

            builder.writeTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS);

            OkHttpClient httpClient = builder
                    .cache(cache)
                    .cookieJar(cookieJar).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(ApiCall.class);
        }
    }
}
