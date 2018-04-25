package com.wan.android.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wan.android.R;
import com.wan.android.collect.MyCollectionActivity;
import com.wan.android.data.bean.LoginMessageEvent;
import com.wan.android.data.bean.LogoutMessageEvent;
import com.wan.android.constant.SpConstants;
import com.wan.android.base.BaseFragment;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.view.MyItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvUserState;
    private MyItemView mMivLogout;
    private LinearLayout mLinearLayoutUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.collection_fragment, null);
        LinearLayout linearLayoutRoot = (LinearLayout) inflate.findViewById(R.id.linearlayout_my);
        mSwipeRefreshLayout.addView(inflate);
        // 设置可下拉刷新的子view
        mSwipeRefreshLayout.setSwipeableChildren(R.id.linearlayout_my);
        mLinearLayoutUser = (LinearLayout) linearLayoutRoot.findViewById(R.id.linearlayout_my_fragment_user);
        mTvUserState = (TextView) linearLayoutRoot.findViewById(R.id.tv_signin_state);
        mLinearLayoutUser.setOnClickListener(this);
        MyItemView mivFavorite = (MyItemView) linearLayoutRoot.findViewById(R.id.myitemview_favorite);
        MyItemView mivAbout = (MyItemView) linearLayoutRoot.findViewById(R.id.myitemview_about);
        mMivLogout = (MyItemView) linearLayoutRoot.findViewById(R.id.myitemview_logout);
        mivFavorite.setOnClickListener(this);
        mivAbout.setOnClickListener(this);
        mMivLogout.setOnClickListener(this);
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
            mMivLogout.setVisibility(View.VISIBLE);
            mLinearLayoutUser.setOnClickListener(null);
        } else {
            mTvUserState.setText(R.string.unregistered);
            mMivLogout.setVisibility(View.GONE);
            mLinearLayoutUser.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearlayout_my_fragment_user:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.myitemview_favorite:
                String username = PreferenceUtils.getString(getContext(), SpConstants.KEY_USERNAME, "");
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(mActivity, R.string.please_login_first, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    MyCollectionActivity.start(getActivity());
                }

                break;
            case R.id.myitemview_about:
                AboutActivity.start(getActivity());
                break;
            case R.id.myitemview_logout:
                PreferenceUtils.putString(mActivity, SpConstants.KEY_USERNAME, "");
                ClearableCookieJar cookieJar =
                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mActivity));
                cookieJar.clear();
                cookieJar.clearSession();
                EventBus.getDefault().post(new LogoutMessageEvent());
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
