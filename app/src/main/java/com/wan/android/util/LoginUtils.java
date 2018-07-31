package com.wan.android.util;

import android.text.TextUtils;

import com.wan.android.constant.SpConstants;

/**
 * 判断是否登录工具类
 * @author wzc
 * @date 2018/7/27
 */
public class LoginUtils {
    private LoginUtils() {
        //no instance
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(
                PreferenceUtils.getString(Utils.getApp(), SpConstants.KEY_USERNAME, ""));
    }
}
