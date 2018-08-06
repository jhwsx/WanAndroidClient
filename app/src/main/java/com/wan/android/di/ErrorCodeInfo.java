package com.wan.android.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @author wzc
 * @date 2018/8/6
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorCodeInfo {
}
