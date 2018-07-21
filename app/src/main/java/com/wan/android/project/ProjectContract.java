package com.wan.android.project;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ProjectTreeBranchData;

import java.util.List;

/**
 * @author wzc
 * @date 2018/7/20
 */
public interface ProjectContract {

    interface View extends BaseView<BasePresenter> {

        void showNetworkError();

        void showGetProjectTreeSuccess(List<ProjectTreeBranchData> data);

        void showGetProjectTreeFail(CommonException e);
    }


    interface Presenter extends BasePresenter {

        void getProjectTree();
    }

}
