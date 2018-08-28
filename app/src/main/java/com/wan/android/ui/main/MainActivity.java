package com.wan.android.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wan.android.R;
import com.wan.android.ui.base.BaseActivity;
import com.wan.android.ui.collect.MyCollectionActivity;
import com.wan.android.ui.home.HomeFragment;
import com.wan.android.ui.login.LoginActivity;
import com.wan.android.ui.login.LoginFragment;
import com.wan.android.ui.navigation.NavigationFragment;
import com.wan.android.ui.project.ProjectFragment;
import com.wan.android.ui.search.SearchActivity;
import com.wan.android.ui.tree.TreeFragment;
import com.wan.android.util.BottomNavigationViewHelper;
import com.wan.android.util.constant.AppConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * 主页
 * @author wzc
 * @date 2018/8/3
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainContract.View,
        View.OnClickListener,
        LogoutDialog.OnDialogPositiveBtnClickListener {

    private TextView mTvUsername;
    private View mHeaderLayout;
    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int TAG_HEADER_VIEW = 1;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @BindView(R.id.nav_view_main)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.fl_main_container)
    FrameLayout mFlContainer;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.bottom_navigation_view_main)
    BottomNavigationView mBottomNavigationView;
    @Inject
    MainContract.Presenter<MainContract.View> mPresenter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        setContentView(R.layout.main_activity);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(MainActivity.this);
        initFragmentList();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_main_container, mFragmentList.get(0))
                    .commit();
        }
        setUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Timber.d("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.d("onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d("onSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        Timber.d("onDestroy");
        mPresenter.onDetach();
        super.onDestroy();
    }
    @Override
    protected void setUp() {

        setSupportActionBar(mToolbar);
        initBottomNavigationView();
        initDrawer();
        setUpNavMenu();
        mPresenter.onNavMenuCreated();
    }

    private void initFragmentList() {
        mFragmentList.add(HomeFragment.newInstance());
        mFragmentList.add(TreeFragment.newInstance());
        mFragmentList.add(NavigationFragment.newInstance());
        mFragmentList.add(ProjectFragment.newInstance());
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initBottomNavigationView() {
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.main_bottom_nav_home:
                                switchFragment(AppConstants.TYPE_HOME_FRAGMENT);
                                break;
                            case R.id.main_bottom_nav_tree:
                                switchFragment(AppConstants.TYPE_TREE_FRAGMENT);
                                break;
                            case R.id.main_bottom_nav_navigation:
                                switchFragment(AppConstants.TYPE_NAVIGATION_FRAGMENT);
                                break;
                            case R.id.main_bottom_nav_project:
                                switchFragment(AppConstants.TYPE_PROJECT_FRAGMENT);
                                break;
                            default:
                        }
                        return true;
                    }
                });
    }

    private void setUpNavMenu() {
        mHeaderLayout = mNavigationView.getHeaderView(0);
        mTvUsername = mHeaderLayout.findViewById(R.id.tv_main_header_nav_username);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_main_search) {
            SearchActivity.start(MainActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_logout:
                FragmentManager fm = getSupportFragmentManager();
                LogoutDialog logoutDialog = new LogoutDialog();
                logoutDialog.show(fm, LogoutDialog.class.getSimpleName());
                break;
            case R.id.nav_my_collect:
                MyCollectionActivity.start(MainActivity.this);
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            // unlogined
            mTvUsername.setText(R.string.unlogined);
            // set head click listener
            mHeaderLayout.setTag(TAG_HEADER_VIEW);
            mHeaderLayout.setOnClickListener(this);
            // hide logout
            mNavigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        } else {
            // logined
            mTvUsername.setText(username);
            // cancel head click listener
            mHeaderLayout.setOnClickListener(null);
            // show logout
            mNavigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
        }
    }

    @Override
    public void showLogoutSuccess() {
        showMessage(R.string.logout_success);
        updateUsername(null);
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        if (tag == TAG_HEADER_VIEW) {
            // open login
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_CODE_LOGIN);
        }
    }
    private int mLastPosition;
    private void switchFragment(int position) {
        Fragment targetFragment = mFragmentList.get(position);
        Fragment lastFragment = mFragmentList.get(mLastPosition);
        if (position == mLastPosition) {
            // 为同一个位置, 不用切换
            return;
        }
        mLastPosition = position;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 隐藏上一个 Fragment
        fragmentTransaction.hide(lastFragment);
        // 判断目标 Fragment 是否已经添加
        if (!targetFragment.isAdded()) {
            // 没有添加, 先添加
            fragmentTransaction.add(R.id.fl_main_container, targetFragment);
        } else {
            // 已经添加, 就显示
            fragmentTransaction.show(targetFragment);
        }
        // 提交事务
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_LOGIN) {
            String username = data.getStringExtra(LoginFragment.EXTRA_USER_NAME);
            updateUsername(username);
        }
    }

    @Override
    public void onDialogPositiveBtnClicked() {
        mPresenter.logout();
    }
}
