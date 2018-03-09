package com.wan.android.util;

import android.content.Context;

import com.wan.android.WanAndroidApplication;

/**
 * @author wzc
 * @date 2018/3/2
 */
public class Utils {
    private Utils() {
        //no instance
    }

    public static Context getContext() {
        return WanAndroidApplication.getContext();
    }
}
