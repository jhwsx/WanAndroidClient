package com.wan.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wan.android.R;
import com.wan.android.bean.PageModel;
import com.wan.android.bean.TreeListResponse;
import com.wan.android.fragment.CommonUseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wzc
 * @date 2018/2/11
 */
public class BranchActivity extends BaseActivity {
    private static final String TAG = BranchActivity.class.getSimpleName();
    private static final String EXTRA_BRANCH_TITLE = "EXTRA_BRANCH_TITLE";
    private static final String EXTRA_CHILDREN_LIST = "EXTRA_CHILDREN_LIST";
    private String mTitle;
    private ArrayList<TreeListResponse.Data.Children> mChildren;

    public static void start(Context context, String title, ArrayList<TreeListResponse.Data.Children> children) {
        Intent starter = new Intent(context, BranchActivity.class);
        starter.putExtra(EXTRA_BRANCH_TITLE, title);
        starter.putExtra(EXTRA_CHILDREN_LIST, children);
        context.startActivity(starter);
    }
    private List<PageModel> mPageModels = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra(EXTRA_BRANCH_TITLE);
            mChildren = (ArrayList<TreeListResponse.Data.Children>) getIntent().getSerializableExtra(EXTRA_CHILDREN_LIST);
            Log.d(TAG, "onCreate: ");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_branch);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.tv_activity_branch_title);
        tvTitle.setText(mTitle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for (int i = 0; i < mChildren.size(); i++) {
            TreeListResponse.Data.Children children = mChildren.get(i);

            mPageModels.add(new PageModel(children.getName(), CommonUseFragment.newInstance(children.getId())));
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mPageModels.get(position).getCommonUseFragment();
            }

            @Override
            public int getCount() {
                return mPageModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mPageModels.get(position).getTabTitle();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
