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
import com.wan.android.adapter.HomeAdapter;
import com.wan.android.bean.BannerResponse;
import com.wan.android.bean.HomeListResponse;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.client.BannerClient;
import com.wan.android.client.HomeListClient;
import com.wan.android.retrofit.RetrofitClient;
import com.wan.android.util.GlideImageLoader;
import com.wan.android.view.MultiSwipeRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/2/1
 */
public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<HomeListResponse.Data.Datas> mDatasList = new ArrayList<>();
    private HomeAdapter mHomeAdapter;
    private Banner mBanner;
    private LoadService mLoadService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取根布局
        MultiSwipeRefreshLayout rootView = (MultiSwipeRefreshLayout) inflater.inflate(R.layout.fragment_home, container, false);
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
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_fragment_home);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_fragment_home, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mRecyclerView = (RecyclerView) recyclerView.findViewById(R.id.recyclerview_fragment_home);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        initAdapter();
        addHeader();
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

    @Override
    public void onStop() {
        super.onStop();
        isBannerLoaded = false;
    }

    private void refresh() {
        if (!isBannerLoaded) {
            initBanner();
        }
        // 防止下拉刷新时,还可以上拉加载
        mHomeAdapter.setEnableLoadMore(false);
        HomeListClient client = RetrofitClient.create(HomeListClient.class);
        Call<HomeListResponse> call = client.getHomeFeed(0);
        call.enqueue(new Callback<HomeListResponse>() {
            @Override
            public void onResponse(Call<HomeListResponse> call, Response<HomeListResponse> response) {
                mHomeAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "response: " + response);
                }

                HomeListResponse body = response.body();
                HomeListResponse.Data data = body.getData();
                List<HomeListResponse.Data.Datas> datas = data.getDatas();
                mDatasList.clear();
                mDatasList.addAll(datas);
                mHomeAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<HomeListResponse> call, Throwable t) {
                mHomeAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "t:" + t);
                }
                mLoadService.showCallback(EmptyCallback.class);
                mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_fragment_home, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
            }
        });
    }

    private void addHeader() {
        View header = LayoutInflater.from(mActivity).inflate(R.layout.home_header_view, (ViewGroup) mRecyclerView.getParent(), false);
        mBanner = (Banner) header.findViewById(R.id.banner_fragment_main_header);
        mHomeAdapter.addHeaderView(header);
    }

    private void initAdapter() {
        mHomeAdapter = new HomeAdapter(R.layout.home_item_view, mDatasList);
        // 加载更多
        mHomeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.d(TAG, "onLoadMoreRequested() called");
                loadMore();
            }
        }, mRecyclerView);
        mHomeAdapter.setEmptyView(R.layout.empty_view);
        mRecyclerView.setAdapter(mHomeAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeListResponse.Data.Datas datas = mDatasList.get(position);
                String link = datas.getLink();
                ContentActivity.start(mActivity, link);
            }
        });
    }

    private int mNextPage = 1;

    private void loadMore() {
        HomeListClient client = RetrofitClient.create(HomeListClient.class);
        Call<HomeListResponse> call = client.getHomeFeed(mNextPage);
        call.enqueue(new Callback<HomeListResponse>() {
            @Override
            public void onResponse(Call<HomeListResponse> call, Response<HomeListResponse> response) {
                mNextPage++;
                if (mNextPage < response.body().getData().getPagecount()) {
                    mHomeAdapter.loadMoreComplete();
                } else {
                    mHomeAdapter.loadMoreEnd();
                }

                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "response: " + response);
                }

                HomeListResponse body = response.body();
                HomeListResponse.Data data = body.getData();
                List<HomeListResponse.Data.Datas> datas = data.getDatas();
                mDatasList.addAll(datas);
                mHomeAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<HomeListResponse> call, Throwable t) {
                mHomeAdapter.loadMoreFail();
                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "t:" + t);
                }
            }
        });
    }

    private boolean isBannerLoaded = false;

    private void initBanner() {
        BannerClient client = RetrofitClient.create(BannerClient.class);
        Call<BannerResponse> call = client.getBanner();
        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "getBanner() onResponse response:" + response);
                }
                BannerResponse body = response.body();
                if (body == null) {
                    return;
                }
                if (body.getErrorcode() != 0) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "getBanner() onResponse error msg: " + body.getErrormsg());
                    }
                    return;
                }
                if (body.getData() == null || body.getData().size() == 0) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "getBanner() onResponse no data");
                    }
                    return;
                }
                isBannerLoaded = true;
                final List<BannerResponse.Data> data = body.getData();
                List<String> titles = new ArrayList<String>();
                List<String> imagePaths = new ArrayList<String>();
                for (int i = 0; i < data.size(); i++) {
                    BannerResponse.Data element = data.get(i);
                    if (element == null) {
                        continue;
                    }
                    titles.add(element.getTitle());
                    imagePaths.add(element.getImagepath());
                }
                mBanner
                        // 显示圆形指示器和标题（垂直显示）
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                        // 指示器居右
                        .setIndicatorGravity(BannerConfig.RIGHT)
                        .setBannerTitles(titles)
                        // 设置图片加载器
                        .setImageLoader(new GlideImageLoader())
                        .isAutoPlay(true)
                        // 设置轮播图片间隔时间（单位毫秒，默认为2000）
                        .setDelayTime(2000)
                        // 设置轮播图片(所有设置参数方法都放在此方法之前执行)
                        .setImages(imagePaths)
                        // 设置点击事件，下标是从0开始
                        .setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {
                                ContentActivity.start(mActivity, data.get(position).getUrl());
                            }
                        })
                        // 开始进行banner渲染（必须放到最后执行）
                        .start();

            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "getBanner() onFailure: t = " + t);
                }

            }
        });
    }
}
