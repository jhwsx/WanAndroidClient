package com.wan.android.ui.navigation;

import com.wan.android.data.network.model.NavigationData;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

import java.util.List;

/**
 * @author wzc
 * @date 2018/8/23
 */
public interface NavigationContract {
    interface View extends MvpView {

        void showGetNavigationSuccess(List<NavigationData> data);

        void showGetNavigationFail();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {
        /**
         * 导航
         */
        void getNavigation();
    }

}
