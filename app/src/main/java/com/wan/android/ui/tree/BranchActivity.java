package com.wan.android.ui.tree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wan.android.R;
import com.wan.android.data.network.model.BranchData;
import com.wan.android.data.network.model.PageData;
import com.wan.android.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 知识体系下的 分支 页面
 *
 * @author wzc
 * @date 2018/8/23
 */
public class BranchActivity extends BaseActivity {
    private static final String EXTRA_BRANCH_DATA = "com.wan.android.extra_branch_data";

    public static void start(Context context, BranchData data) {
        Intent starter = new Intent(context, BranchActivity.class);
        starter.putExtra(EXTRA_BRANCH_DATA, data);
        context.startActivity(starter);
    }

    @BindView(R.id.toolbar_activity_branch)
    Toolbar mToolbar;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private List<PageData> mPageData = new ArrayList<>();
    private BranchData mBranchData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_activity);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        setUp();
    }

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            mBranchData = (BranchData) getIntent().getSerializableExtra(EXTRA_BRANCH_DATA);
        } else {
            finish();
            return;
        }
        mToolbar.setTitle(mBranchData.getName());
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ArrayList<BranchData.Leaf> leaves = mBranchData.getChildren();
        for (BranchData.Leaf leaf : leaves) {
            mPageData.add(new PageData(leaf.getName(), BranchFragment.newInstance(leaf.getId())));
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mPageData.get(position).getFragment();
            }

            @Override
            public int getCount() {
                return mPageData.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mPageData.get(position).getTitle();
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
