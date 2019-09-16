package com.wan.android.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.wan.android.R;
import com.wan.android.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索页面
 *
 * @author wzc
 * @date 2018/8/21
 */
public class SearchActivity extends BaseActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    public static void start(Context context) {
        Intent starter = new Intent(context, SearchActivity.class);
        context.startActivity(starter);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(SearchFragment.TAG) == null) {
            fm.beginTransaction()
                    .add(R.id.contentFrame, SearchFragment.newInstance(), SearchFragment.TAG)
                    .commit();
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setUp() {

    }
}
