package com.wan.android.home;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.annotation.NotProguard;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.BannerData;
import com.wan.android.data.bean.CommonException;

import java.util.List;

/**
 * @author wzc
 * @date 2018/3/27
 */
public interface HomeContract {
    @NotProguard
    interface View extends BaseView<Presenter> {
        // banner获取成功
        void showBannerSuccess(List<BannerData> data);

        // banner获取失败
        void showBannerFail(CommonException e);

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
        // 下拉刷新
        void swipeRefresh();

        // 上拉加载更多
        void loadMore(int currPage);

        // 请求banner
        void fetchBanner();

        // 收藏
        void collect(int id);

        // 取消收藏
        void uncollect(int id);
    }

}
