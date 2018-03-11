package com.wan.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.activity.ContentActivity;
import com.wan.android.adapter.BranchAdapter;
import com.wan.android.bean.BranchListResponse;
import com.wan.android.bean.CollectRepsonse;
import com.wan.android.bean.UncollectRepsonse;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.client.BranchListClient;
import com.wan.android.client.CollectClient;
import com.wan.android.client.UncollectClient;
import com.wan.android.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/2/12
 */
public class BranchFragment extends BaseListFragment {

    private static final String TAG = BranchFragment.class.getSimpleName();
    private List<BranchListResponse.Data.Datas> mDatasList = new ArrayList<>();
    private BranchAdapter mBranchAdapter;
    private static final String ARG_CID = "arg_cid";

    public static BranchFragment newInstance(int cid) {

        Bundle args = new Bundle();
        args.putInt(ARG_CID, cid);
        BranchFragment fragment = new BranchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mCid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCid = getArguments().getInt(ARG_CID);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAdapter();
        initRefreshLayout();
        refresh();
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
                String title = datas.getTitle();
                ContentActivity.start(mActivity,title, link, datas.getId());
            }
        });

        mBranchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_home_item_view_collect:
                        if (mDatasList.get(position).getCollect()) {
                            unCollect(view, position);
                        } else {
                            collect(view, position);
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void collect(final View view, final int position) {
        CollectClient collectClient = RetrofitClient.create(CollectClient.class);
        Call<CollectRepsonse> call = collectClient.collect(mDatasList.get(position).getId());
        call.enqueue(new Callback<CollectRepsonse>() {
            @Override
            public void onResponse(Call<CollectRepsonse> call, Response<CollectRepsonse> response) {
                CollectRepsonse body = response.body();
                if (body.getErrorcode() != 0) {
                    Toast.makeText(mActivity, body.getErrormsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mActivity, R.string.collect_successfully, Toast.LENGTH_SHORT).show();
                mDatasList.get(position).setCollect(true);
                ((ImageView) view).setImageResource(R.drawable.ic_favorite);
            }

            @Override
            public void onFailure(Call<CollectRepsonse> call, Throwable t) {
                Toast.makeText(mActivity, getString(R.string.collect_failed) + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void unCollect(final View view, final int position) {
        UncollectClient uncollectClient = RetrofitClient.create(UncollectClient.class);
        Call<UncollectRepsonse> call = uncollectClient.uncollect(mDatasList.get(position).getId());
        call.enqueue(new Callback<UncollectRepsonse>() {
            @Override
            public void onResponse(Call<UncollectRepsonse> call, Response<UncollectRepsonse> response) {
                UncollectRepsonse body = response.body();
                if (body.getErrorcode() != 0) {
                    Toast.makeText(mActivity, body.getErrormsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mActivity, R.string.uncollect_successfully, Toast.LENGTH_SHORT).show();
                mDatasList.get(position).setCollect(false);
                ((ImageView) view).setImageResource(R.drawable.ic_favorite_empty);
            }

            @Override
            public void onFailure(Call<UncollectRepsonse> call, Throwable t) {
                Toast.makeText(mActivity, getString(R.string.uncollect_failed) + t.toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void refresh() {

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
                mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
            }
        });
    }
}
