package com.wan.android.ui.project;

import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

import java.util.List;

/**
 * @author wzc
 * @date 2018/8/27
 */
public interface ProjectChildContract {

    interface View extends MvpView {
        void showSwipeRefreshSuccess(List<ArticleDatas> datas);

        void showSwipeRefreshNoData();

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

        void showCollectInSiteArticleSuccess();

        void showUncollectArticleListArticleSuccess();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {
        /**
         * 下拉刷新
         *
         * @param cid 分类的id
         */
        void swipeRefresh(int cid);

        /**
         * 上拉加载
         *
         * @param cid 分类的id
         */
        void loadMore(int cid);

        void collectInSiteArticle(int id);

        void uncollectArticleListArticle(int id);

        boolean getLoginStaus();
    }

}
