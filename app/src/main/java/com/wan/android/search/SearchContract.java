package com.wan.android.search;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.annotation.NotProguard;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.HotkeyData;

import java.util.List;

/**
 * @author wzc
 * @date 2018/3/29
 */
public interface SearchContract {
    @NotProguard
    interface View extends BaseView<Presenter> {
        // 获取热门搜索成功
        void showHotkeySuccess(List<HotkeyData> dataList);

        // 获取热门搜索失败
        void showHotkeyFail(CommonException e);

        // 下拉刷新成功
        void showSwipeRefreshSuccess(List<ArticleDatas> datas);

        // 下拉刷新失败
        void showSwipeRefreshFail(CommonException e);

        // 上拉加载更多成功
        void showLoadMoreSuccess(List<ArticleDatas> datas);

        // 上拉加载更多失败
        void showLoadMoreFail(CommonException e);

        // 上拉加载更多完成
        void showLoadMoreComplete();

        // 上拉加载更多结束
        void showLoadMoreEnd();

        // 收藏成功
        void showCollectSuccess();

        // 收藏失败
        void showCollectFail(CommonException e);

        // 取消收藏成功
        void showUncollectSuccess();

        // 取消收藏失败
        void showUncollectFail(CommonException e);

    }
    @NotProguard
    interface Presenter extends BasePresenter {
        // 获取热门搜索
        void fetchHotkey();

        // 下拉刷新
        void swipeRefresh(String query);

        // 上拉加载更多
        void loadMore(int currPage, String query);

        // 收藏
        void collect(int id);

        // 取消收藏
        void uncollect(int id);
    }
}

