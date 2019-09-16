package com.wan.android.ui.setting;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.wan.android.R;
import com.wan.android.ui.base.BaseSingleFragmentActivity;

/**
 * @author wzc
 * @date 2018/8/28
 */
public class SettingsActivity extends BaseSingleFragmentActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
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
    protected boolean hasFragment() {
        return true;
    }

    @Override
    protected String getActivityName() {
        return TAG;
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
