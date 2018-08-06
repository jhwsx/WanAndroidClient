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
 * 登录 Presenter
 * @author wzc
 * @date 2018/8/3
 */
public class LoginPresenter<V extends LoginContract.View> extends BasePresenter<V>
        implements LoginContract.Presenter<V> {
    @Inject
    public LoginPresenter(@ApplicationContext Context context, DataManager dataManager,
                          CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void login(String username, String password) {
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
        // 隐藏软键盘
        getMvpView().hideKeyboard();
        // 显示加载动画
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .login(username, password)
                .compose(RxUtils.<CommonResponse<AccountData>>rxSchedulerHelper())
                .compose(RxUtils.<AccountData>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<AccountData>(getMvpView()){
                    @Override
                    public void onNext(AccountData accountData) {
                        super.onNext(accountData);
                        getMvpView().showLoginSuccess(accountData);
                        getMvpView().hideLoading();
                    }
                }));
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        getDataManager().setLoginStatus(isLogin);
    }

    @Override
    public void setUserName(String userName) {
        getDataManager().setUsername(userName);
    }


}
