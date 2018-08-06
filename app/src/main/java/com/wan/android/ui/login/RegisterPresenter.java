package com.wan.android.ui.login;

import android.content.Context;

import com.wan.android.R;
import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.AccountData;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BaseObserver;
import com.wan.android.ui.base.BasePresenter;
import com.wan.android.util.rx.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 注册 Presenter
 * @author wzc
 * @date 2018/8/6
 */
public class RegisterPresenter<V extends RegisterContract.View> extends BasePresenter<V>
        implements RegisterContract.Presenter<V> {
    @Inject
    public RegisterPresenter(@ApplicationContext Context context, DataManager dataManager,
                             CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void register(String username, String password, String repassword) {
        if (checkNetwork()) {
            return;
        }
        if (username == null || username.isEmpty()) {
            getMvpView().onError(R.string.login_username_empty);
            return;
        }
        if (password == null || password.isEmpty()) {
            getMvpView().onError(R.string.login_password_empty);
            return;
        }
        if (repassword == null || repassword.isEmpty()) {
            getMvpView().onError(R.string.register_repassword_empty);
            return;
        }
        if (!password.equals(repassword)) {
            getMvpView().onError(R.string.register_password_repassword_inconsistent);
            return;
        }
        getMvpView().hideKeyboard();
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
        .register(username, password,repassword)
                .compose(RxUtils.<CommonResponse<AccountData>>rxSchedulerHelper())
                .compose(RxUtils.<AccountData>handleResult(getApplicationContext(), getMvpView()))
        .subscribeWith(new BaseObserver<AccountData>(getMvpView()) {
            @Override
            public void onNext(AccountData accountData) {
                super.onNext(accountData);
                getMvpView().showRegisterSuccess(accountData);
                getMvpView().hideLoading();
            }
        }));
    }
}
