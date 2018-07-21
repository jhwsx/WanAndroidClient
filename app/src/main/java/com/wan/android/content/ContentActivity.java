package com.wan.android.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.wan.android.R;
import com.wan.android.base.BaseActivity;
import com.wan.android.util.ActivityUtils;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class ContentActivity extends BaseActivity implements OnReceivedTitleListener {
    private static final String EXTRA_CONTENT_URL = "extra_content_url";
    private static final String EXTRA_ID = "extra_id";
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_FROM_TYPE = "extra_from_type";
    private Toolbar mToolbar;
    private ContentFragment mContentFragment;
    private ContentPresenter mContentPresenter;


    public static void start(Context context, String title, String url, int id) {
        Intent starter = new Intent(context, ContentActivity.class);
        starter.putExtra(EXTRA_CONTENT_URL, url);
        starter.putExtra(EXTRA_TITLE, title);
        starter.putExtra(EXTRA_ID, id);
        context.startActivity(starter);
    }

    public static void start(Context context, int fromType, String title, String url, int id) {
        Intent starter = new Intent(context, ContentActivity.class);
        starter.putExtra(EXTRA_FROM_TYPE, fromType);
        starter.putExtra(EXTRA_CONTENT_URL, url);
        starter.putExtra(EXTRA_TITLE, title);
        starter.putExtra(EXTRA_ID, id);
        context.startActivity(starter);
    }

    private String mUrl;
    private int mId;
    private String mTitle;
    private int mFromType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_act);
        if (getIntent() != null) {
            mFromType = getIntent().getIntExtra(EXTRA_FROM_TYPE, 0);
            mUrl = getIntent().getStringExtra(EXTRA_CONTENT_URL);
            mId = getIntent().getIntExtra(EXTRA_ID, -1);
            mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        }
        // Set up the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.loading);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.elevation_value));
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mContentFragment = (ContentFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mContentFragment == null) {
            // Create the fragment
            mContentFragment = ContentFragment.newInstance(mFromType, mTitle, mUrl, mId);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mContentFragment, R.id.contentFrame);
        }

        // Create the presenter
        mContentPresenter = new ContentPresenter(mContentFragment);
    }

    @Override
    public void onReceiveTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mContentFragment.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
