package com.wan.android.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.wan.android.R;
import com.wan.android.fragment.CollectionFragment;
import com.wan.android.fragment.HomeFragment;
import com.wan.android.fragment.TreeFragment;
import com.youth.banner.Banner;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.elevation_value));
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);


        mDrawer = new DrawerBuilder(this)
                .withHeader(R.layout.drawer_header)
                //this layout have to contain child layouts
                .withRootView(R.id.framelayout_activity_main_container)
                .withToolbar(mToolbar)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .build();
        banner = (Banner) findViewById(R.id.banner);
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
                    case R.id.tab_collection:
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
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = mDrawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}
