package com.wan.android.ui.home;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BaseObserver;
import com.wan.android.ui.base.BasePresenter;
import com.wan.android.util.rx.RxUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

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
            // 获取数据库中的文章列表数据
            getCompositeDisposable().add(getDataManager().getDbHomeArticles()
                    .compose(RxUtils.<List<ArticleDatas>>rxSchedulerHelper())
                    .subscribe(new Consumer<List<ArticleDatas>>() {
                        @Override
                        public void accept(List<ArticleDatas> articleDatas) throws Exception {
                            if (articleDatas == null || articleDatas.isEmpty()) {
                                getMvpView().showSwipeRefreshFail();
                            } else {
                                getMvpView().showSwipeRefreshSuccess(articleDatas);
                            }
                        }
                    }));
            // 获取数据库中的 banner 数据
            getCompositeDisposable().add(getDataManager().getDbBanner()
                    .compose(RxUtils.<List<BannerData>>rxSchedulerHelper())
                    .subscribe(new Consumer<List<BannerData>>() {
                        @Override
                        public void accept(List<BannerData> data) throws Exception {
                            if (data != null && !data.isEmpty()) {
                                getMvpView().showBannerSuccess(data);
                            }
                        }
                    }));
            return;
        }
        getCompositeDisposable().add(getDataManager().getHomeList(mCurrPage)
                .compose(RxUtils.<CommonResponse<ArticleData>>rxSchedulerHelper())
                .compose(RxUtils.<ArticleData>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<ArticleData>(getMvpView()) {
                    @Override
                    public void onNext(ArticleData articleData) {
                        super.onNext(articleData);
                        saveHomeArticles2Db(articleData.getDatas());
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
                        // 存储 banner 数据到数据库
                        getCompositeDisposable().add(getDataManager().saveBanner2Db(bannerData)
                                .compose(RxUtils.<Boolean>rxSchedulerHelper())
                                .subscribe());
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

                        saveHomeArticles2Db(articleData.getDatas());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().showLoadMoreFail();
                    }
                }));
    }

    /**
     * 存储文章列表数据到数据库
     * @param data 数据集合
     */
    private void saveHomeArticles2Db(List<ArticleDatas> data) {
        getCompositeDisposable().add(getDataManager().saveHomeArticles2Db(data)
                .compose(RxUtils.<Boolean>rxSchedulerHelper())
                .subscribe());
    }

}
