package com.wan.android.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wan.android.R;
import com.wan.android.constant.DefaultConstants;
import com.wan.android.fragment.HomeFragment;
import com.wan.android.fragment.MyFragment;
import com.wan.android.fragment.TreeFragment;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Banner banner;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private MenuItem menuItem;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.elevation_value));
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_activity_main);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view_activity_main);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.tab_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_knowledge_system:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_my:
                        mViewPager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
                return false;
            }
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

        mFragments.add(new HomeFragment());
        mFragments.add(new TreeFragment());
        mFragments.add(new MyFragment());
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };

        mViewPager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hot:
                FuActivity.start(mContext);
                return true;
            case R.id.action_search:
                SearchActivity.start(mContext);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                Toast.makeText(this, R.string.exit_after_one_more_click, Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);
            }
            // 返回true表示事件不再传递了
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
