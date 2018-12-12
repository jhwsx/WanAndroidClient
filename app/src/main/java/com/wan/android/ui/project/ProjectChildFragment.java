package com.wan.android.ui.project;

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
import com.wan.android.data.network.model.ContentData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.adapter.ProjectAdapter;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.ui.content.ContentActivity;
import com.wan.android.ui.loadcallback.EmptyCallback;
import com.wan.android.ui.loadcallback.LoadingCallback;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;
import com.wan.android.ui.login.LoginActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @author wzc
 * @date 2018/8/27
 */
public class ProjectChildFragment extends BaseFragment
        implements ProjectChildContract.View,
        BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemChildClickListener {
    private static final String TAG = ProjectChildFragment.class.getSimpleName();
    private static final String ARGS_ID = "com.wan.android.args_project_id";
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Inject
    ProjectAdapter mAdapter;
    @Inject
    LayoutInflater mInflater;
    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    HorizontalDividerItemDecoration mHorizontalDividerItemDecoration;
    @Inject
    ProjectChildContract.Presenter<ProjectChildContract.View> mPresenter;
    private LoadService mLoadService;
    private int mId;
    private int mClickCollectPositoin;
    private ImageView mIvCollect;

    public static ProjectChildFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ARGS_ID, id);
        ProjectChildFragment fragment = new ProjectChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ARGS_ID);
        }
    }

    @Override
    protected void setUp(View view) {
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
        mPresenter.swipeRefresh(mId);
    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    @Override
    protected boolean hasChildFragment() {
        return true;
    }

    @Override
    public void scrollToTop() {
        if (mLinearLayoutManager.findFirstVisibleItemPosition() > 20) {
            mRecyclerView.scrollToPosition(0);
        } else {
            mRecyclerView.smoothScrollToPosition(0);
        }
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
                        mPresenter.swipeRefresh(mId);
                    }
                });
        return mLoadService.getLoadLayout();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void showSwipeRefreshSuccess(List<ArticleDatas> datas) {
        Timber.d("showSwipeRefreshSuccess: size=%s", datas.size());
        mAdapter.setNewData(datas);
        mLoadService.showSuccess();
        mAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
    }

    @Override
    public void showSwipeRefreshNoData() {
        Timber.d("showSwipeRefreshNoData");
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showCallback(EmptyCallback.class);
    }

    @Override
    public void showSwipeRefreshFail() {
        Timber.d("showSwipeRefreshFail");
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        if (mAdapter.getData().isEmpty()) {
            mLoadService.showCallback(NetworkErrorCallback.class);
        }
    }

    @Override
    public void showLoadMoreSuccess(List<ArticleDatas> datas) {
        Timber.d("showLoadMoreSuccess: size=%s", datas.size());
        mAdapter.addData(datas);
    }

    @Override
    public void showLoadMoreFail() {
        Timber.d("showLoadMoreFail");
        mAdapter.loadMoreFail();
    }

    @Override
    public void showLoadMoreComplete() {
        Timber.d("showLoadMoreComplete");
        mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoadMoreEnd() {
        Timber.d("showLoadMoreEnd");
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<ArticleDatas> data = adapter.getData();
        ArticleDatas articleDatas = data.get(position);
        ContentData contentData = new ContentData(articleDatas.getId(),
                articleDatas.getTitle(), articleDatas.getLink(), articleDatas.isCollect());

        View title = view.findViewById(R.id.tv_recycle_project_item_title);
        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra(ContentActivity.EXTRA_CONTENT_DATA, contentData);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getBaseActivity(), title, getString(R.string.shared_title));
        getActivity().startActivity(intent, transitionActivityOptions.toBundle());
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMore(mId);
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        mPresenter.swipeRefresh(mId);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        if (view.getId() == R.id.iv_recycle_project_item_collect) {
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
