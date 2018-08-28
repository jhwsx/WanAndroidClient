package com.wan.android.ui.collect;

import com.wan.android.data.network.model.CollectDatas;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

import java.util.List;

/**
 * @author wzc
 * @date 2018/8/28
 */
public interface MyCollectionContract {

    interface View extends MvpView {
        void showSwipeRefreshSuccess(List<CollectDatas> datas);

        void showSwipeRefreshFail();

        void showSwipeRefreshNoData();

        void showLoadMoreSuccess(List<CollectDatas> datas);

        void showLoadMoreFail();
        /**
         * 本次上拉加载更多完成
         */
        void showLoadMoreComplete();

        /**
         * 上拉加载更多结束
         */
        void showLoadMoreEnd();

        void showUncollectMyCollectionArticleSuccess();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {

        void swipeRefresh();

        void loadMore();

        boolean getLoginStaus();

        void uncollectMyCollectionArticle(int id, int originId);
    }

}
