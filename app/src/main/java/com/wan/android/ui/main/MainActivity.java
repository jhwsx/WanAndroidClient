package com.wan.android.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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
import com.wan.android.ui.home.HomeFragment;
import com.wan.android.ui.login.LoginActivity;
import com.wan.android.ui.login.LoginFragment;
import com.wan.android.ui.navigation.NavigationFragment;
import com.wan.android.ui.project.ProjectFragment;
import com.wan.android.ui.tree.TreeFragment;
import com.wan.android.util.BottomNavigationViewHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(MainActivity.this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_main_container, HomeFragment.newInstance())
                    .commit();
        }
        setUp();
    }

    @Override
    protected void setUp() {
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.main_bottom_nav_home:
                                switchFragment(0);
                                break;
                            case R.id.main_bottom_nav_tree:
                                switchFragment(1);
                                break;
                            case R.id.main_bottom_nav_navigation:
                                switchFragment(2);
                                break;
                            case R.id.main_bottom_nav_project:
                                switchFragment(3);
                                break;
                            default:
                        }
                        return true;
                    }
                });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setUpNavMenu();
        mPresenter.onNavMenuCreated();
    }

    private void setUpNavMenu() {
        mHeaderLayout = mNavigationView.getHeaderView(0);
        mTvUsername = mHeaderLayout.findViewById(R.id.tv_main_header_nav_username);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
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
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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

    private void switchFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                fragmentTransaction.replace(R.id.fl_main_container, HomeFragment.newInstance());
                break;
            case 1:
                fragmentTransaction.replace(R.id.fl_main_container, TreeFragment.newInstance());
                break;
            case 2:
                fragmentTransaction.replace(R.id.fl_main_container, NavigationFragment.newInstance());
                break;
            case 3:
                fragmentTransaction.replace(R.id.fl_main_container, ProjectFragment.newInstance());
                break;
            default:
                fragmentTransaction.replace(R.id.fl_main_container, HomeFragment.newInstance());
        }
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
