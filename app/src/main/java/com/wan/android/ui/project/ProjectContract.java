package com.wan.android.ui.project;

import com.wan.android.data.network.model.ProjectData;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

import java.util.List;

/**
 * @author wzc
 * @date 2018/8/27
 */
public interface ProjectContract {
    interface View extends MvpView {
        void showGetProjectSuccess(List<ProjectData> data);

        void showGetProjectFail();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {
        /**
         * 项目
         */
        void getProject();
    }

}
