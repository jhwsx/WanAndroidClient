package com.wan.android.content;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.annotation.NotProguard;
import com.wan.android.data.bean.CommonException;

/**
 * @author wzc
 * @date 2018/3/29
 */
public interface ContentContract {
    @NotProguard
    interface View extends BaseView<Presenter> {
        void showCollectSuccess();
        void showCollectFail(CommonException e);
    }
    @NotProguard
    interface Presenter extends BasePresenter {
        void collect(int id);
    }

}