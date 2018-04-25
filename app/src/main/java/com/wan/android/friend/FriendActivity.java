package com.wan.android.friend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wan.android.R;
import com.wan.android.base.BaseActivity;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.ActivityUtils;

/**
 * 常用页面
 *
 * @author wzc
 * @date 2018/2/24
 */
public class FriendActivity extends BaseActivity {

    private FriendPresenter mFriendPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, FriendActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_activity);
        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.elevation_value));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FriendFragment friendFragment = (FriendFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (friendFragment == null) {
            // Create the fragment
            friendFragment = FriendFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), friendFragment, R.id.contentFrame);
        }

        mFriendPresenter = new FriendPresenter(RetrofitClient.getInstance(), friendFragment);
    }

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
}
