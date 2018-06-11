package com.wan.android.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.wan.android.base.BaseSingleFragmentActivity;

/**
 * @author wzc
 * @date 2018/6/3
 */
public class TucaoActivity extends BaseSingleFragmentActivity {

    private TucaoFragment mTucaoFragment;

    public static void start(Context context) {
        Intent starter = new Intent(context, TucaoActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected Fragment createFragment() {
        mTucaoFragment = TucaoFragment.newInstance();
        return mTucaoFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mTucaoFragment.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
