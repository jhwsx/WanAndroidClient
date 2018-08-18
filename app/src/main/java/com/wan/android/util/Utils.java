package com.wan.android.util;

import android.content.Context;

import com.wan.android.App;

/**
 * @author wzc
 * @date 2018/3/2
 */
public class Utils {
    private Utils() {
        //no instance
    }

    public static Context getApp() {
        return App.getContext();
    }
}
