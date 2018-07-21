package com.wan.android.project;

import com.wan.android.constant.ErrorCodeConstants;
import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.client.CollectClient;
import com.wan.android.data.client.ProjectBranchClient;
import com.wan.android.data.client.UncollectClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wzc
 * @date 2018/7/20
 */
public class ProjectBranchPresenter implements ProjectBranchContract.Presenter {

    private ProjectBranchContract.View mView;

    public ProjectBranchPresenter(ProjectBranchContract.View view) {
        mView = view;
    }

    @Override
    public void swipeRefresh(int cid) {
        RetrofitClient.getInstance()
                .create(ProjectBranchClient.class)
                .getProjectBranch(1, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<ArticleData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonResponse<ArticleData> response) {
                        if (response == null) {
                            mView.showSwipeRefreshFail(
                                    CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mView.showSwipeRefreshFail(new CommonException(
                                    response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        ArticleData data = response.getData();
                        if (data == null || data.getDatas() == null || data.getDatas().isEmpty()) {
                            mView.showSwipeRefreshFail(CommonException.convert(
                                    ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mView.showSwipeRefreshSuccess(data.getDatas());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showSwipeRefreshFail(
                                new CommonException(ErrorCodeConstants.CODE_ERROR, e.toString()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void loadMore(final int currPage, int cid) {
        RetrofitClient.getInstance()
                .create(ProjectBranchClient.class)
                .getProjectBranch(currPage, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<ArticleData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonResponse<ArticleData> response) {
                        if (response == null) {
                            mView.showLoadMoreFail(CommonException.convert(
                                    ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mView.showLoadMoreFail(new CommonException(
                                    response.getErrorcode(),response.getErrormsg()));
                            return;
                        }

                        ArticleData data = response.getData();
                        if (data == null || data.getDatas() == null) {
                            mView.showLoadMoreFail(CommonException.convert(
                                    ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }

                        mView.showLoadMoreSuccess(data.getDatas());

                        int nextPage = currPage + 1;
                        if (nextPage <= data.getPagecount()) {
                            // 不是最后一页
                            mView.showLoadMoreComplete();
                        } else {
                            // 已经是最后一页
                            mView.showLoadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void collect(int id) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(CollectClient.class)
                .collect(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<String> response) {
                        if (response == null) {
                            mView.showCollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mView.showCollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mView.showCollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showCollectFail(new CommonException(ErrorCodeConstants.CODE_ERROR, e.toString()));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }

    @Override
    public void uncollect(int id) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(UncollectClient.class)
                .uncollect(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<String> response) {
                        if (response == null) {
                            mView.showUncollectFail(
                                    CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mView.showUncollectFail(
                                    new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mView.showUncollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showUncollectFail(new CommonException(ErrorCodeConstants.CODE_ERROR, e.toString()));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }

    @Override
    public void start() {

    }
}
