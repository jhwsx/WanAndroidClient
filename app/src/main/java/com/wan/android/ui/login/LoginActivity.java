package com.wan.android.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wan.android.ui.base.BaseSingleFragmentActivity;

/**
 * 登录页面
 * @author wzc
 * @date 2018/8/3
 */
public class LoginActivity extends BaseSingleFragmentActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void inject() {
        getActivityComponent().inject(LoginActivity.this);
    }

    @Override
    protected Fragment createFragment() {
        return LoginFragment.newInstance();
    }

    @Override
    protected void setUp() {
        // do nothing
    }
}
