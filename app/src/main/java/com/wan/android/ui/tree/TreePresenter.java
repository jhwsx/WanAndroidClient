package com.wan.android.ui.tree;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.BranchData;
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
 * @date 2018/8/23
 */
public class TreePresenter<V extends TreeContract.View> extends BasePresenter<V>
        implements TreeContract.Presenter<V> {
    @Inject
    public TreePresenter(@ApplicationContext Context context, DataManager dataManager,
                         CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void swipeRefresh() {
        getCompositeDisposable().add(getDataManager().getTree()
                .compose(RxUtils.<CommonResponse<List<BranchData>>>rxSchedulerHelper())
                .compose(RxUtils.<List<BranchData>>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<List<BranchData>>(getMvpView()) {
                    @Override
                    public void onNext(List<BranchData> branchData) {
                        super.onNext(branchData);
                        getMvpView().showSwipeRefreshSuccess(branchData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().showSwipeRefreshFail();
                    }
                }));
    }
}
