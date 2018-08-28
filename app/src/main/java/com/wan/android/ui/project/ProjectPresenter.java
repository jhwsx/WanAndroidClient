package com.wan.android.ui.project;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.data.network.model.ProjectData;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BaseObserver;
import com.wan.android.ui.base.BasePresenter;
import com.wan.android.util.rx.RxUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author wzc
 * @date 2018/8/27
 */
public class ProjectPresenter<V extends ProjectContract.View> extends BasePresenter<V>
        implements ProjectContract.Presenter<V> {
    @Inject
    public ProjectPresenter(@ApplicationContext Context context, DataManager dataManager,
                            CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void getProject() {
        if (!isNetworkConnected()) {
            getMvpView().showGetProjectFail();
            return;
        }
        getCompositeDisposable().add(getDataManager().getProject()
                .compose(RxUtils.<CommonResponse<List<ProjectData>>>rxSchedulerHelper())
                .compose(RxUtils.<List<ProjectData>>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<List<ProjectData>>(getMvpView()) {
                    @Override
                    public void onNext(List<ProjectData> data) {
                        super.onNext(data);
                        getMvpView().showGetProjectSuccess(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getMvpView().showGetProjectFail();
                    }
                }));
    }
}
