package com.wan.android.ui.collect;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.CollectDatas;
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
public class AddCollectPresenter<V extends AddCollectContract.View> extends BasePresenter<V>
        implements AddCollectContract.Presenter<V> {
    @Inject
    public AddCollectPresenter(@ApplicationContext Context context, DataManager dataManager,
                               CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void collectOutOfSiteArticle(String title, String author, String link) {
        getCompositeDisposable().add(getDataManager().collectOutOfSiteArticle(title, author, link)
                .compose(RxUtils.<CommonResponse<CollectDatas>>rxSchedulerHelper())
                .compose(RxUtils.<CollectDatas>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<CollectDatas>(getMvpView()) {
                    @Override
                    public void onNext(CollectDatas datas) {
                        super.onNext(datas);
                        getMvpView().showCollectOutOfSiteArticleSuccess(datas);
                    }

                }));
    }
}
