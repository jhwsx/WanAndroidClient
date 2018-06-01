package com.wan.android.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wan.android.base.BaseSingleFragmentActivity;

/**
 * @author wzc
 * @date 2018/5/28
 */
public class SettingsActivity extends BaseSingleFragmentActivity {

    private SettingsFragment mSettingsFragment;
    private SettingsPresenter mSettingsPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the presenter
        mSettingsPresenter = new SettingsPresenter(mSettingsFragment);
    }

    @Override
    protected Fragment createFragment() {
        mSettingsFragment = SettingsFragment.newInstance();
        return mSettingsFragment;
    }
}
