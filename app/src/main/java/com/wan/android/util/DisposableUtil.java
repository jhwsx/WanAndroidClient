package com.wan.android.util;

import io.reactivex.disposables.Disposable;

/**
 * 取消订阅工具类
 *
 * @author wzc
 * @date 2018/5/7
 */
public class DisposableUtil {

    private DisposableUtil() {
        //no instance
    }

    public static  void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
