package com.wan.android.ui.search;

import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

import java.util.List;

/**
 * @author wzc
 * @date 2018/8/22
 */
public interface SearchResultContract {

    interface View extends MvpView {

        void showSearchNoMatch();

        void showSwipeRefreshSuccess(List<ArticleDatas> datas);

        void showSwipeRefreshFail();

        void showLoadMoreSuccess(List<ArticleDatas> datas);

        void showLoadMoreFail();
        /**
         * 本次上拉加载更多完成
         */
        void showLoadMoreComplete();

        /**
         * 上拉加载更多结束
         */
        void showLoadMoreEnd();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {
        /**
         * 下拉刷新搜索
         * @param k 搜索关键词
         */
        void swipeRefresh(String k);

        /**
         * 上拉加载搜索
         * @param k 搜索关键词
         */
        void loadMore(String k);
    }

}
