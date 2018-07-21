package com.wan.android.util;

import com.umeng.analytics.MobclickAgent;
import com.wan.android.listener.EventReporterWrapper;

/**
 * @author wzc
 * @date 2018/6/2
 */
public class UmengEventReporter implements EventReporterWrapper {

    @Override
    public void report(String event) {
        MobclickAgent.onEvent(Utils.getApp(), event);
    }
}
