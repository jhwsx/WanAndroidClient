package com.wan.android.util;

import com.wan.android.constant.SpConstants;

/**
 * @author wzc
 * @date 2018/5/21
 */
public class NightModeUtils {

    private NightModeUtils() {
        //no instance
    }

    public static boolean isNightMode() {
        return PreferenceUtils.getBoolean(Utils.getApp(), SpConstants.KEY_NIGHT_MODE, false);
    }

    public static void setNightMode(boolean nightMode) {
        PreferenceUtils.putBoolean(Utils.getApp(), SpConstants.KEY_NIGHT_MODE, nightMode);
    }
}
