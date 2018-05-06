package com.wan.android.collect;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.annotation.NotProguard;
import com.wan.android.data.bean.CollectDatas;
import com.wan.android.data.bean.CommonException;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CollectContract {
    @NotProguard
    interface View extends BaseView<Presenter> {
        // 下拉刷新成功
        void showSwipeRefreshSuccess(List<CollectDatas> datas);

        // 下拉刷新失败
        void showSwipeRefreshFail(CommonException e);

        // 上拉加载更多成功
        void showLoadMoreSuccess(List<CollectDatas> datas);

        // 上拉加载更多失败
        void showLoadMoreFail(CommonException e);

        // 上拉加载更多完成
        void showLoadMoreComplete();

        // 上拉加载更多结束
        void showLoadMoreEnd();

        // 取消收藏成功
        void showUncollectSuccess();

        // 取消收藏失败
        void showUncollectFail(CommonException e);

        // 添加收藏成功
        void showAddCollectSuccess(CollectDatas data);

        // 添加收藏失败
        void showAddCollectFail(CommonException e);
    }
    @NotProguard
    interface Presenter extends BasePresenter {
        // 下拉刷新
        void swipeRefresh();

        // 上拉加载更多
        void loadMore(int currPage);

        // 取消收藏
        void uncollect(int id, int originalId);

        // 添加收藏
        void addCollect(String title, String author, String link);
    }

}
