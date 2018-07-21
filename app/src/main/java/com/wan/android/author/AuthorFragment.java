package com.wan.android.author;

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
import com.wan.android.MainActivity;
import com.wan.android.R;
import com.wan.android.adapter.CommonListAdapter;
import com.wan.android.base.BaseListFragment;
import com.wan.android.branch.BranchActivity;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.constant.FromTypeConstants;
import com.wan.android.constant.SpConstants;
import com.wan.android.content.ContentActivity;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.event.ContentCollectSuccessFromAuthorEvent;
import com.wan.android.data.event.NavigationEvent;
import com.wan.android.data.event.ProjectEvent;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Author 片段
 * @author wzc
 * @date 2018/3/30
 */
public class AuthorFragment extends BaseListFragment implements AuthorContract.View {
    private static final String ARG_AUTHOR = "arg_author";
    private CommonListAdapter mCommonListAdapter;
    private List<ArticleDatas> mDatasList = new ArrayList<>();
    private String mAuthor;
    private AuthorContract.Presenter mPresenter;
    private ImageView mItemIv;
    private int mItemPosition;

    public static AuthorFragment newInstance(String author) {
        Bundle args = new Bundle();
        args.putString(ARG_AUTHOR, author);
        AuthorFragment fragment = new AuthorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mAuthor = getArguments().getString(ARG_AUTHOR);
        }
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

    private ImageView mView;
    private int mPosition;
    private int mCurrPage = 1;

    private void initAdapter() {
        mCommonListAdapter = new CommonListAdapter(R.layout.recycle_item, mDatasList);
        // 加载更多
        mCommonListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore(mCurrPage, mAuthor);
                mCurrPage++;
            }
        }, mRecyclerView);
        mCommonListAdapter.setEmptyView(R.layout.empty_view);
        mRecyclerView.setAdapter(mCommonListAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleDatas datas = mDatasList.get(position);
                mItemIv = (ImageView) view.findViewById(R.id.iv_home_item_view_collect);
                mItemPosition = position;
                String link = datas.getLink();
                String title = datas.getTitle();
                ContentActivity.start(mActivity, FromTypeConstants.FROM_AUTHOR_FRAGMENT, title, link, datas.getId());
            }
        });

        mCommonListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_home_item_view_collect:
                        if (TextUtils.isEmpty(PreferenceUtils.getString(Utils.getApp(), SpConstants.KEY_USERNAME, ""))) {
                            Toast.makeText(Utils.getApp(), R.string.login_first, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mActivity, LoginActivity.class);
                            startActivity(intent);
                            return;
                        }
                        mView = (ImageView) view;
                        mPosition = position;
                        if (mDatasList.get(position).isCollect()) {
                            mPresenter.uncollect(mDatasList.get(position).getId());
                        } else {
                            mPresenter.collect(mDatasList.get(position).getId());
                        }
                        break;
                    case R.id.tv_home_item_view_chapter_name:
                        String superChapterName = mDatasList.get(position).getSuperChapterName();
                        String title = mDatasList.get(position).getChapterName();
                        BranchData.Leaf leaf = new BranchData.Leaf();
                        leaf.setChildren(new ArrayList<String>());
                        leaf.setCourseid(mDatasList.get(position).getCourseId());
                        leaf.setId(mDatasList.get(position).getChapterId());
                        leaf.setName(mDatasList.get(position).getChapterName());
                        ArrayList<BranchData.Leaf> data = new ArrayList<BranchData.Leaf>();
                        data.add(leaf);
                        BranchActivity.start(mActivity,superChapterName,title, data);
                        break;
                    case R.id.tv_home_item_view_tag:
                        ArrayList<ArticleDatas.TagsBean> tags = mDatasList.get(position).getTags();
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
                        MainActivity.start(mActivity);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh();
            }
        });
    }

    @Override
    protected void swipeRefresh() {
        // 防止下拉刷新时,还可以上拉加载
        mCommonListAdapter.setEnableLoadMore(false);
        mCurrPage = resetCurrPage();
        // 下拉刷新
        mPresenter.swipeRefresh(mAuthor);
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
        Toast.makeText(Utils.getApp(), e.toString(), Toast.LENGTH_SHORT).show();
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
        mView.setImageResource(R.drawable.ic_favorite);
        mDatasList.get(mPosition).setCollect(true);
        Toast.makeText(Utils.getApp(), R.string.collect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCollectFail(CommonException e) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getString(R.string.collect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectSuccess() {
        mView.setImageResource(R.drawable.ic_favorite_empty);
        mDatasList.get(mPosition).setCollect(false);
        Toast.makeText(Utils.getApp(), R.string.uncollect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectFail(CommonException e) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getString(R.string.uncollect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(AuthorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contentCollectSuccessFromAuthorEvent(
            ContentCollectSuccessFromAuthorEvent event) {
        mItemIv.setImageResource(R.drawable.ic_favorite);
        mDatasList.get(mItemPosition).setCollect(true);
    }
}
