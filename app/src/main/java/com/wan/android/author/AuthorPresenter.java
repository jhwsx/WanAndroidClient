package com.wan.android.author;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.client.AuthorClient;
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
 * @date 2018/3/30
 */
public class AuthorPresenter implements AuthorContract.Presenter {
    private final AuthorContract.View mAuthorView;

    public AuthorPresenter(AuthorContract.View authorView) {
        mAuthorView = authorView;
        mAuthorView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void swipeRefresh(String author) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(AuthorClient.class)
                .getAuthorFilter(0, author)
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
                            mAuthorView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mAuthorView.showSwipeRefreshFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        ArticleData data = response.getData();
                        if (data == null || data.getDatas() == null || data.getDatas().size() == 0) {
                            mAuthorView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mAuthorView.showSwipeRefreshSuccess(data.getDatas());
                    }

                    @Override
                    public void onError(Throwable t) {
                        mAuthorView.showSwipeRefreshFail(
                                new CommonException(-1, t != null && BuildConfig.DEBUG ? t.toString()
                                        : Utils.getApp().getString(R.string.swipe_refresh_fail)));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }

    @Override
    public void loadMore(final int currPage, String author) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(AuthorClient.class)
                .getAuthorFilter(currPage, author)
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
                            mAuthorView.showLoadMoreFail(
                                    new CommonException(-1, BuildConfig.DEBUG ? Utils.getApp().getString(R.string.response_cannot_be_null)
                                            : Utils.getApp().getString(R.string.load_more_fail)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mAuthorView.showLoadMoreFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        ArticleData data = response.getData();
                        if (data == null || data.getDatas() == null) {
                            mAuthorView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }

                        mAuthorView.showLoadMoreSuccess(data.getDatas());

                        int nextPage = currPage + 1;
                        if (nextPage < data.getPagecount()) {
                            mAuthorView.showLoadMoreComplete();
                        } else {
                            mAuthorView.showLoadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mAuthorView.showLoadMoreFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString() : Utils.getApp().getString(R.string.load_more_fail)));
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
                            mAuthorView.showCollectFail(new CommonException(-1, BuildConfig.DEBUG ? Utils.getApp().getString(R.string.response_cannot_be_null)
                                    : Utils.getApp().getString(R.string.collect_failed)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mAuthorView.showCollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mAuthorView.showCollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DisposableUtil.dispose(disposable[0]);
                        mAuthorView.showCollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getApp().getString(R.string.collect_failed)));
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
                            mAuthorView.showUncollectFail(new CommonException(-1, BuildConfig.DEBUG ? Utils.getApp().getString(R.string.response_cannot_be_null)
                                    : Utils.getApp().getString(R.string.uncollect_failed)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mAuthorView.showUncollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mAuthorView.showUncollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DisposableUtil.dispose(disposable[0]);
                        mAuthorView.showUncollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getApp().getString(R.string.uncollect_failed)));
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }
}
