package com.wan.android.project;

import com.wan.android.constant.ErrorCodeConstants;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.bean.ProjectTreeBranchData;
import com.wan.android.data.client.ProjectTreeClient;
import com.wan.android.data.source.RetrofitClient;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wzc
 * @date 2018/7/20
 */
public class ProjectPresenter implements ProjectContract.Presenter {

    private ProjectContract.View mView;

    public ProjectPresenter(ProjectContract.View view) {
        mView = view;
    }

    @Override
    public void getProjectTree() {
        RetrofitClient.getInstance()
                .create(ProjectTreeClient.class)
                .getProjectTree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<List<ProjectTreeBranchData>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonResponse<List<ProjectTreeBranchData>> response) {
                        if (response == null) {
                            mView.showGetProjectTreeFail(
                                    CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mView.showGetProjectTreeFail(new CommonException(
                                    response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        List<ProjectTreeBranchData> data = response.getData();
                        if (data == null || data.isEmpty()) {
                            mView.showGetProjectTreeFail(
                                    CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mView.showGetProjectTreeSuccess(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showGetProjectTreeFail(new CommonException(
                                ErrorCodeConstants.CODE_ERROR, e.toString()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void start() {

    }
}
