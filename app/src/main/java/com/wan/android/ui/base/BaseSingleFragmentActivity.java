package com.wan.android.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wan.android.R;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 单Fragment页面的超类
 *
 * @author wzc
 * @date 2018/1/12
 */

public abstract class BaseSingleFragmentActivity extends BaseActivity {

    @BindView(R.id.toolbar_common)
    Toolbar mToolbar;
    @BindDimen(R.dimen.dp_4)
    int dp4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);
        inject();
        setUnBinder(ButterKnife.bind(this));
        ViewCompat.setElevation(mToolbar, dp4);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container_single_fragment);
        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_single_fragment, createFragment())
                    .commitAllowingStateLoss();
        }
    }

    protected abstract void inject();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract Fragment createFragment();
}
