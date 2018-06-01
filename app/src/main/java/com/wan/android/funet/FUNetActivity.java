package com.wan.android.funet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wan.android.base.BaseSingleFragmentActivity;

/**
 * 我的网站
 * @author wzc
 * @date 2018/5/31
 */
public class FUNetActivity extends BaseSingleFragmentActivity {
    private FUNetFragment mFUNetFragment;
    private FUNetPresenter mFUNetPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, FUNetActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the presenter
        mFUNetPresenter = new FUNetPresenter(mFUNetFragment);
    }

    @Override
    protected Fragment createFragment() {
        mFUNetFragment = FUNetFragment.newInstance();
        return mFUNetFragment;
    }
}
