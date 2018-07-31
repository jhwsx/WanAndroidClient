package com.wan.android.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.wan.android.R;
import com.wan.android.adapter.CommonListAdapter;
import com.wan.android.author.AuthorActivity;
import com.wan.android.base.BaseListFragment;
import com.wan.android.branch.BranchActivity;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.constant.FromTypeConstants;
import com.wan.android.content.ContentActivity;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.BannerData;
import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.event.ContentCollectSuccessFromHomeEvent;
import com.wan.android.data.event.NavigationEvent;
import com.wan.android.data.event.ProjectEvent;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.util.GlideImageLoader;
import com.wan.android.util.LoginUtils;
import com.wan.android.util.ToastUtils;
import com.wan.android.util.Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页 Fragment
 * @author wzc
 * @date 2018/3/27
 */
public class HomeFragment extends BaseListFragment implements HomeContract.View {
    private CommonListAdapter mCommonListAdapter;
    private Banner mBanner;
    private HomeContract.Presenter mPresenter;
    private boolean mIsBannerLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new HomePresenter(this);
        initAdapter();
        addHeader();
        initRefreshLayout();
        swipeRefresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsBannerLoaded = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contentCollectSuccessFromHomeEvent(
            ContentCollectSuccessFromHomeEvent contentCollectEvent) {
        mItemIv.setImageResource(R.drawable.ic_favorite);
        mCommonListAdapter.getData().get(mItemPosition).setCollect(true);
    }
    @Override
    protected void swipeRefresh() {
        if (!mIsBannerLoaded) {
            mPresenter.fetchBanner();
        }
        // 防止下拉刷新时,还可以上拉加载
        mCommonListAdapter.setEnableLoadMore(false);
        mCurrPage = resetCurrPage();
        // 下拉刷新
        mPresenter.swipeRefresh();
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> swipeRefresh());
    }
    // 点击收藏的收藏图片
    private ImageView mCollectIv;
    // 点击收藏的位置
    private int mCollectPosition;
    // 点击条目的收藏图片
    private ImageView mItemIv;
    // 点击条目的位置
    private int mItemPosition;
    private int mCurrPage = 1;

    private void initAdapter() {
        mCommonListAdapter = new CommonListAdapter(R.layout.recycle_item);
        // 加载更多
        mCommonListAdapter.setOnLoadMoreListener(() -> {
            mPresenter.loadMore(mCurrPage);
            mCurrPage++;
        }, mRecyclerView);
        mCommonListAdapter.setEmptyView(R.layout.empty_view);
        mRecyclerView.setAdapter(mCommonListAdapter);
        mCommonListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ArticleDatas datas = mCommonListAdapter.getData().get(position);
            mItemIv = (ImageView) view.findViewById(R.id.iv_home_item_view_collect);
            mItemPosition = position;
            ContentActivity.start(mActivity, FromTypeConstants.FROM_HOME_FRAGMENT, datas.getTitle(), datas.getLink(), datas.getId());
        });
        mCommonListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ArticleDatas articleDatas = mCommonListAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.iv_home_item_view_collect:
                    if (!LoginUtils.isLogin()) {
                        ToastUtils.showShort(R.string.login_first);
                        Intent intent = new Intent(mActivity, LoginActivity.class);
                        startActivity(intent);
                        return;
                    }
                    mCollectIv = (ImageView) view;
                    mCollectPosition = position;
                    if (articleDatas.isCollect()) {
                        mPresenter.uncollect(articleDatas.getId());
                    } else {
                        mPresenter.collect(articleDatas.getId());
                    }
                    break;
                case R.id.tv_home_item_view_chapter_name:
                    String superChapterName = articleDatas.getSuperChapterName();
                    String title = articleDatas.getChapterName();
                    BranchData.Leaf leaf = new BranchData.Leaf();
                    leaf.setChildren(new ArrayList<String>());
                    leaf.setCourseid(articleDatas.getCourseId());
                    leaf.setId(articleDatas.getChapterId());
                    leaf.setName(articleDatas.getChapterName());
                    ArrayList<BranchData.Leaf> data = new ArrayList<BranchData.Leaf>();
                    data.add(leaf);
                    BranchActivity.start(mActivity,superChapterName,title, data);
                    break;
                case R.id.tv_home_item_view_author:
                    AuthorActivity.start(mActivity, articleDatas.getAuthor());
                    break;
                case R.id.tv_home_item_view_tag:
                    ArrayList<ArticleDatas.TagsBean> tags = articleDatas.getTags();
                    if (!tags.isEmpty() && tags.get(0) != null) {
                        String name = tags.get(0).getName();
                        if (name.contains(getString(R.string.navigation))) {
                            // 跳转导航
                            EventBus.getDefault().post(new NavigationEvent());
                        } else if (name.contains(getString(R.string.project))) {
                            // 跳转项目
                            EventBus.getDefault().post(new ProjectEvent());
                        }
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private void addHeader() {
        View header = LayoutInflater.from(mActivity).inflate(R.layout.home_header_view, (ViewGroup) mRecyclerView.getParent(), false);
        mBanner = (Banner) header.findViewById(R.id.banner_fragment_main_header);
        mCommonListAdapter.addHeaderView(header);
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
                .setOnBannerListener(position -> {
                    BannerData e = data.get(position);
                    ContentActivity.start(mActivity, e.getTitle(), e.getUrl(), e.getId());
                })
                // 开始进行banner渲染（必须放到最后执行）
                .start();
    }

    @Override
    public void showBannerFail(CommonException e) {
        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSwipeRefreshSuccess(List<ArticleDatas> datas) {
        mCommonListAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mCommonListAdapter.setNewData(datas);
        mCommonListAdapter.disableLoadMoreIfNotFullPage();
        mLoadService.showSuccess();
    }

    @Override
    public void showSwipeRefreshFail(CommonException e) {
        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
        mCommonListAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showCallback(EmptyCallback.class);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
    }

    @Override
    public void showLoadMoreSuccess(List<ArticleDatas> datas) {
        mCommonListAdapter.addData(datas);
        mLoadService.showSuccess();
    }

    @Override
    public void showLoadMoreFail(CommonException e) {
        mCommonListAdapter.loadMoreFail();
        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadMoreComplete() {
        mCommonListAdapter.loadMoreComplete();
    }

    @Override
    public void showLoadMoreEnd() {
        mCommonListAdapter.loadMoreEnd();
    }

    @Override
    public void showCollectSuccess() {
        mCollectIv.setImageResource(R.drawable.ic_favorite);
        mCommonListAdapter.getData().get(mCollectPosition).setCollect(true);
        Toast.makeText(Utils.getApp(), R.string.collect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCollectFail(CommonException e) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getString(R.string.collect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectSuccess() {
        mCollectIv.setImageResource(R.drawable.ic_favorite_empty);
        mCommonListAdapter.getData().get(mCollectPosition).setCollect(false);
        Toast.makeText(Utils.getApp(), R.string.uncollect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectFail(CommonException e) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getString(R.string.uncollect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        // do nothing
    }
}
