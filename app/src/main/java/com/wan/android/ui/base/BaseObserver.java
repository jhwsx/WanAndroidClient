package com.wan.android.ui.base;

import com.wan.android.R;

import java.net.UnknownHostException;

import io.reactivex.observers.ResourceObserver;

/**
 * @author wzc
 * @date 2018/8/6
 */
public class BaseObserver<T> extends ResourceObserver<T> {
    private MvpView mView;

    public BaseObserver(MvpView view) {
        mView = view;
    }

    @Override
    public void onNext(T t) {
        // do nothing here
    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        if (e instanceof UnknownHostException) {
            mView.onError(R.string.error_msg_network_error);
        } else {
            mView.onError(e.getMessage());
        }
        mView.hideLoading();
    }

    @Override
    public void onComplete() {
        // do nothing
    }
}
