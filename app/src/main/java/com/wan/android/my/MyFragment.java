package com.wan.android.my;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.base.BaseFragment;
import com.wan.android.collect.MyCollectionActivity;
import com.wan.android.constant.SpConstants;
import com.wan.android.data.bean.LoginMessageEvent;
import com.wan.android.data.bean.LogoutMessageEvent;
import com.wan.android.data.bean.NightModeEvent;
import com.wan.android.funet.FUNetActivity;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.util.NightModeUtils;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.view.MyItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import skin.support.SkinCompatManager;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MyFragment.class.getSimpleName();
    private TextView mTvUserState;
//    private MyItemView mMivLogout;
    private LinearLayout mLinearLayoutUser;
    private MyItemView mMivNightMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_hot).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.my_fragment, null);
        LinearLayout linearLayoutRoot = (LinearLayout) inflate.findViewById(R.id.linearlayout_my);
        mSwipeRefreshLayout.addView(inflate);
        // 设置可下拉刷新的子view
        mSwipeRefreshLayout.setSwipeableChildren(R.id.linearlayout_my);
        mLinearLayoutUser = (LinearLayout) linearLayoutRoot.findViewById(R.id.linearlayout_my_fragment_user);
        mTvUserState = (TextView) linearLayoutRoot.findViewById(R.id.tv_signin_state);
        mLinearLayoutUser.setOnClickListener(this);
        MyItemView mivFavorite = (MyItemView) linearLayoutRoot.findViewById(R.id.myitemview_favorite);
        MyItemView mivMyWebsite = (MyItemView) linearLayoutRoot.findViewById(R.id.myitemview_website);
        mMivNightMode = (MyItemView) linearLayoutRoot.findViewById(R.id.myitemview_night_mode);
        mMivNightMode.post(new Runnable() {
            @Override
            public void run() {
                boolean nightMode = NightModeUtils.isNightMode();
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "init switch nightMode:" + nightMode);
                }
                mMivNightMode.setToggle(nightMode);
            }
        });
        mivFavorite.setOnClickListener(this);
        mivMyWebsite.setOnClickListener(this);
        mMivNightMode.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 1000);
                }
            }
        });

        update();
    }

    private void update() {
        String username = PreferenceUtils.getString(getContext(), SpConstants.KEY_USERNAME, "");

        if (!TextUtils.isEmpty(username)) {
            mTvUserState.setText(username);
            mLinearLayoutUser.setOnClickListener(null);
        } else {
            mTvUserState.setText(R.string.unregistered);
            mLinearLayoutUser.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearlayout_my_fragment_user:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.myitemview_favorite: {
                String username = PreferenceUtils.getString(getContext(), SpConstants.KEY_USERNAME, "");
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(mActivity, R.string.please_login_first, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    MyCollectionActivity.start(getActivity());
                }
                break;
            }
            case R.id.myitemview_website: {
                String username = PreferenceUtils.getString(getContext(), SpConstants.KEY_USERNAME, "");
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(mActivity, R.string.please_login_first, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    FUNetActivity.start(getActivity());
                }
                break;
            }

            case R.id.myitemview_night_mode:
                mMivNightMode.toggle();
                if (mMivNightMode.isToggleOn()) {
                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mActivity.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_night));
                        mActivity.getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary_night));
                    }
                } else {
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mActivity.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                        mActivity.getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
                mSwipeRefreshLayout.setColorSchemeResources(
                        mMivNightMode.isToggleOn()
                                ? new int[]{R.color.colorPrimaryDark_night, R.color.colorPrimary_night}
                                : new int[]{R.color.colorPrimaryDark, R.color.colorPrimary});

                NightModeUtils.setNightMode(mMivNightMode.isToggleOn());

                EventBus.getDefault().post(new NightModeEvent());
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LogoutMessageEvent messageEvent) {
        update();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LoginMessageEvent messageEvent) {
        update();
    }
}
