package com.wan.android.ui.setting;

import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

/**
 * @author wzc
 * @date 2018/8/30
 */
public interface RoastContract {
    interface View extends MvpView {

    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {

        String getRoastOpenid();

        void setRoastOpenid(String openid);

        int getRoastHeadPicId();

        void setRoastHeadPicId(int headPicId);

        String getUsername();
    }

}
