package com.wan.android.ui.project;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.ArticleData;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BaseObserver;
import com.wan.android.ui.base.BasePresenter;
import com.wan.android.util.rx.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author wzc
 * @date 2018/8/27
 */
public class ProjectChildPresenter<V extends ProjectChildContract.View> extends BasePresenter<V>
        implements ProjectChildContract.Presenter<V> {
    @Inject
    public ProjectChildPresenter(@ApplicationContext Context context, DataManager dataManager,
                                 CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }
    private int mCurrPage = 1;
    @Override
    public void swipeRefresh(int id) {
        mCurrPage = 1;
        if (!isNetworkConnected()) {
            return;
        }
        getCompositeDisposable().add(getDataManager().getProjectList(mCurrPage, id)
                .compose(RxUtils.<CommonResponse<ArticleData>>rxSchedulerHelper())
                .compose(RxUtils.<ArticleData>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<ArticleData>(getMvpView()) {
                    @Override
                    public void onNext(ArticleData articleData) {
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
    public void loadMore(int id) {
        if (!isNetworkConnected()) {
            return;
        }
        mCurrPage++;
        getCompositeDisposable().add(getDataManager()
                .getProjectList(mCurrPage, id)
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
