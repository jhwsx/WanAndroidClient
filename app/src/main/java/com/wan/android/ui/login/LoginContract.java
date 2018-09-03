package com.wan.android.ui.login;

import com.wan.android.data.network.model.AccountData;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

/**
 * 登录契约类
 *
 * @author wzc
 * @date 2018/8/6
 */
public interface LoginContract {

    interface View extends MvpView {

        /**
         * 显示登录成功
         *
         * @param accountData 账户数据
         */
        void showLoginSuccess(AccountData accountData);

    }

    interface Presenter<V extends View> extends MvpPresenter<V> {

        /**
         * 登录
         *
         * @param username 用户名
         * @param password 密码
         */
        void login(String username, String password);

        void setLoginStatus(boolean isLogin);

        void setUserName(String userName);
    }
}
