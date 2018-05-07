package com.wan.android.collect;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.CollectData;
import com.wan.android.data.bean.CollectDatas;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.client.CollectListClient;
import com.wan.android.data.client.CollectOtherClient;
import com.wan.android.data.client.UncollectAllClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.Utils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class CollectPresenter implements CollectContract.Presenter {
    private final CollectContract.View mCollectView;

    public CollectPresenter(CollectContract.View collectView) {
        mCollectView = collectView;
        mCollectView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void swipeRefresh() {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(CollectListClient.class)
                .getCollect(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<CollectData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<CollectData> response) {
                        if (response == null) {
                            mCollectView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mCollectView.showSwipeRefreshFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        CollectData data = response.getData();
                        if (data == null || data.getDatas() == null || data.getDatas().size() == 0) {
                            mCollectView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mCollectView.showSwipeRefreshSuccess(data.getDatas());
                    }

                    @Override
                    public void onError(Throwable t) {
                        mCollectView.showSwipeRefreshFail(
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
    public void loadMore(final int currPage) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(CollectListClient.class)
                .getCollect(currPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<CollectData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<CollectData> response) {

                        if (response == null) {
                            mCollectView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mCollectView.showLoadMoreFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        CollectData data = response.getData();
                        if (data == null || data.getDatas() == null) {
                            mCollectView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }

                        mCollectView.showLoadMoreSuccess(data.getDatas());

                        if (currPage + 1 < data.getPagecount()) {
                            mCollectView.showLoadMoreComplete();
                        } else {
                            mCollectView.showLoadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCollectView.showLoadMoreFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString() : Utils.getContext().getString(R.string.load_more_fail)));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }

    @Override
    public void uncollect(int id, int originalId) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(UncollectAllClient.class)
                .uncollectAll(id, originalId)
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
                            mCollectView.showUncollectFail(new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                    : Utils.getContext().getString(R.string.uncollect_failed)));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mCollectView.showUncollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        mCollectView.showUncollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCollectView.showUncollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.uncollect_failed)));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }

    @Override
    public void addCollect(String title, String author, String link) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(CollectOtherClient.class)
                .collectOther(title, author, link)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<CollectDatas>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<CollectDatas> response) {
                        if (response == null) {
                            mCollectView.showAddCollectFail(new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                    : Utils.getContext().getString(R.string.collect_failed)));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mCollectView.showAddCollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        CollectDatas data = response.getData();
                        mCollectView.showAddCollectSuccess(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCollectView.showAddCollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.collect_failed)));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }
}
