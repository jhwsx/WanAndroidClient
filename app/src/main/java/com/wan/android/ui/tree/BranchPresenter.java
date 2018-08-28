package com.wan.android.ui.tree;

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
 * @date 2018/8/23
 */
public class BranchPresenter<V extends BranchContract.View> extends BasePresenter<V>
        implements BranchContract.Presenter<V> {
    private int mCurrPage = 0;
    @Inject
    public BranchPresenter(@ApplicationContext Context context, DataManager dataManager,
                           CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void swipeRefresh(int id) {
        mCurrPage = 0;
        if (!isNetworkConnected()) {
            return;
        }
        getCompositeDisposable().add(getDataManager().getLeafArticles(mCurrPage, id)
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
    }

    @Override
    public void loadMore(int id) {
        if (!isNetworkConnected()) {
            return;
        }
        mCurrPage++;
        getCompositeDisposable().add(getDataManager()
                .getLeafArticles(mCurrPage, id)
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

    @Override
    public void collectInSiteArticle(int id) {
        getCompositeDisposable().add(getDataManager().collectInSiteArticle(id)
                .compose(RxUtils.<CommonResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult2(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<String>(getMvpView()) {
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        getMvpView().showCollectInSiteArticleSuccess();
                    }
                }));
    }

    @Override
    public void uncollectArticleListArticle(int id) {
        getCompositeDisposable().add(getDataManager().uncollectArticleListArticle(id)
                .compose(RxUtils.<CommonResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult2(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<String>(getMvpView()) {
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        getMvpView().showUncollectArticleListArticleSuccess();
                    }
                }));
    }

    @Override
    public boolean getLoginStaus() {
        return getDataManager().getLoginStatus();
    }

}
