package com.wan.android.ui.main;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.wan.android.data.DataManager;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author wzc
 * @date 2018/8/3
 */
public class MainPresenter<V extends MainContract.View> extends BasePresenter<V>
        implements MainContract.Presenter<V> {

    private ClearableCookieJar mClearableCookieJar;
    @Inject
    public MainPresenter(@ApplicationContext Context context, DataManager dataManager,
                         ClearableCookieJar clearableCookieJar,
                         CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
        mClearableCookieJar = clearableCookieJar;
    }
    public ClearableCookieJar getClearableCookieJar() {
        return mClearableCookieJar;
    }
    @Override
    public void onNavMenuCreated() {
        if (!isViewAttached()) {
            return;
        }
        // get username from data
        String username = getDataManager().getUsername();
        getMvpView().updateUsername(username);
    }

    @Override
    public void logout() {
        getDataManager().setUsername("");
        getDataManager().setLoginStatus(false);
        getClearableCookieJar().clear();
        getMvpView().showLogoutSuccess();
    }

    @Override
    public boolean getLoginStatus() {
        return getDataManager().getLoginStatus();
    }
}
