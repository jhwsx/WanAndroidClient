package com.wan.android.branch;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.client.BranchListClient;
import com.wan.android.data.client.CollectClient;
import com.wan.android.data.client.UncollectClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.Utils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class BranchPresenter implements BranchContract.Presenter {
    private static final String TAG = BranchPresenter.class.getSimpleName();
    private final BranchContract.View mBranchView;

    public BranchPresenter( BranchContract.View homeView) {
        mBranchView = homeView;
        mBranchView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void swipeRefresh(int cid) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(BranchListClient.class)
                .getBranchList(0, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<ArticleData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<ArticleData> response) {

                        if (response == null) {
                            mBranchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mBranchView.showSwipeRefreshFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        ArticleData data = response.getData();
                        if (data == null || data.getDatas() == null || data.getDatas().size() == 0) {
                            mBranchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mBranchView.showSwipeRefreshSuccess(data.getDatas());
                    }

                    @Override
                    public void onError(Throwable t) {
                        mBranchView.showSwipeRefreshFail(
                                new CommonException(-1, t != null && BuildConfig.DEBUG ? t.toString()
                                        : Utils.getContext().getString(R.string.swipe_refresh_fail)));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }

    @Override
    public void loadMore(final int currPage, int cid) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(BranchListClient.class)
                .getBranchList(currPage, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<ArticleData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<ArticleData> response) {

                        if (response == null) {
                            mBranchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mBranchView.showLoadMoreFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        ArticleData data = response.getData();
                        if (data == null || data.getDatas() == null ) {
                            mBranchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }

                        mBranchView.showLoadMoreSuccess(data.getDatas());

                        int nextPage = currPage + 1;
                        if (nextPage < data.getPagecount()) {
                            mBranchView.showLoadMoreComplete();
                        } else {
                            mBranchView.showLoadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBranchView.showLoadMoreFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString() : Utils.getContext().getString(R.string.load_more_fail)));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
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
                            mBranchView.showCollectFail(new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                    : Utils.getContext().getString(R.string.collect_failed)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mBranchView.showCollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mBranchView.showCollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DisposableUtil.dispose(disposable[0]);
                        mBranchView.showCollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.collect_failed)));
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
                            mBranchView.showUncollectFail(new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                    : Utils.getContext().getString(R.string.uncollect_failed)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mBranchView.showUncollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mBranchView.showUncollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DisposableUtil.dispose(disposable[0]);
                        mBranchView.showUncollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.uncollect_failed)));
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }
}
