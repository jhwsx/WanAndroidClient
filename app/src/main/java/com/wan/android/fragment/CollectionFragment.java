package com.wan.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.activity.ContentActivity;
import com.wan.android.activity.LoginActivity;
import com.wan.android.adapter.CollectAdapter;
import com.wan.android.bean.CollectListResponse;
import com.wan.android.bean.LoginMessageEvent;
import com.wan.android.bean.LogoutMessageEvent;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.client.CollectListClient;
import com.wan.android.constant.SpConstants;
import com.wan.android.retrofit.RetrofitClient;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.view.MultiSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * @author wzc
 * @date 2018/2/1
 */
public class CollectionFragment extends BaseFragment {
    private static final String TAG = CollectionFragment.class.getSimpleName();
    private LinearLayout mLinearLayoutLogin;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private LoadService mLoadService;
    private RecyclerView mRecyclerView;
    private CollectAdapter mCollectAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        mLinearLayoutLogin = (LinearLayout) view.findViewById(R.id.linearlayout_fragment_collect_login);
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_fragment_collect);
        String username = PreferenceUtils.getString(getActivity(), SpConstants.KEY_USERNAME, "");
        if (TextUtils.isEmpty(username)) {
            mLinearLayoutLogin.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.INVISIBLE);
        } else {
            mLinearLayoutLogin.setVisibility(View.INVISIBLE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
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
        mSwipeRefreshLayout.addView(mLoadService.getLoadLayout(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_fragment_home, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);

        mRecyclerView = (RecyclerView) recyclerView.findViewById(R.id.recyclerview_fragment_home);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        initAdapter();
        initRefreshLayout();
        if (!TextUtils.isEmpty(username)) {
            refresh();
        }

        TextView textView = (TextView) view.findViewById(R.id.tv_login);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), 2000);
            }
        });
        return view;
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        // 防止下拉刷新时,还可以上拉加载
        mCollectAdapter.setEnableLoadMore(false);
        CollectListClient client = RetrofitClient.create(CollectListClient.class);
        Call<CollectListResponse> call = client.getCollect(0);
        call.enqueue(new Callback<CollectListResponse>() {
            @Override
            public void onResponse(Call<CollectListResponse> call, Response<CollectListResponse> response) {

                mCollectAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "response:" + response);
                }
                CollectListResponse body = response.body();
                if (body == null) {
                    return;
                }
                CollectListResponse.Data data = body.getData();
                if (data == null) {
                    return;
                }
                List<CollectListResponse.Data.Datas> datas = data.getDatas();
                if (datas.size() == 0) {
                    mLoadService.showCallback(EmptyCallback.class);
                    return;
                }
                mDatasList.clear();
                mDatasList.addAll(datas);
                mCollectAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<CollectListResponse> call, Throwable t) {
                mCollectAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
                mLoadService.showCallback(EmptyCallback.class);
                mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_fragment_home, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "t:" + t);
                }
            }
        });
    }

    private List<CollectListResponse.Data.Datas> mDatasList = new ArrayList<>();

    private void initAdapter() {
        mCollectAdapter = new CollectAdapter(R.layout.home_item_view, mDatasList);
        mCollectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(mCollectAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                CollectListResponse.Data.Datas data = mDatasList.get(position);
                String link = data.getLink();
                ContentActivity.start(mActivity, link);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private int mNextPage = 1;

    private void loadMore() {
        CollectListClient client = RetrofitClient.create(CollectListClient.class);
        Call<CollectListResponse> call = client.getCollect(mNextPage);
        call.enqueue(new Callback<CollectListResponse>() {
            @Override
            public void onResponse(Call<CollectListResponse> call, Response<CollectListResponse> response) {
                CollectListResponse body = response.body();
                if (body == null) {
                    return;
                }
                CollectListResponse.Data data = body.getData();
                if (data == null) {
                    return;
                }
                mNextPage++;
                if (mNextPage < response.body().getData().getPagecount()) {
                    mCollectAdapter.loadMoreComplete();
                } else {
                    mCollectAdapter.loadMoreEnd();
                }
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "response:" + response);
                }

                List<CollectListResponse.Data.Datas> datas = data.getDatas();
                mDatasList.addAll(datas);
                mCollectAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<CollectListResponse> call, Throwable t) {
                mCollectAdapter.loadMoreFail();
            }
        });
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
        String username = PreferenceUtils.getString(getActivity(), SpConstants.KEY_USERNAME, "");
        if (TextUtils.isEmpty(username)) {
            mLinearLayoutLogin.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.INVISIBLE);
        } else {
            mLinearLayoutLogin.setVisibility(View.INVISIBLE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            refresh();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LogoutMessageEvent messageEvent) {
        mLinearLayoutLogin.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.INVISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LoginMessageEvent messageEvent) {
        mLinearLayoutLogin.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        refresh();
    }
}
