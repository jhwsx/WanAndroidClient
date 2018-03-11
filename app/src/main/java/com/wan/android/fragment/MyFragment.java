package com.wan.android.fragment;

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
import com.wan.android.activity.AboutActivity;
import com.wan.android.activity.LoginActivity;
import com.wan.android.activity.MyCollectionActivity;
import com.wan.android.bean.LogoutMessageEvent;
import com.wan.android.constant.SpConstants;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.view.MyItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.app.Activity.RESULT_OK;

/**
 * @author wzc
 * @date 2018/2/1
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MyFragment.class.getSimpleName();
    private TextView mTvUserState;
    private MyItemView mMivLogout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.fragment_collection, null);
        LinearLayout linearLayoutRoot = (LinearLayout) inflate.findViewById(R.id.linearlayout_my);
        mSwipeRefreshLayout.addView(inflate);
        // 设置可下拉刷新的子view
        mSwipeRefreshLayout.setSwipeableChildren(R.id.linearlayout_my);
        LinearLayout linearLayoutUser = (LinearLayout) linearLayoutRoot.findViewById(R.id.linearlayout_my_fragment_user);
        mTvUserState = (TextView) linearLayoutRoot.findViewById(R.id.tv_signin_state);
        linearLayoutUser.setOnClickListener(this);
        MyItemView mivFavorite = (MyItemView) linearLayoutRoot.findViewById(R.id.myitemview_favorite);
        MyItemView mivAbout = (MyItemView) linearLayoutRoot.findViewById(R.id.myitemview_about);
        mMivLogout = (MyItemView) linearLayoutRoot.findViewById(R.id.myitemview_logout);
        mivFavorite.setOnClickListener(this);
        mivAbout.setOnClickListener(this);
        mMivLogout.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        update();
    }

    private void update() {
        String username = PreferenceUtils.getString(getContext(), SpConstants.KEY_USERNAME, "");

        if (!TextUtils.isEmpty(username)) {
            mTvUserState.setText(username);
            mMivLogout.setVisibility(View.VISIBLE);
        } else {
            mTvUserState.setText(R.string.unregistered);
            mMivLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearlayout_my_fragment_user:
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), 2000);
                break;
            case R.id.myitemview_favorite:
                String username = PreferenceUtils.getString(getContext(), SpConstants.KEY_USERNAME, "");
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(mActivity, R.string.please_login_first, Toast.LENGTH_SHORT).show();
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), 2000);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode != 2000) {
            return;
        }
        update();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LogoutMessageEvent messageEvent) {
        update();
    }

}
