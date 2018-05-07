package com.wan.android.home;

import android.util.Log;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.BannerData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.client.BannerClient;
import com.wan.android.data.client.CollectClient;
import com.wan.android.data.client.HomeListClient;
import com.wan.android.data.client.UncollectClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.Utils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link HomeFragment}), retrieves the data and updates the
 * UI as required.
 */
public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = HomePresenter.class.getSimpleName();
    private final HomeContract.View mHomeView;


    public HomePresenter(HomeContract.View homeView) {
        mHomeView = checkNotNull(homeView, "homeView cannot be null");
        mHomeView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void swipeRefresh() {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(HomeListClient.class)
                .getHomeFeed(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommonResponse<ArticleData>>() {
                    @Override
                    public void accept(CommonResponse<ArticleData> response) throws Exception {

                        if (response == null) {
                            mHomeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mHomeView.showSwipeRefreshFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        ArticleData data = response.getData();
                        // data.getDatas().size() == 0 的判断，为 true，表示没有数据
                        if (data == null || data.getDatas() == null || data.getDatas().size() == 0) {
                            mHomeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mHomeView.showSwipeRefreshSuccess(data.getDatas());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        mHomeView.showSwipeRefreshFail(
                                new CommonException(-1, t != null && BuildConfig.DEBUG ? t.toString()
                                        : Utils.getContext().getString(R.string.swipe_refresh_fail)));
                        DisposableUtil.dispose(disposable[0]);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        DisposableUtil.dispose(disposable[0]);
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable d) throws Exception {
                        disposable[0] = d;
                    }
                });
    }

    @Override
    public void loadMore(final int currPage) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(HomeListClient.class)
                .getHomeFeed(currPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<ArticleData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<ArticleData> response) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "loadMore onNext()  response: " + response);
                        }

                        if (response == null) {
                            mHomeView.showLoadMoreFail(
                                    new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                            : Utils.getContext().getString(R.string.load_more_fail)));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mHomeView.showLoadMoreFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        ArticleData data = response.getData();
                        // data.getDatas().size() == 0 的判断不可以加，加载到最后一页时，就没有数据，但是不应该返回没有数据的异常。
                        if (data == null || data.getDatas() == null) {
                            mHomeView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }

                        mHomeView.showLoadMoreSuccess(data.getDatas());

                        int nextPage = currPage + 1;
                        if (nextPage < data.getPagecount()) {
                            // 不是最后一页
                            mHomeView.showLoadMoreComplete();
                        } else {
                            // 已经是最后一页
                            mHomeView.showLoadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "loadMore onError()", e);
                        }
                        mHomeView.showLoadMoreFail(
                                new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                        : Utils.getContext().getString(R.string.load_more_fail)));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        // 在这里取消订阅，而不在 onNext() 中，因为这里在 onNext() 之后调用；
                        // 如果在 onNext() 中取消订阅的话，这个 onComplete() 方法就不会被调用了。
                        DisposableUtil.dispose(disposable[0]);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "loadMore onComplete()");
                        }
                    }
                });
    }

    @Override
    public void fetchBanner() {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(BannerClient.class)
                .getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<List<BannerData>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<List<BannerData>> response) {

                        if (response == null) {
                            mHomeView.showBannerFail(
                                    new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                            : Utils.getContext().getString(R.string.fetch_banner_fail)));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mHomeView.showBannerFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        if (response.getData() == null || response.getData().size() == 0) {
                            mHomeView.showBannerFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mHomeView.showBannerSuccess(response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        DisposableUtil.dispose(disposable[0]);
                        mHomeView.showBannerFail(
                                new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                        : Utils.getContext().getString(R.string.fetch_banner_fail)));
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
                            mHomeView.showCollectFail(new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                    : Utils.getContext().getString(R.string.collect_failed)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mHomeView.showCollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mHomeView.showCollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mHomeView.showCollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.collect_failed)));
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
                            mHomeView.showUncollectFail(new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                    : Utils.getContext().getString(R.string.uncollect_failed)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mHomeView.showUncollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mHomeView.showUncollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mHomeView.showUncollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.uncollect_failed)));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }
}
