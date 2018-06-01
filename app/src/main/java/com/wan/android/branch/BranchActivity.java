package com.wan.android.branch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.wan.android.BasePresenter;
import com.wan.android.R;
import com.wan.android.base.BaseActivity;
import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.PageModel;
import com.wan.android.util.EdgeEffectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分支页面
 *
 * @author wzc
 * @date 2018/2/11
 */
public class BranchActivity extends BaseActivity {
    private static final String TAG = BranchActivity.class.getSimpleName();
    private static final String EXTRA_BRANCH_TITLE = "EXTRA_BRANCH_TITLE";
    private static final String EXTRA_CHILDREN_LIST = "EXTRA_CHILDREN_LIST";
    private String mTitle;
    private ArrayList<BranchData.Leaf> mChildren;
    private List<BasePresenter> mPresenterList = new ArrayList<>();
    public static void start(Context context, String title, ArrayList<BranchData.Leaf> children) {
        Intent starter = new Intent(context, BranchActivity.class);
        starter.putExtra(EXTRA_BRANCH_TITLE, title);
        starter.putExtra(EXTRA_CHILDREN_LIST, children);
        context.startActivity(starter);
    }
    private List<PageModel> mPageModels = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_activity);
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra(EXTRA_BRANCH_TITLE);
            mChildren = (ArrayList<BranchData.Leaf>) getIntent().getSerializableExtra(EXTRA_CHILDREN_LIST);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_branch);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.tv_activity_branch_title);
        tvTitle.setText(mTitle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for (int i = 0; i < mChildren.size(); i++) {
            BranchData.Leaf children = mChildren.get(i);

            BranchFragment branchFragment = BranchFragment.newInstance(children.getId());
            mPageModels.add(new PageModel(children.getName(), branchFragment));
            mPresenterList.add(new BranchPresenter(branchFragment));
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        EdgeEffectUtils.setViewPagerEdgeEffect(viewPager);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mPageModels.get(position).getBranchFragment();
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
