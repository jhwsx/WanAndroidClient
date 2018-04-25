package com.wan.android.loginregister;

import com.wan.android.data.bean.AccountData;
import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.data.bean.CommonException;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface RegisterContract {
    interface View extends BaseView<Presenter> {
        void showRegisterSuccess(AccountData accountData);
        void showRegisterFail(CommonException e);
        void showProgressBar();
        void dismissProgressBar();
    }


    interface Presenter extends BasePresenter {
        void register(String username, String password, String repassword);
    }
}
