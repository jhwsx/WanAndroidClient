package com.wan.android.navigate;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.annotation.NotProguard;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.NavigationData;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface NavigationContract {
    @NotProguard
    interface View extends BaseView<Presenter> {
        // navigation获取成功
        void showNavigationSuccess(List<NavigationData> dataList);
        // navigation获取失败
        void showNavigationFail(CommonException e);

    }
    @NotProguard
    interface Presenter extends BasePresenter {
        void fetchNavigation();
    }
}
