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
        /**
         * 显示无网络
         */
        void showNetworkError();

        /**
         * 显示登录用户名为空
         */
        void showLoginUsernameEmpty();

        /**
         * 显示登录密码为空
         */
        void showLoginPasswordEmpty();

        /**
         * 显示登录成功
         * @param accountData 账户数据
         */
        void showLoginSuccess(AccountData accountData);

        /**
         * 显示登录失败
         * @param e 失败信息
         */
        void showLoginFail(CommonException e);

        /**
         * 显示进度圈
         */
        void showProgressBar();

        /**
         * 隐藏进度圈
         */
        void dismissProgressBar();
    }

    @NotProguard
    interface Presenter extends BasePresenter {
        /**
         * 登录
         * @param username 用户名
         * @param password 密码
         */
        void login(String username, String password);
    }
}
