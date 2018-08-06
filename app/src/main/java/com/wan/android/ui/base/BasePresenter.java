package com.wan.android.ui.base;

import android.content.Context;

import com.wan.android.R;
import com.wan.android.data.DataManager;
import com.wan.android.di.ApplicationContext;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author wzc
 * @date 2018/8/3
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private final Context mContext;
    private final DataManager mDataManager;
    private final CompositeDisposable mCompositeDisposable;
    private V mMvpView;

    @Inject
    public BasePresenter(@ApplicationContext Context context, DataManager dataManager,
                         CompositeDisposable compositeDisposable) {
        mContext = context;
        mDataManager = dataManager;
        mCompositeDisposable = compositeDisposable;
    }


    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }


    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }

    @Override
    public void addRxBindingSubscribe(Disposable disposable) {
        getCompositeDisposable().add(disposable);
    }

    public V getMvpView() {
        return mMvpView;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }


    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public Context getApplicationContext() {
        return mContext;
    }
    @Override
    public void handleApiError(Throwable throwable) {

    }

    @Override
    public void setUserAsLoggedOut() {

    }

    protected boolean checkNetwork() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_msg_network_error);
            return true;
        }
        return false;
    }
}
