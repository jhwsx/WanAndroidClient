package com.wan.android.ui.tree;

import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

import java.util.List;

/**
 * @author wzc
 * @date 2018/8/23
 */
public interface BranchContract {

    interface View extends MvpView {
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
         * 下拉刷新
         * @param id 分类的id
         */
        void swipeRefresh(int id);

        /**
         * 上拉加载
         * @param id 分类的id
         */
        void loadMore(int id);
    }

}
