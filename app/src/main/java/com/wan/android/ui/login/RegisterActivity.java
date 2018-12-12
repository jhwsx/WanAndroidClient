package com.wan.android.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wan.android.R;
import com.wan.android.ui.base.BaseSingleFragmentActivity;

/**
 * 注册页面
 *
 * @author wzc
 * @date 2018/3/27
 */
public class RegisterActivity extends BaseSingleFragmentActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    public static void start(Context context) {
        Intent starter = new Intent(context, RegisterActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void inject() {
        getActivityComponent().inject(RegisterActivity.this);
    }

    @Override
    protected Fragment createFragment() {
        return RegisterFragment.newInstance();
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
        // do nothing
    }

    @Override
    protected void initTitle() {
        mTvTitle.setText(R.string.register);
    }
}
