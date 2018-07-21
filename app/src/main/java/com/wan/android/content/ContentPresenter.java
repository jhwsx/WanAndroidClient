package com.wan.android.content;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.client.CollectClient;
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
public class ContentPresenter implements ContentContract.Presenter {
    private final ContentContract.View mContentView;

    public ContentPresenter(ContentContract.View contentView) {
        mContentView = contentView;
        mContentView.setPresenter(this);
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
                            mContentView.showCollectFail(new CommonException(-1, BuildConfig.DEBUG ? Utils.getApp().getString(R.string.response_cannot_be_null)
                                    : Utils.getApp().getString(R.string.collect_failed)));
                            return;
                        }
                        if (response.getErrorcode() != 0) {
                            mContentView.showCollectFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        mContentView.showCollectSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DisposableUtil.dispose(disposable[0]);
                        mContentView.showCollectFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getApp().getString(R.string.collect_failed)));
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
