package com.wan.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wan.android.base.BaseActivity;

/**
 * 闪屏页
 * @author wzc
 * @date 2018/3/9
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }
}
