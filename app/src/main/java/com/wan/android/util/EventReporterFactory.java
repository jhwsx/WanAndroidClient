package com.wan.android.util;

import com.wan.android.listener.EventReporterWrapper;

/**
 * @author wzc
 * @date 2018/6/2
 */
public class EventReporterFactory {

    private static volatile EventReporterWrapper sInstance;

    private EventReporterFactory() {

    }

    /**
     * 获取图片加载器
     *
     * @return
     */
    public static EventReporterWrapper getReporter() {
        if (sInstance == null) {
            synchronized (EventReporterFactory.class) {
                if (sInstance == null) {
                    sInstance = new UmengEventReporter();
                }
            }
        }
        return sInstance;
    }
}
