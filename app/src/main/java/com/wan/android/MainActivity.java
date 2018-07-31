package com.wan.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;
import com.wan.android.base.BaseActivity;
import com.wan.android.constant.DefaultConstants;
import com.wan.android.data.event.NavigationEvent;
import com.wan.android.data.event.NightModeEvent;
import com.wan.android.data.event.ProjectEvent;
import com.wan.android.friend.FriendActivity;
import com.wan.android.home.HomeFragment;
import com.wan.android.my.MyFragment;
import com.wan.android.navigate.NavigationFragment;
import com.wan.android.project.ProjectFragment;
import com.wan.android.search.SearchActivity;
import com.wan.android.setting.SettingsActivity;
import com.wan.android.tree.TreeFragment;
import com.wan.android.util.BottomNavigationViewHelper;
import com.wan.android.util.EdgeEffectUtils;
import com.wan.android.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页面
 *
 * @author wzc
 * @date 2018/3/10
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.viewpager_activity_main)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation_view_activity_main)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.toolbar_activity_main)
    Toolbar mToolbar;
    @BindDimen(R.dimen.elevation_value)
    int mElevationValue;
    private MenuItem menuItem;
    private List<Fragment> mFragmentList = new ArrayList<>();
    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        ViewCompat.setElevation(mToolbar, mElevationValue);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        EventBus.getDefault().register(this);
        EdgeEffectUtils.setViewPagerEdgeEffect(mViewPager);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.tab_home:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.tab_tree:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.tab_navigation:
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.tab_project:
                    mViewPager.setCurrentItem(3);
                    break;
                case R.id.tab_my:
                    mViewPager.setCurrentItem(4);
                    break;
                default:
                    break;
            }
            return false;
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = mBottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }
        });

        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new TreeFragment());
        mFragmentList.add(new NavigationFragment());
        mFragmentList.add(ProjectFragment.newInstance());
        mFragmentList.add(new MyFragment());
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }
        };

        mViewPager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                SearchActivity.start(mContext);
                return true;
            case R.id.action_hot:
                FriendActivity.start(mContext);
                return true;
            case R.id.action_settings:
                SettingsActivity.start(mContext);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > DefaultConstants.ONE_CLICK_TIME_LIMIT) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                String string = String.format(getString(R.string.main_exit_text), getString(R.string.app_name));
                ToastUtils.showShort(string);
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                finish();
            }
            // 返回true表示事件不再传递了
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void nightModeEvent(NightModeEvent nightModeEvent) {
        Logger.d("nightModeEvent");
        EdgeEffectUtils.setViewPagerEdgeEffect(mViewPager);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void navigationEvent(NavigationEvent event) {
        mViewPager.setCurrentItem(2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void projectEvent(ProjectEvent event) {
        mViewPager.setCurrentItem(3);
    }
}
