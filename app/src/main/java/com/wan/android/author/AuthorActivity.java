package com.wan.android.author;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wan.android.R;
import com.wan.android.base.BaseActivity;
import com.wan.android.util.ActivityUtils;

/**
 * @author wzc
 * @date 2018/3/30
 */
public class AuthorActivity extends BaseActivity {
    private static final String EXTRA_AUTHOR = "extra_author";
    private AuthorPresenter mAuthorPresenter;

    public static void start(Context context, String author) {
        Intent starter = new Intent(context, AuthorActivity.class);
        starter.putExtra(EXTRA_AUTHOR, author);
        context.startActivity(starter);
    }

    private String mAuthor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_activity);
        if (getIntent() != null) {
            mAuthor = getIntent().getStringExtra(EXTRA_AUTHOR);
        }
        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mAuthor);
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.elevation_value));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AuthorFragment authorFragment = (AuthorFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (authorFragment == null) {
            // Create the fragment
            authorFragment = AuthorFragment.newInstance(mAuthor);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), authorFragment, R.id.contentFrame);
        }

        mAuthorPresenter = new AuthorPresenter(authorFragment);
    }

}
