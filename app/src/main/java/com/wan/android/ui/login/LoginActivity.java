package com.wan.android.ui.login;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.wan.android.R;
import com.wan.android.ui.base.BaseSingleFragmentActivity;

/**
 * 登录页面
 *
 * @author wzc
 * @date 2018/8/3
 */
public class LoginActivity extends BaseSingleFragmentActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
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

    @Override
    protected void initTitle() {
        mTvTitle.setText(R.string.login);
    }

    @Override
    protected boolean hasFragment() {
        return true;
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }
}
