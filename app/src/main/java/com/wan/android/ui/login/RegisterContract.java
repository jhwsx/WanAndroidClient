package com.wan.android.ui.login;

import com.wan.android.data.network.model.AccountData;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

/**
 * 注册契约类
 * @author wzc
 * @date 2018/8/6
 */
public interface RegisterContract {
    interface View extends MvpView {

        void showRegisterSuccess(AccountData accountData);

    }

    interface Presenter<V extends View> extends MvpPresenter<V> {

        void register(String username, String password, String repassword);
    }

}
