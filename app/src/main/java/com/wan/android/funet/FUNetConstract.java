package com.wan.android.funet;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.annotation.NotProguard;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.FUNetData;

import java.util.List;

/**
 * @author wzc
 * @date 2018/5/31
 */
public interface FUNetConstract {
    @NotProguard
    interface View extends BaseView<Presenter> {
        void showGetFUNetsSuccess(List<FUNetData> data);

        void showGetFUNetsFail(CommonException e);
    }

    @NotProguard
    interface Presenter extends BasePresenter {
        void getFUNets();
    }

}
