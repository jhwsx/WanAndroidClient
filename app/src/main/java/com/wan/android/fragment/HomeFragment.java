package com.wan.android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.activity.ContentActivity;
import com.wan.android.adapter.CommonListAdapter;
import com.wan.android.bean.ArticleDatas;
import com.wan.android.bean.BannerData;
import com.wan.android.bean.CommonResponse;
import com.wan.android.bean.ArticleData;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.client.BannerClient;
import com.wan.android.client.HomeListClient;
import com.wan.android.helper.CollectHelper;
import com.wan.android.helper.UncollectHelper;
import com.wan.android.listener.OnCollectSuccessListener;
import com.wan.android.listener.OnUncollectSuccessListener;
import com.wan.android.retrofit.RetrofitClient;
import com.wan.android.util.GlideImageLoader;
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
public class HomeFragment extends BaseListFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private List<ArticleDatas> mDatasList = new ArrayList<>();
    private CommonListAdapter mCommonListAdapter;
    private Banner mBanner;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAdapter();
        addHeader();
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

    @Override
    public void onStop() {
        super.onStop();
        isBannerLoaded = false;
    }

    @Override
    protected void refresh() {
        if (!isBannerLoaded) {
            initBanner();
        }
        // 防止下拉刷新时,还可以上拉加载
        mCommonListAdapter.setEnableLoadMore(false);
        HomeListClient client = RetrofitClient.create(HomeListClient.class);
        Call<CommonResponse<ArticleData>> call = client.getHomeFeed(0);
        call.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {
                mCommonListAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "response: " + response);
                }

                CommonResponse<ArticleData> body = response.body();
                ArticleData data = body.getData();
                List<ArticleDatas> datas = data.getDatas();
                mDatasList.clear();
                mDatasList.addAll(datas);
                mCommonListAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                mCommonListAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "t:" + t);
                }
                mLoadService.showCallback(EmptyCallback.class);
                mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
            }
        });
    }

    private void addHeader() {
        View header = LayoutInflater.from(mActivity).inflate(R.layout.home_header_view, (ViewGroup) mRecyclerView.getParent(), false);
        mBanner = (Banner) header.findViewById(R.id.banner_fragment_main_header);
        mCommonListAdapter.addHeaderView(header);
    }

    private void initAdapter() {
        mCommonListAdapter = new CommonListAdapter(R.layout.list_item_view, mDatasList);
        // 加载更多
        mCommonListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.d(TAG, "onLoadMoreRequested() called");
                loadMore();
            }
        }, mRecyclerView);
        mCommonListAdapter.setEmptyView(R.layout.empty_view);
        mRecyclerView.setAdapter(mCommonListAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleDatas datas = mDatasList.get(position);
                ContentActivity.start(mActivity, datas.getTitle(), datas.getLink(), datas.getId());
            }
        });
        mCommonListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_home_item_view_collect:
                        if (mDatasList.get(position).isCollect()) {
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
        CollectHelper.collect(mDatasList.get(position).getId(), new OnCollectSuccessListener() {
            @Override
            public void onCollectSuccess() {
                mDatasList.get(position).setCollect(true);
                ((ImageView) view).setImageResource(R.drawable.ic_favorite);
            }
        });

    }

    private void unCollect(final View view, final int position) {
        UncollectHelper.uncollect(mDatasList.get(position).getId(), new OnUncollectSuccessListener() {
            @Override
            public void onUncollectSuccess() {
                mDatasList.get(position).setCollect(false);
                ((ImageView) view).setImageResource(R.drawable.ic_favorite_empty);
            }
        });

    }

    private int mNextPage = 1;

    private void loadMore() {
        HomeListClient client = RetrofitClient.create(HomeListClient.class);
        Call<CommonResponse<ArticleData>> call = client.getHomeFeed(mNextPage);
        call.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {
                mNextPage++;
                if (mNextPage < response.body().getData().getPagecount()) {
                    mCommonListAdapter.loadMoreComplete();
                } else {
                    mCommonListAdapter.loadMoreEnd();
                }

                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "response: " + response);
                }

                CommonResponse<ArticleData> body = response.body();
                ArticleData data = body.getData();
                List<ArticleDatas> datas = data.getDatas();
                mDatasList.addAll(datas);
                mCommonListAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                mCommonListAdapter.loadMoreFail();
                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "t:" + t);
                }
            }
        });
    }

    private boolean isBannerLoaded = false;

    private void initBanner() {
        BannerClient client = RetrofitClient.create(BannerClient.class);
        Call<CommonResponse<List<BannerData>>> call = client.getBanner();
        call.enqueue(new Callback<CommonResponse<List<BannerData>>>() {
            @Override
            public void onResponse(Call<CommonResponse<List<BannerData>>> call, Response<CommonResponse<List<BannerData>>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "getBanner() onResponse response:" + response);
                }
                CommonResponse<List<BannerData>> body = response.body();
                if (body == null) {
                    return;
                }
                if (body.getErrorcode() != 0) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "getBanner() onResponse ic_error msg: " + body.getErrormsg());
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
                final List<BannerData> data = body.getData();
                List<String> titles = new ArrayList<String>();
                List<String> imagePaths = new ArrayList<String>();
                for (int i = 0; i < data.size(); i++) {
                    BannerData element = data.get(i);
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
                                BannerData e = data.get(position);
                                ContentActivity.start(mActivity,e.getTitle(), e.getUrl(), e.getId());
                            }
                        })
                        // 开始进行banner渲染（必须放到最后执行）
                        .start();

            }

            @Override
            public void onFailure(Call<CommonResponse<List<BannerData>>> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "getBanner() onFailure: t = " + t);
                }

            }
        });
    }
}
