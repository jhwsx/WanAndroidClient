package com.wan.android.branch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wan.android.R;
import com.wan.android.adapter.CommonListAdapter;
import com.wan.android.author.AuthorActivity;
import com.wan.android.base.BaseListFragment;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.constant.SpConstants;
import com.wan.android.content.ContentActivity;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ContentCollectEvent;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class BranchFragment extends BaseListFragment implements BranchContract.View{

    private static final String TAG = BranchFragment.class.getSimpleName();
    private List<ArticleDatas> mDatasList = new ArrayList<>();
    private CommonListAdapter mCommonListAdapter;
    private static final String ARG_CID = "arg_cid";
    private BranchContract.Presenter mPresenter;


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

        initAdapter();
        initRefreshLayout();
        swipeRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(ContentCollectEvent contentCollectEvent) {
        mItemIv.setImageResource(R.drawable.ic_favorite);
        mDatasList.get(mItemPosition).setCollect(true);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh();
            }
        });
    }

    private void initAdapter() {
        mCommonListAdapter = new CommonListAdapter(R.layout.recycle_item, mDatasList);
        // 加载更多
        mCommonListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore(mCurrPage, mCid);
                mCurrPage++;
            }
        }, mRecyclerView);
        mCommonListAdapter.setEmptyView(R.layout.empty_view);
        mRecyclerView.setAdapter(mCommonListAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleDatas datas = mDatasList.get(position);
                String link = datas.getLink();
                String title = datas.getTitle();
                mItemIv = (ImageView) view.findViewById(R.id.iv_home_item_view_collect);
                mItemPosition = position;
                ContentActivity.start(mActivity,title, link, datas.getId());
            }
        });

        mCommonListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleDatas articleDatas = mDatasList.get(position);
                switch (view.getId()) {
                    case R.id.iv_home_item_view_collect:
                        if (TextUtils.isEmpty(PreferenceUtils.getString(Utils.getContext(), SpConstants.KEY_USERNAME, ""))) {
                            Toast.makeText(Utils.getContext(), R.string.login_first, Toast.LENGTH_SHORT).show();
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
                        String title = articleDatas.getChapterName();
                        BranchData.Leaf leaf = new BranchData.Leaf();
                        leaf.setChildren(new ArrayList<String>());
                        leaf.setCourseid(articleDatas.getCourseId());
                        leaf.setId(articleDatas.getChapterId());
                        leaf.setName(articleDatas.getChapterName());
                        ArrayList<BranchData.Leaf> data = new ArrayList<BranchData.Leaf>();
                        data.add(leaf);
                        BranchActivity.start(mActivity,title, data);
                        break;
                    case R.id.tv_home_item_view_author:
                        AuthorActivity.start(mActivity, articleDatas.getAuthor());
                        break;
                    default:
                        break;
                }
            }
        });
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

    @Override
    protected void swipeRefresh() {
        // 防止下拉刷新时,还可以上拉加载
        mCommonListAdapter.setEnableLoadMore(false);
        // 下拉刷新
        mPresenter.swipeRefresh(mCid);
    }

    @Override
    public void showSwipeRefreshSuccess(List<ArticleDatas> datas) {
        mCommonListAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mDatasList.clear();
        mDatasList.addAll(datas);
        mCommonListAdapter.notifyDataSetChanged();
        mLoadService.showSuccess();
    }

    @Override
    public void showSwipeRefreshFail(CommonException e) {
        Toast.makeText(Utils.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        mCommonListAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showCallback(EmptyCallback.class);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
    }

    @Override
    public void showLoadMoreSuccess(List<ArticleDatas> datas) {
        mDatasList.addAll(datas);
        mCommonListAdapter.notifyDataSetChanged();
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
        mDatasList.get(mCollectPosition).setCollect(true);
        Toast.makeText(Utils.getContext(), R.string.collect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCollectFail(CommonException e) {
        Toast.makeText(Utils.getContext(), Utils.getContext().getString(R.string.collect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectSuccess() {
        mCollectIv.setImageResource(R.drawable.ic_favorite_empty);
        mDatasList.get(mCollectPosition).setCollect(false);
        Toast.makeText(Utils.getContext(), R.string.uncollect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectFail(CommonException e) {
        Toast.makeText(Utils.getContext(), Utils.getContext().getString(R.string.uncollect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setPresenter(BranchContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
