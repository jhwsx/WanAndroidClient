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
import com.wan.android.R;
import com.wan.android.adapter.CommonListAdapter;
import com.wan.android.base.BaseListFragment;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.constant.SpConstants;
import com.wan.android.content.ContentActivity;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.CommonException;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wzc
 * @date 2018/3/30
 */
public class AuthorFragment extends BaseListFragment implements AuthorContract.View {
    private static final String ARG_AUTHOR = "arg_author";
    private CommonListAdapter mCommonListAdapter;
    private List<ArticleDatas> mDatasList = new ArrayList<>();
    private String mAuthor;
    private AuthorContract.Presenter mPresenter;

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
                String link = datas.getLink();
                String title = datas.getTitle();
                ContentActivity.start(mActivity, title, link, datas.getId());
            }
        });

        mCommonListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_home_item_view_collect:
                        if (TextUtils.isEmpty(PreferenceUtils.getString(Utils.getContext(), SpConstants.KEY_USERNAME, ""))) {
                            Toast.makeText(Utils.getContext(), R.string.login_first, Toast.LENGTH_SHORT).show();
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
        resetCurrPage(mCurrPage);
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
        mView.setImageResource(R.drawable.ic_favorite);
        mDatasList.get(mPosition).setCollect(true);
        Toast.makeText(Utils.getContext(), R.string.collect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCollectFail(CommonException e) {
        Toast.makeText(Utils.getContext(), Utils.getContext().getString(R.string.collect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectSuccess() {
        mView.setImageResource(R.drawable.ic_favorite_empty);
        mDatasList.get(mPosition).setCollect(false);
        Toast.makeText(Utils.getContext(), R.string.uncollect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectFail(CommonException e) {
        Toast.makeText(Utils.getContext(), Utils.getContext().getString(R.string.uncollect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(AuthorContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
