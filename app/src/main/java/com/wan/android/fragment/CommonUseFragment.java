package com.wan.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.activity.ContentActivity;
import com.wan.android.adapter.BranchAdapter;
import com.wan.android.bean.BranchListResponse;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.client.BranchListClient;
import com.wan.android.retrofit.RetrofitClient;
import com.wan.android.view.MultiSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/2/12
 */
public class CommonUseFragment extends BaseFragment {

    private static final String TAG = CommonUseFragment.class.getSimpleName();
    private LoadService mLoadService;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<BranchListResponse.Data.Datas> mDatasList = new ArrayList<>();
    private BranchAdapter mBranchAdapter;
    private static final String ARG_CID = "arg_cid";
    public static CommonUseFragment newInstance(int cid) {
        
        Bundle args = new Bundle();
        args.putInt(ARG_CID, cid);
        CommonUseFragment fragment = new CommonUseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mCid;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCid =  getArguments().getInt(ARG_CID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MultiSwipeRefreshLayout rootView = (MultiSwipeRefreshLayout) inflater.inflate(R.layout.fragment_common_use, container, false);
        // 获取RecyclerView布局
        View recyclerView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, null);
        // 获取LoadService,把RecyclerView添加进去
        mLoadService = LoadSir.getDefault().register(recyclerView, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                refresh();
            }
        });

        // 把状态管理页面添加到根布局中
        rootView.addView(mLoadService.getLoadLayout(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 设置可下拉刷新的子view
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_fragment_common_use);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_fragment_home, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mRecyclerView = (RecyclerView) recyclerView.findViewById(R.id.recyclerview_fragment_home);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        initAdapter();
        initRefreshLayout();
        refresh();
        return rootView;
    }
    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }
    private void initAdapter() {
        mBranchAdapter = new BranchAdapter(R.layout.home_item_view, mDatasList);
        // 加载更多
        mBranchAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.d(TAG, "onLoadMoreRequested() called");
                loadMore();
            }
        }, mRecyclerView);
        mBranchAdapter.setEmptyView(R.layout.empty_view);
        mRecyclerView.setAdapter(mBranchAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                BranchListResponse.Data.Datas datas = mDatasList.get(position);
                String link = datas.getLink();
                ContentActivity.start(mActivity, link);
            }
        });
    }

    private int mNextPage = 1;
    private void loadMore() {
        BranchListClient client = RetrofitClient.create(BranchListClient.class);
        Call<BranchListResponse> call = client.getBranchList(mNextPage, mCid);
        call.enqueue(new Callback<BranchListResponse>() {
            @Override
            public void onResponse(Call<BranchListResponse> call, Response<BranchListResponse> response) {
                mNextPage++;
                if (mNextPage < response.body().getData().getPagecount()) {
                    mBranchAdapter.loadMoreComplete();
                } else {
                    mBranchAdapter.loadMoreEnd();
                }
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "response:" + response);
                }

                BranchListResponse body = response.body();
                BranchListResponse.Data data = body.getData();
                List<BranchListResponse.Data.Datas> datas = data.getDatas();
                mDatasList.addAll(datas);
                mBranchAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<BranchListResponse> call, Throwable t) {
                mBranchAdapter.loadMoreFail();
            }
        });
    }

    private void refresh() {

        // 防止下拉刷新时,还可以上拉加载
        mBranchAdapter.setEnableLoadMore(false);
        BranchListClient client = RetrofitClient.create(BranchListClient.class);
        Call<BranchListResponse> call = client.getBranchList(0, mCid);
        call.enqueue(new Callback<BranchListResponse>() {
            @Override
            public void onResponse(Call<BranchListResponse> call, Response<BranchListResponse> response) {
                mBranchAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "response: " + response);
                }

                BranchListResponse body = response.body();
                BranchListResponse.Data data = body.getData();
                List<BranchListResponse.Data.Datas> datas = data.getDatas();
                mDatasList.clear();
                mDatasList.addAll(datas);
                mBranchAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<BranchListResponse> call, Throwable t) {
                mBranchAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "t:" + t);
                }
                mLoadService.showCallback(EmptyCallback.class);
                mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_fragment_home, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
            }
        });
    }
}
