package com.wan.android.data.network;

import com.wan.android.data.network.model.AccountData;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.data.network.model.HotkeyData;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author wzc
 * @date 2018/8/2
 */
public interface ApiHelper {
    /**
     * get one page of home list
     *
     * @param page page number
     * @return ArticleData
     */
    Observable<CommonResponse<ArticleData>> getHomeList(int page);

    /**
     * login
     *
     * @param username 用户名
     * @param password 用户密码
     * @return AccountData
     */
    Observable<CommonResponse<AccountData>> login(String username, String password);

    /**
     * 注册
     *
     * @param username   用户名
     * @param password   用户密码
     * @param repassword 再次输入的用户密码
     * @return AccountData 账户数据
     */
    Observable<CommonResponse<AccountData>> register(String username, String password, final String repassword);

    /**
     * 获取轮播图
     *
     * @return BannerData 集合
     */
    Observable<CommonResponse<List<BannerData>>> getBanner();

    /**
     * 搜索热词
     *
     * @return 热词数据集合
     */
    Observable<CommonResponse<List<HotkeyData>>> getHotkey();

    /**
     * 搜索
     *
     * @param page 页码
     * @param k    搜索关键词 注意：支持多个关键词，用空格隔开
     * @return 搜索到的一页数据
     */
    Observable<CommonResponse<ArticleData>> search(int page, String k);
}
