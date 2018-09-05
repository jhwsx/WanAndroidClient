package com.wan.android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.ContentData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.adapter.CommonListAdapter;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.ui.content.ContentActivity;
import com.wan.android.ui.loadcallback.LoadingCallback;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;
import com.wan.android.ui.login.LoginActivity;
import com.wan.android.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页 Fragment
 *
 * @author wzc
 * @date 2018/8/2
 */
public class HomeFragment extends BaseFragment implements HomeContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Inject
    HomePresenter<HomeContract.View> mPresenter;
    @Inject
    CommonListAdapter mAdapter;
    @Inject
    LayoutInflater mInflater;
    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    HorizontalDividerItemDecoration mHorizontalDividerItemDecoration;
    private LoadService mLoadService;
    private Banner mBanner;
    private boolean mIsBannerLoaded;
    private int mClickCollectPositoin;
    private ImageView mIvCollect;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
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
                        mLoadService.showCallback(LoadingCallback.class);
                        mPresenter.swipeRefresh(mIsBannerLoaded);
                    }
                });
        return mLoadService.getLoadLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }

    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    protected void setUp(View view) {
        View header = mInflater.inflate(R.layout.home_header_view, null);
        mBanner = (Banner) header.findViewById(R.id.banner_fragment_main_header);
        mBanner// 显示圆形指示器和标题（垂直显示）
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                // 指示器居右
                .setIndicatorGravity(BannerConfig.RIGHT)
                // 设置图片加载器
                .setImageLoader(new GlideImageLoader())
                .isAutoPlay(true)
                // 设置轮播图片间隔时间（单位毫秒，默认为2000）
                .setDelayTime(2000);
        mAdapter.addHeaderView(header);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mRecyclerView.addItemDecoration(mHorizontalDividerItemDecoration);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage();
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setEnableLoadMore(false);
        mPresenter.swipeRefresh(mIsBannerLoaded);
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
    public void showBannerSuccess(final List<BannerData> data) {
        mIsBannerLoaded = true;
        List<String> titles = new ArrayList<String>();
        List<String> imagePaths = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            BannerData element = data.get(i);
            if (element == null) {
                continue;
            }
            titles.add(element.getTitle());
            imagePaths.add(element.getImagePath());
        }
        mBanner.setBannerTitles(titles)
                // 设置轮播图片(所有设置参数方法都放在此方法之前执行)
                .setImages(imagePaths)
                // 设置点击事件，下标是从0开始
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        BannerData bd = data.get(position);
                        ContentData cd = new ContentData(bd.getId(), bd.getTitle(), bd.getUrl(), null);
                        ContentActivity.start(getActivity(), cd);
                    }
                })
                // 开始进行banner渲染（必须放到最后执行）
                .start();
    }

    @Override
    public void showSwipeRefreshSuccess(List<ArticleDatas> datas) {
        mAdapter.setNewData(datas);
        mLoadService.showSuccess();
        mAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
    }

    @Override
    public void showSwipeRefreshFail() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        if (mAdapter.getData().isEmpty()) {
            mLoadService.showCallback(NetworkErrorCallback.class);
        }
    }

    @Override
    public void showLoadMoreSuccess(List<ArticleDatas> datas) {
        mAdapter.addData(datas);
    }

    @Override
    public void showLoadMoreFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void showLoadMoreComplete() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoadMoreEnd() {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void showCollectInSiteArticleSuccess() {
        showMessage(R.string.collect_successfully);
        setCollectState(true);
    }

    @Override
    public void showUncollectArticleListArticleSuccess() {
        showMessage(R.string.uncollect_successfully);
        setCollectState(false);
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        mPresenter.swipeRefresh(mIsBannerLoaded);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMore();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<ArticleDatas> data = adapter.getData();
        ArticleDatas articleDatas = data.get(position);
        ContentData contentData = new ContentData(articleDatas.getId(),
                articleDatas.getTitle(), articleDatas.getLink(), articleDatas.isCollect());

        View title = view.findViewById(R.id.tv_home_item_view_title);
        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra(ContentActivity.EXTRA_CONTENT_DATA, contentData);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), title, getString(R.string.shared_title));
        getActivity().startActivity(intent, transitionActivityOptions.toBundle());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        if (view.getId() == R.id.iv_home_item_view_collect) {
            boolean isLogin = mPresenter.getLoginStaus();
            if (!isLogin) {
                onError(R.string.login_first);
                LoginActivity.start(getBaseActivity());
                return;
            }
            mClickCollectPositoin = position;
            mIvCollect = (ImageView) view;
            ArticleDatas articleDatas = (ArticleDatas) adapter.getData().get(position);
            int id = (int) articleDatas.getId().longValue();
            if (getCollectState()) {
                mPresenter.uncollectArticleListArticle(id);
            } else {
                mPresenter.collectInSiteArticle(id);
            }

        }
    }

    private boolean getCollectState() {
        ArticleDatas articleDatas = mAdapter.getData().get(mClickCollectPositoin);
        return articleDatas.isCollect();
    }

    private void setCollectState(boolean isCollect) {
        ArticleDatas articleDatas = mAdapter.getData().get(mClickCollectPositoin);
        articleDatas.setCollect(isCollect);
        mIvCollect.setImageResource(isCollect ? R.drawable.ic_favorite : R.drawable.ic_favorite_empty);
    }
}
