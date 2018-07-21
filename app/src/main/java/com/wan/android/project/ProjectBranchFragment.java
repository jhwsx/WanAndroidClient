package com.wan.android.project;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wan.android.R;
import com.wan.android.adapter.ProjectAdapter;
import com.wan.android.base.BaseListFragment;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.constant.FromTypeConstants;
import com.wan.android.constant.SpConstants;
import com.wan.android.content.ContentActivity;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ProjectTreeBranchData;
import com.wan.android.data.event.ContentCollectSuccessFromProjectEvent;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * @author wzc
 * @date 2018/7/20
 */
public class ProjectBranchFragment extends BaseListFragment
        implements ProjectBranchContract.View {
    private static final String ARGS_PROJECT_TREE_BRANCH_DATA
            = "com.wan.android.args_project_tree_branch_data";
    private ProjectAdapter mProjectAdapter;
    private ImageView mIvItem;
    private int mPosition;

    public static ProjectBranchFragment newInstance(ProjectTreeBranchData data) {

        Bundle args = new Bundle();
        args.putSerializable(ARGS_PROJECT_TREE_BRANCH_DATA, data);
        ProjectBranchFragment fragment = new ProjectBranchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ProjectTreeBranchData mProjectTreeBranchData;
    private ProjectBranchContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mProjectTreeBranchData = (ProjectTreeBranchData) getArguments()
                    .getSerializable(ARGS_PROJECT_TREE_BRANCH_DATA);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new ProjectBranchPresenter(this);
        initAdapter();
        initRefreshLayout();
        swipeRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh();
            }
        });
    }

    private Integer mCurrPage = 1;
    // 点击收藏的收藏图片
    private ImageView mCollectIv;
    // 点击收藏的位置
    private int mCollectPosition;
    private void initAdapter() {
        mProjectAdapter = new ProjectAdapter(R.layout.project_recycle_item);
        mRecyclerView.setAdapter(mProjectAdapter);
        mProjectAdapter.setEnableLoadMore(false);
        mProjectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrPage++;
                mPresenter.loadMore(mCurrPage, mProjectTreeBranchData.getId());
            }
        }, mRecyclerView);
        mProjectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ArticleDatas> data = mProjectAdapter.getData();
                ArticleDatas datas = data.get(position);
                String link = datas.getLink();
                String title = datas.getTitle();
                mIvItem = (ImageView) view.findViewById(R.id.iv_recycle_project_item_collect);
                mPosition = position;
                ContentActivity.start(mActivity, FromTypeConstants.FROM_PROJECT_FRAGMENT, title, link, datas.getId());
            }
        });
        mProjectAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<ArticleDatas> data = mProjectAdapter.getData();
                ArticleDatas articleDatas = data.get(position);
                switch (view.getId()) {
                    case R.id.iv_recycle_project_item_collect:
                        if (TextUtils.isEmpty(PreferenceUtils.getString(Utils.getApp(), SpConstants.KEY_USERNAME, ""))) {
                            Toast.makeText(Utils.getApp(), R.string.login_first, Toast.LENGTH_SHORT).show();
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
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void swipeRefresh() {
        // 防止下拉刷新时,还可以上拉加载
        mProjectAdapter.setEnableLoadMore(false);
        mCurrPage = resetCurrPage();
        mPresenter.swipeRefresh(mProjectTreeBranchData.getId());
    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showSwipeRefreshSuccess(List<ArticleDatas> data) {
        mProjectAdapter.setEnableLoadMore(true);
        mProjectAdapter.setNewData(data);
        mProjectAdapter.disableLoadMoreIfNotFullPage();
        mProjectAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showSuccess();
    }

    @Override
    public void showSwipeRefreshFail(CommonException e) {
        mProjectAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showCallback(EmptyCallback.class);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
    }

    @Override
    public void showLoadMoreSuccess(List<ArticleDatas> datas) {
        mProjectAdapter.addData(datas);
        mProjectAdapter.notifyDataSetChanged();
        mLoadService.showSuccess();
    }

    @Override
    public void showLoadMoreFail(CommonException e) {
        mProjectAdapter.loadMoreFail();
    }

    @Override
    public void showLoadMoreComplete() {
        mProjectAdapter.loadMoreComplete();
    }

    @Override
    public void showLoadMoreEnd() {
        mProjectAdapter.loadMoreEnd();
    }

    @Override
    public void showCollectSuccess() {
        mCollectIv.setImageResource(R.drawable.ic_favorite);
        mProjectAdapter.getData().get(mCollectPosition).setCollect(true);
        Toast.makeText(Utils.getApp(), R.string.collect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCollectFail(CommonException e) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getString(R.string.collect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectSuccess() {
        mCollectIv.setImageResource(R.drawable.ic_favorite_empty);
        mProjectAdapter.getData().get(mCollectPosition).setCollect(false);
        Toast.makeText(Utils.getApp(), R.string.uncollect_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUncollectFail(CommonException e) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getString(R.string.uncollect_failed) + " : " + e.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setPresenter(ProjectBranchContract.Presenter presenter) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contentCollectSuccessFromProjectEvent(
            ContentCollectSuccessFromProjectEvent event) {
        mIvItem.setImageResource(R.drawable.ic_favorite);
        mProjectAdapter.getData().get(mPosition).setCollect(true);

    }
}
