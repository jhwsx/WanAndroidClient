package com.wan.android.loginregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;

import com.wan.android.R;
import com.wan.android.base.BaseActivity;
import com.wan.android.util.ActivityUtils;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class LoginActivity extends BaseActivity {

    private LoginPresenter mLoginPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.elevation_value));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LoginFragment loginFragment =
                (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (loginFragment == null) {
            // Create the fragment
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginFragment, R.id.contentFrame);
        }

        // Create the presenter
        mLoginPresenter = new LoginPresenter(loginFragment);
    }
}
