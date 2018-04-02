package com.wan.android.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.base.BaseFragment;
import com.wan.android.data.bean.CommonException;
import com.wan.android.content.ContentActivity;
import com.wan.android.data.bean.FriendData;
import com.wan.android.util.Colors;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class FriendFragment extends BaseFragment implements FriendContract.View {
    private LoadService mLoadService;
    private TagFlowLayout mTagFlowLayout;
    private FriendContract.Presenter mPresenter;
    public static FriendFragment newInstance() {
        Bundle args = new Bundle();
        FriendFragment fragment = new FriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 获取片段的布局
        View friendView = LayoutInflater.from(getContext()).inflate(R.layout.friend_fragment, null);
        // 获取LoadService,把RecyclerView添加进去
        mLoadService = LoadSir.getDefault().register(friendView, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                swipeRefresh();
            }
        });

        // 把状态管理页面添加到根布局中
        mSwipeRefreshLayout.addView(mLoadService.getLoadLayout(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTagFlowLayout = (TagFlowLayout)mSwipeRefreshLayout. findViewById(R.id.id_flowlayout);

        // 设置可下拉刷新的子view
        mSwipeRefreshLayout.setSwipeableChildren(R.id.scrollview_friend, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh();
            }
        });
        swipeRefresh();
    }

    private void swipeRefresh() {
        mPresenter.getFriend();
    }

    @Override
    public void showGetFriendSuccess(final List<FriendData> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showSuccess();
        final ArrayList<Integer> colors = Colors.randomList(data.size());
        mTagFlowLayout.setAdapter(new TagAdapter<FriendData>(data) {
            @Override
            public View getView(FlowLayout parent, int position, FriendData d) {
                TextView textView = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.flowlayout_item, mTagFlowLayout, false);
                textView.setText(d.getName());
                textView.setTextColor(colors.get(position));
                return textView;
            }
        });
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                FriendData d = data.get(position);
                ContentActivity.start(mActivity, d.getName(), d.getLink(), d.getId());
                return true;
            }
        });
    }

    @Override
    public void showGetFriengFail(CommonException e) {
        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showCallback(EmptyCallback.class);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.scrollview_friend, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
    }

    @Override
    public void setPresenter(FriendContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
