package com.wan.android.tree;

import com.wan.android.data.bean.BranchData;
import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.data.bean.CommonException;

import java.util.List;

/**
 * @author wzc
 * @date 2018/3/27
 */
public interface TreeContract {

    interface View extends BaseView<Presenter> {
        // 下拉刷新成功
        void showSwipeRefreshSuccess(List<BranchData> data);

        // 下拉刷新失败
        void showSwipeRefreshFail(CommonException e);
    }
    interface Presenter extends BasePresenter {
        // 下拉刷新
        void swipeRefresh();
    }
}
