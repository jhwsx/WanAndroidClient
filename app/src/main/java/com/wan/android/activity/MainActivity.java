package com.wan.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.wan.android.R;
import com.wan.android.bean.LoginMessageEvent;
import com.wan.android.bean.LogoutMessageEvent;
import com.wan.android.constant.SpConstants;
import com.wan.android.fragment.CollectionFragment;
import com.wan.android.fragment.HomeFragment;
import com.wan.android.fragment.TreeFragment;
import com.wan.android.retrofit.CookiesManager;
import com.wan.android.util.PreferenceUtils;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Banner banner;
    private Toolbar mToolbar;
    private Drawer mDrawer;
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private MenuItem menuItem;
    private List<Fragment> mFragments = new ArrayList<>();
    private TextView mTvSignin;
    private TextView mTvSigninState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.elevation_value));
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        View header = LayoutInflater.from(mContext).inflate(R.layout.drawer_header, null);
        mTvSigninState = (TextView) header.findViewById(R.id.tv_signin_state);
        mTvSignin = (TextView) header.findViewById(R.id.tv_signin);
        String usename = PreferenceUtils.getString(mContext, SpConstants.KEY_USERNAME, "");
        if (TextUtils.isEmpty(usename)) {
            mTvSigninState.setText("未登录");
            mTvSignin.setText("点击登录");
            mTvSignin.setTag(1);
        } else {
            mTvSigninState.setText(usename);
            mTvSignin.setText("退出登录");
            mTvSignin.setTag(0);
        }

        mTvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) mTvSignin.getTag();
                if (tag == 0) {
                    mTvSigninState.setText("未登录");
                    mTvSignin.setText("点击登录");
                    mTvSignin.setTag(1);
                    CookiesManager.clearAllCookies();
                    PreferenceUtils.putString(mContext, SpConstants.KEY_USERNAME,"");
                    EventBus.getDefault().post(new LogoutMessageEvent());
                } else if (tag == 1) {
                    startActivityForResult(new Intent(mContext, LoginActivity.class), 1000);
                }
            }
        });
        mDrawer = new DrawerBuilder(this)
                .withHeader(header)
                //this layout have to contain child layouts
                .withRootView(R.id.framelayout_activity_main_container)
                .withToolbar(mToolbar)
                .withSelectedItem(-1)
                .withDisplayBelowStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("常用").withIdentifier(1),
                        new PrimaryDrawerItem().withName("注册").withIdentifier(2)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(mContext, FuActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {

                            }
                            if (intent != null) {
                                startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .build();
        banner = (Banner) findViewById(R.id.banner);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_activity_main);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view_activity_main);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (mDrawer.isDrawerOpen()) {
                    mDrawer.closeDrawer();
                }
                switch (item.getItemId()) {
                    case R.id.tab_home:
                        mViewPager.setCurrentItem(0);
                        mToolbar.setTitle(R.string.app_name);
                        break;
                    case R.id.tab_knowledge_system:
                        mViewPager.setCurrentItem(1);
                        mToolbar.setTitle(R.string.tree);
                        break;
                    case R.id.tab_collection:
                        mViewPager.setCurrentItem(2);
                        mToolbar.setTitle(R.string.collect);
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
        mFragments.add(new CollectionFragment());
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LoginMessageEvent messageEvent) {
        String usename = messageEvent.getUsername();
        mTvSigninState.setText(usename);
        mTvSignin.setText("退出登录");
        mTvSignin.setTag(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = mDrawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        if (requestCode == 1000) {
//            String usename = PreferenceUtils.getString(mContext, SpConstants.KEY_USERNAME, "");
//            mTvSigninState.setText(usename);
//            mTvSignin.setText("退出登录");
//            mTvSignin.setTag(0);
//        }
//    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }

    }
}
