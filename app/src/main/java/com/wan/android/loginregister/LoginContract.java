package com.wan.android.loginregister;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.annotation.NotProguard;
import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.CommonException;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface LoginContract {
    @NotProguard
    interface View extends BaseView<Presenter> {
        void showLoginSuccess(AccountData accountData);
        void showLoginFail(CommonException e);
        void showProgressBar();
        void dismissProgressBar();
    }

    @NotProguard
    interface Presenter extends BasePresenter {
        void login(String username, String password);
    }
}
