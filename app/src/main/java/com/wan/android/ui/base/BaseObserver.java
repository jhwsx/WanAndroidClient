package com.wan.android.ui.base;

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
        mView.onError(e.getMessage());
        mView.hideLoading();
    }

    @Override
    public void onComplete() {
        // do nothing
    }
}
