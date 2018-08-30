package com.wan.android.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.wan.android.R;
import com.wan.android.ui.base.BaseSingleFragmentActivity;

/**
 * 吐个槽页面
 *
 * @author wzc
 * @date 2018/6/3
 */
public class RoastActivity extends BaseSingleFragmentActivity {

    private RoastFragment mRoastFragment;

    public static void start(Context context) {
        Intent starter = new Intent(context, RoastActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected Fragment createFragment() {
        mRoastFragment = RoastFragment.newInstance();
        return mRoastFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mRoastFragment.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mTvTitle.setText(R.string.tucao);
    }
}
