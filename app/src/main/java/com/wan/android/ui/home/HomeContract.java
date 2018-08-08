package com.wan.android.ui.home;

import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

import java.util.List;

/**
 * 首页契约类
 *
 * @author wzc
 * @date 2018/8/7
 */
public interface HomeContract {

    interface View extends MvpView {

        void showBannerSuccess(List<BannerData> data);

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

        void swipeRefresh(boolean isBannerLoaded);

        void loadMore();
    }
}

