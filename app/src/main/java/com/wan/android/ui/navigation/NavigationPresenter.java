package com.wan.android.ui.navigation;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.data.network.model.NavigationData;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BaseObserver;
import com.wan.android.ui.base.BasePresenter;
import com.wan.android.util.rx.RxUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author wzc
 * @date 2018/8/23
 */
public class NavigationPresenter<V extends NavigationContract.View> extends BasePresenter<V>
        implements NavigationContract.Presenter<V> {
    @Inject
    public NavigationPresenter(@ApplicationContext Context context, DataManager dataManager,
                               CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void getNavigation() {
        getCompositeDisposable().add(getDataManager().getNavigation()
                .compose(RxUtils.<CommonResponse<List<NavigationData>>>rxSchedulerHelper())
                .compose(RxUtils.<List<NavigationData>>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<List<NavigationData>>(getMvpView()) {
                    @Override
                    public void onNext(List<NavigationData> data) {
                        super.onNext(data);
                        getMvpView().showGetNavigationSuccess(data);
                    }
                }));
    }
}
