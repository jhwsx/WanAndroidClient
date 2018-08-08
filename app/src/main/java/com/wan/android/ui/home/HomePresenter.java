package com.wan.android.ui.home;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BaseObserver;
import com.wan.android.ui.base.BasePresenter;
import com.wan.android.util.rx.RxUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 首页 Presenter
 *
 * @author wzc
 * @date 2018/8/7
 */
public class HomePresenter<V extends HomeContract.View> extends BasePresenter<V>
        implements HomeContract.Presenter<V> {
    private int mCurrPage = 0;
    private static final String BANNER_DATA = "banner_data";
    private static final String HOME_LIST_DATA = "home_list_data";

    @Inject
    public HomePresenter(@ApplicationContext Context context, DataManager dataManager,
                         CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void swipeRefresh(boolean isBannerLoaded) {
        mCurrPage = 0;
        if (!isNetworkConnected()) {
            getMvpView().showSwipeRefreshFail();
            return;
        }
        getCompositeDisposable().add(getDataManager().getHomeList(mCurrPage)
                .compose(RxUtils.<CommonResponse<ArticleData>>rxSchedulerHelper())
                .compose(RxUtils.<ArticleData>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<ArticleData>(getMvpView()) {
                    @Override
                    public void onNext(ArticleData articleData) {
                        super.onNext(articleData);
                        getMvpView().showSwipeRefreshSuccess(articleData.getDatas());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().showSwipeRefreshFail();
                    }
                }));

        if (isBannerLoaded) {
            return;
        }
        getCompositeDisposable().add(getDataManager().getBanner()
                .compose(RxUtils.<CommonResponse<List<BannerData>>>rxSchedulerHelper())
                .compose(RxUtils.<List<BannerData>>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<List<BannerData>>(getMvpView()) {
                    @Override
                    public void onNext(List<BannerData> bannerData) {
                        super.onNext(bannerData);
                        getMvpView().showBannerSuccess(bannerData);
                    }

                }));

    }

    @Override
    public void loadMore() {
        mCurrPage++;
        getCompositeDisposable().add(getDataManager()
                .getHomeList(mCurrPage)
                .compose(RxUtils.<CommonResponse<ArticleData>>rxSchedulerHelper())
                .compose(RxUtils.<ArticleData>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<ArticleData>(getMvpView()) {
                    @Override
                    public void onNext(ArticleData articleData) {
                        super.onNext(articleData);
                        getMvpView().showLoadMoreSuccess(articleData.getDatas());

                        int nextPage = mCurrPage + 1;
                        if (nextPage <= articleData.getPagecount()) {
                            // 不是最后一页
                            getMvpView().showLoadMoreComplete();
                        } else {
                            // 已经是最后一页
                            getMvpView().showLoadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().showLoadMoreFail();
                    }
                }));
    }

}
