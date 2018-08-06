package com.wan.android.ui.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wan.android.ui.main.MainActivity;

/**
 * 闪屏页
 * @author wzc
 * @date 2018/8/2
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.start(this);
        finish();
    }
}
