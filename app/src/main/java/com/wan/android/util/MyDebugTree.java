package com.wan.android.util;

import timber.log.Timber;

/**
 * Timber 的 MyDebugTree
 * @author wzc
 * @date 2018/6/20
 */
public class MyDebugTree extends Timber.DebugTree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (tag != null) {
            String threadName = Thread.currentThread().getName();
            tag = "<" + threadName + "> " + tag;
        }
        super.log(priority, tag, message, t);
    }
    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return super.createStackElementTag(element) + "(Line " + element.getLineNumber() + ")";
    }
}