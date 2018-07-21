package com.wan.android.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.wan.android.BasePresenter;
import com.wan.android.R;
import com.wan.android.base.BaseFragment;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.PageModel;
import com.wan.android.data.bean.ProjectTreeBranchData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wzc
 * @date 2018/7/20
 */
public class ProjectFragment extends BaseFragment
        implements ProjectContract.View {

    private ProjectPresenter mPresenter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static ProjectFragment newInstance() {
        Bundle args = new Bundle();
        ProjectFragment fragment = new ProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProjectPresenter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initData();
    }

    private void initData() {
        mPresenter.getProjectTree();
    }

    private void initViews(View view) {
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.project_fragment, null);
        mSwipeRefreshLayout.addView(inflate);
        // 设置可下拉刷新的子view
        mTabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewpager);
    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showGetProjectTreeSuccess(List<ProjectTreeBranchData> data) {
        final ArrayList<PageModel> pageModels = new ArrayList<>(10);
        for (int i = 0; i < data.size(); i++) {
            ProjectTreeBranchData element = data.get(i);
            pageModels.add(new PageModel(element.getName(), ProjectBranchFragment.newInstance(element)));
        }

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return pageModels.get(position).getFragment();
            }

            @Override
            public int getCount() {
                return pageModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pageModels.get(position).getTabTitle();
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void showGetProjectTreeFail(CommonException e) {

    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }
}
