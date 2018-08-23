package com.wan.android.ui.search;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BaseObserver;
import com.wan.android.ui.base.BasePresenter;
import com.wan.android.util.rx.RxUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author wzc
 * @date 2018/8/22
 */
public class SearchResultPresenter<V extends SearchResultContract.View> extends BasePresenter<V>
        implements SearchResultContract.Presenter<V> {
    private int mCurrPage = 0;

    @Inject
    public SearchResultPresenter(@ApplicationContext Context context, DataManager dataManager,
                                 CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void swipeRefresh(String k) {
        mCurrPage = 0;
        if (!isNetworkConnected()) {
            return;
        }

        getCompositeDisposable().add(getDataManager().search(mCurrPage, k)
                .compose(RxUtils.<CommonResponse<ArticleData>>rxSchedulerHelper())
                .compose(RxUtils.<ArticleData>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<ArticleData>(getMvpView()) {
                    @Override
                    public void onNext(ArticleData articleData) {
                        super.onNext(articleData);
                        List<ArticleDatas> datas = articleData.getDatas();
                        if (datas.isEmpty()) {
                            getMvpView().showSearchNoMatch();
                        } else {
                            getMvpView().showSwipeRefreshSuccess(datas);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().showSwipeRefreshFail();
                    }
                }));
    }

    @Override
    public void loadMore(String k) {
        mCurrPage++;
        getCompositeDisposable().add(getDataManager()
                .search(mCurrPage, k)
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
