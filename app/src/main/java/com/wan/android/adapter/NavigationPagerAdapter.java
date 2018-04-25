package com.wan.android.adapter;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wan.android.data.bean.NavigationData;
import com.wan.android.navigate.NavigationListFragment;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * @author wzc
 * @date 2018/3/20
 */
public class NavigationPagerAdapter extends FragmentPagerAdapter implements TabAdapter {
    private List<NavigationData> mList = new ArrayList<>();
    private List<NavigationListFragment> mFragments = new ArrayList<>();
    public NavigationPagerAdapter(FragmentManager fragmentManager, List<NavigationData> list, List<NavigationListFragment> fragments) {
        super(fragmentManager);
        mList.clear();
        mList.addAll(list);
        mFragments.clear();
        mFragments.addAll(fragments);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ITabView.TabBadge getBadge(int position) {
        return null;
    }

    @Override
    public ITabView.TabIcon getIcon(int position) {
        return null;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getName();
    }

    @Override
    public TabView.TabTitle getTitle(int position) {

        return new TabView.TabTitle.Builder()
                .setContent(mList.get(position).getName())
                .setTextColor(Color.WHITE, 0xBBFFFFFF)
                .build();
    }

    @Override
    public int getBackground(int position) {
        return 0;
    }
}
