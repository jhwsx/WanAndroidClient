package com.wan.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.activity.BranchActivity;
import com.wan.android.adapter.TreeAdapter;
import com.wan.android.bean.TreeListResponse;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.client.KnowledgeListClient;
import com.wan.android.view.MultiSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wzc
 * @date 2018/2/1
 */
public class TreeFragment extends BaseFragment {
    private static final String TAG = TreeFragment.class.getSimpleName();
    private LoadService mLoadService;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TreeAdapter mTreeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 获取根布局
        MultiSwipeRefreshLayout rootView = (MultiSwipeRefreshLayout) inflater.inflate(R.layout.fragment_knowledge, container, false);
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
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_fragment_knowledge);
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

    private void refresh() {
        String API_BASE_URL = "http://wanandroid.com/";
        OkHttpClient.Builder httpClient1 = new OkHttpClient.Builder();

        Retrofit.Builder builder1 = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create()
                );
        Retrofit retrofit1 = builder1
                .client(
                        httpClient1.build()
                )
                .build();

        KnowledgeListClient client1 = retrofit1.create(KnowledgeListClient.class);
        Call<TreeListResponse> call = client1.getTree();
        call.enqueue(new Callback<TreeListResponse>() {
            @Override
            public void onResponse(Call<TreeListResponse> call, Response<TreeListResponse> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                mLoadService.showSuccess();

                TreeListResponse body = response.body();
                List<TreeListResponse.Data> data = body.getData();
                mDatasList.clear();
                mDatasList.addAll(data);
                mTreeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TreeListResponse> call, Throwable t) {

                mSwipeRefreshLayout.setRefreshing(false);
                mLoadService.showCallback(EmptyCallback.class);
                mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_fragment_home,R.id.ll_error, R.id.ll_empty,R.id.ll_loading);

            }
        });
    }
    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }
    private List<TreeListResponse.Data> mDatasList = new ArrayList<>();

    private void initAdapter() {
        mTreeAdapter = new TreeAdapter(R.layout.knowledge_item_view, mDatasList);
        mTreeAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mTreeAdapter);
        mTreeAdapter.setEnableLoadMore(false);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                TreeListResponse.Data data = mDatasList.get(position);
                String title = data.getName();
                ArrayList<TreeListResponse.Data.Children> children = data.getChildren();
                BranchActivity.start(mActivity, title, children);
                Toast.makeText(mActivity, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
