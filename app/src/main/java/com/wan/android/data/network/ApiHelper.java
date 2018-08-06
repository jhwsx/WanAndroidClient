package com.wan.android.data.network;

import com.wan.android.data.network.model.AccountData;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.CommonResponse;

import io.reactivex.Observable;

/**
 * @author wzc
 * @date 2018/8/2
 */
public interface ApiHelper {
    /**
     * get one page of home list
     * @param page page number
     * @return  ArticleData
     */
    Observable<CommonResponse<ArticleData>> getHomeList(int page);

    /**
     * login
     * @param username 用户名
     * @param password 用户密码
     * @return AccountData
     */
    Observable<CommonResponse<AccountData>> login(String username, String password);

    /**
     * 注册
     * @param username 用户名
     * @param password 用户密码
     * @param repassword 再次输入的用户密码
     * @return  AccountData 账户数据
     */
    Observable<CommonResponse<AccountData>> register(String username, String password, final String repassword);
}
