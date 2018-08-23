package com.wan.android.ui.tree;

import com.wan.android.data.network.model.BranchData;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

import java.util.List;

/**
 * @author wzc
 * @date 2018/8/23
 */
public interface TreeContract {
    interface View extends MvpView {
        // 下拉刷新成功
        void showSwipeRefreshSuccess(List<BranchData> data);

        void showSwipeRefreshFail();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {
        /**
         * 下拉刷新
         */
        void swipeRefresh();
    }

}
