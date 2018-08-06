package com.wan.android.ui.main;

import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

/**
 * @author wzc
 * @date 2018/8/6
 */
public interface MainContract {

    interface View extends MvpView {
        /**
         * 更新用户名
         * @param username 用户名
         */
        void updateUsername(String username);

        /**
         * 显示退出登录成功
         */
        void showLogoutSuccess();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {

        void onNavMenuCreated();

        /**
         * 退出登录
         */
        void logout();
    }
}
