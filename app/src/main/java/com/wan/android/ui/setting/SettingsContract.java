package com.wan.android.ui.setting;

import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

/**
 * @author wzc
 * @date 2018/8/30
 */
public interface SettingsContract {

    interface View extends MvpView {
    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {

        boolean getLoginStatus();
    }
}
