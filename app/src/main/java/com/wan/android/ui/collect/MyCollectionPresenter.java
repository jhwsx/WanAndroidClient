package com.wan.android.ui.collect;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.CollectData;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BaseObserver;
import com.wan.android.ui.base.BasePresenter;
import com.wan.android.util.rx.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author wzc
 * @date 2018/8/28
 */
public class MyCollectionPresenter<V extends MyCollectionContract.View> extends BasePresenter<V>
        implements MyCollectionContract.Presenter<V> {
    private int mCurrPage = 0;

    @Inject
    public MyCollectionPresenter(@ApplicationContext Context context, DataManager dataManager,
                                 CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void swipeRefresh() {
        mCurrPage = 0;
        if (!isNetworkConnected()) {
            getMvpView().showSwipeRefreshFail();
            return;
        }
        getCompositeDisposable().add(getDataManager().getMyCollection(mCurrPage)
                .compose(RxUtils.<CommonResponse<CollectData>>rxSchedulerHelper())
                .compose(RxUtils.<CollectData>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<CollectData>(getMvpView()) {
                    @Override
                    public void onNext(CollectData articleData) {
                        super.onNext(articleData);
                        if (articleData.getDatas().isEmpty()) {
                            getMvpView().showSwipeRefreshNoData();
                        } else {
                            getMvpView().showSwipeRefreshSuccess(articleData.getDatas());
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
    public void loadMore() {
        if (!isNetworkConnected()) {
            return;
        }
        mCurrPage++;
        getCompositeDisposable().add(getDataManager().getMyCollection(mCurrPage)
                .compose(RxUtils.<CommonResponse<CollectData>>rxSchedulerHelper())
                .compose(RxUtils.<CollectData>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<CollectData>(getMvpView()) {
                    @Override
                    public void onNext(CollectData articleData) {
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

    @Override
    public boolean getLoginStaus() {
        return getDataManager().getLoginStatus();
    }

    @Override
    public void uncollectMyCollectionArticle(int id, int originId) {
        getCompositeDisposable().add(getDataManager().uncollectMyCollectionArticle(id, originId)
                .compose(RxUtils.<CommonResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult2(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<String>(getMvpView()) {
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        getMvpView().showUncollectMyCollectionArticleSuccess();
                    }

                }));
    }
}
