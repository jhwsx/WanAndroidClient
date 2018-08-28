package com.wan.android.ui.content;

import android.content.Context;

import com.wan.android.data.DataManager;
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
public class ContentPresenter<V extends ContentContract.View> extends BasePresenter<V>
        implements ContentContract.Presenter<V> {
    @Inject
    public ContentPresenter(@ApplicationContext Context context, DataManager dataManager,
                            CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
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
