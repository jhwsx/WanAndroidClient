package com.wan.android.search;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.bean.HotkeyData;
import com.wan.android.data.client.CollectClient;
import com.wan.android.data.client.HotkeyClient;
import com.wan.android.data.client.SearchClient;
import com.wan.android.data.client.UncollectClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.Utils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class SearchPresenter implements SearchContract.Presenter {
    private final SearchContract.View mSearchView;

    public SearchPresenter(SearchContract.View searchView) {
        mSearchView = searchView;
        mSearchView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void fetchHotkey() {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(HotkeyClient.class)
                .getHotkey()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<List<HotkeyData>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<List<HotkeyData>> response) {
                        if (response == null) {
                            mSearchView.showHotkeyFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mSearchView.showHotkeyFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        final List<HotkeyData> dataList = response.getData();
                        if (dataList == null) {
                            mSearchView.showHotkeyFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }

                        mSearchView.showHotkeySuccess(dataList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSearchView.showHotkeyFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.fetch_hotkey_fail)));
                        DisposableUtil.dispose(disposable[0]);
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }

    @Override
    public void swipeRefresh(String query) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(SearchClient.class)
                .search(0, query)
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
                            mSearchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mSearchView.showSwipeRefreshFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        ArticleData data = response.getData();
                        if (data == null || data.getDatas() == null || data.getDatas().size() == 0) {
                            mSearchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mSearchView.showSwipeRefreshSuccess(data.getDatas());
                    }

                    @Override
                    public void onError(Throwable t) {
                        mSearchView.showSwipeRefreshFail(
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
    public void loadMore(final int currPage, String query) {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(SearchClient.class)
                .search(currPage, query)
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
                            mSearchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mSearchView.showLoadMoreFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        ArticleData data = response.getData();
                        if (data == null || data.getDatas() == null) {
                            mSearchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mSearchView.showLoadMoreSuccess(data.getDatas());

                        if (currPage + 1 < data.getPagecount()) {
                            mSearchView.showLoadMoreComplete();
                        } else {
                            mSearchView.showLoadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSearchView.showLoadMoreFail(
                                new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                        : Utils.getContext().getString(R.string.load_more_fail)));
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
                            mSearchView.showCollectFail(
                                    new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                    : Utils.getContext().getString(R.string.collect_failed)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mSearchView.showCollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mSearchView.showCollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSearchView.showCollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
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
                            mSearchView.showUncollectFail(
                                    new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                    : Utils.getContext().getString(R.string.uncollect_failed)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mSearchView.showUncollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mSearchView.showUncollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSearchView.showUncollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
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
