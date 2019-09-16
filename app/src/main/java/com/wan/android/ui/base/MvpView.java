package com.wan.android.ui.base;

import androidx.annotation.StringRes;

/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface MvpView {

    /**
     * 显示加载进度
     */
    void showLoading();

    /**
     * 隐藏加载进度
     */
    void hideLoading();

    /**
     * 显示错误信息
     *
     * @param resId 资源 id
     */
    void onError(@StringRes int resId);

    /**
     * 显示错误信息
     *
     * @param message 信息
     */
    void onError(String message);

    /**
     * 显示提示信息
     *
     * @param message 信息
     */
    void showMessage(String message);

    /**
     * 显示提示信息
     *
     * @param resId 资源 id
     */
    void showMessage(@StringRes int resId);

    /**
     * 网络是否连接
     *
     * @return boolean true 已连接, false 未连接
     */
    boolean isNetworkConnected();

    /**
     * 关闭软键盘
     */
    void hideKeyboard();

}