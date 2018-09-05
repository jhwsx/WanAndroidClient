package com.wan.android.ui.tree;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.data.network.model.BranchData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.adapter.TreeAdapter;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * 知识体系 Fragment
 *
 * @author wzc
 * @date 2018/8/3
 */
public class TreeFragment extends BaseFragment implements TreeContract.View, BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Inject
    TreeContract.Presenter<TreeContract.View> mPresenter;
    @Inject
    TreeAdapter mAdapter;
    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    HorizontalDividerItemDecoration mHorizontalDividerItemDecoration;
    private LoadService mLoadService;

    public static TreeFragment newInstance() {
        Bundle args = new Bundle();
        TreeFragment fragment = new TreeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        mLoadService = LoadSir.getDefault().register(view,
                new com.kingja.loadsir.callback.Callback.OnReloadListener() {
                    @Override
                    public void onReload(View v) {
                        mPresenter.swipeRefresh();
                    }
                });
        return mLoadService.getLoadLayout();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    protected void setUp(View view) {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mRecyclerView.addItemDecoration(mHorizontalDividerItemDecoration);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setEnableLoadMore(false);
        mPresenter.swipeRefresh();
    }

    @Override
    public void scrollToTop() {
        if (mLinearLayoutManager.findFirstVisibleItemPosition() > 20) {
            mRecyclerView.scrollToPosition(0);
        } else {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void showSwipeRefreshSuccess(List<BranchData> data) {
        Timber.d("showSwipeRefreshSuccess: size=%s", data.size());
        mAdapter.setNewData(data);
        mLoadService.showSuccess();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSwipeRefreshFail() {
        Timber.d("showSwipeRefreshFail");
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showCallback(NetworkErrorCallback.class);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BranchData branchData = mAdapter.getData().get(position);
        BranchActivity.start(getBaseActivity(), branchData);
    }

    @Override
    public void onRefresh() {
        mPresenter.swipeRefresh();
    }
}
