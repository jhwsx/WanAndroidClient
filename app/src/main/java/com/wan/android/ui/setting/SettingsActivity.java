package com.wan.android.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wan.android.R;
import com.wan.android.ui.base.BaseSingleFragmentActivity;

/**
 * @author wzc
 * @date 2018/8/28
 */
public class SettingsActivity extends BaseSingleFragmentActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void inject() {

    }

    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mTvTitle.setText(R.string.settings);
    }
}
