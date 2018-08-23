package com.wan.android.ui.search;

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
import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.adapter.CommonListAdapter;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.ui.event.SearchResultFragmentDestroyEvent;
import com.wan.android.ui.loadcallback.EmptyCallback;
import com.wan.android.ui.loadcallback.LoadingCallback;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * 搜索结果片段
 *
 * @author wzc
 * @date 2018/8/22
 */
public class SearchResultFragment extends BaseFragment
        implements SearchResultContract.View,
        BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = SearchResultFragment.class.getSimpleName();
    public static final String ARGS_SEARCH_QUERY = "com.wan.android.args_search_query";
    private LoadService mLoadService;

    public static SearchResultFragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString(ARGS_SEARCH_QUERY, query);
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Inject
    CommonListAdapter mAdapter;
    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    HorizontalDividerItemDecoration mHorizontalDividerItemDecoration;
    @Inject
    SearchResultContract.Presenter<SearchResultContract.View> mPresenter;
    private String mQuery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuery = getArguments().getString(ARGS_SEARCH_QUERY);
        }
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
        mLoadService = LoadSir.getDefault().register(view, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                query(mQuery);
            }
        });
        return mLoadService.getLoadLayout();
    }

    @Override
    protected void setUp(View view) {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mRecyclerView.addItemDecoration(mHorizontalDividerItemDecoration);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage();
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setEnableLoadMore(false);
        query(mQuery);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        EventBus.getDefault().post(new SearchResultFragmentDestroyEvent());
        super.onDestroyView();
    }

    public void query(String query) {
        mQuery = query;
        Timber.d("swipeRefresh: query=%s", mQuery);
        mPresenter.swipeRefresh(mQuery);
    }

    @Override
    public void showSearchNoMatch() {
        mLoadService.showCallback(EmptyCallback.class);
        showMessage(R.string.search_no_match);
    }

    @Override
    public void showSwipeRefreshSuccess(List<ArticleDatas> datas) {
        Timber.d("showSwipeRefreshSuccess, data size=%s", datas.size());
        mAdapter.setNewData(datas);
        mLoadService.showSuccess();
        mAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
    }

    @Override
    public void showSwipeRefreshFail() {
        Timber.d("showSwipeRefreshFail");
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        if (mAdapter.getData().isEmpty()) {
            mLoadService.showCallback(NetworkErrorCallback.class);
        }
    }

    @Override
    public void showLoadMoreSuccess(List<ArticleDatas> datas) {
        Timber.d("showLoadMoreSuccess, data size=%s", datas.size());
        mAdapter.addData(datas);
    }

    @Override
    public void showLoadMoreFail() {
        Timber.d("showLoadMoreFail");
        mAdapter.loadMoreFail();
    }

    @Override
    public void showLoadMoreComplete() {
        Timber.d("showLoadMoreComplete");
        mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoadMoreEnd() {
        Timber.d("showLoadMoreEnd");
        mAdapter.loadMoreEnd();
    }

    @Override
    public void onLoadMoreRequested() {
        Timber.d("loadMore, query=%s", mQuery);
        mPresenter.loadMore(mQuery);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        query(mQuery);
    }
}
