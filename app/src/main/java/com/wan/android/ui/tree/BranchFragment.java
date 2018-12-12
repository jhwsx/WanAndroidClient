package com.wan.android.ui.tree;

import android.content.Context;
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
import com.wan.android.ui.adapter.CommonListAdapter;
import com.wan.android.ui.base.BaseViewPagerFragment;
import com.wan.android.ui.content.ContentActivity;
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
 * 知识体系下的文章 Fragment
 *
 * @author wzc
 * @date 2018/8/23
 */
public class BranchFragment extends BaseViewPagerFragment
        implements BranchContract.View,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemChildClickListener {
    private static final String ARGS_ID = "com.wan.android.args_id";
    private static final String TAG = BranchFragment.class.getSimpleName();
    public static BranchFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ARGS_ID, id);
        BranchFragment fragment = new BranchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Inject
    CommonListAdapter mAdapter;
    @Inject
    LayoutInflater mInflater;
    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    HorizontalDividerItemDecoration mHorizontalDividerItemDecoration;
    @Inject
    BranchContract.Presenter<BranchContract.View> mPresenter;

    private LoadService mLoadService;
    private int mId = -1;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getArguments() != null && mId == -1) {
            mId = getArguments().getInt(ARGS_ID);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("onAttach, %s", this.toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate, %s", this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView, %s", this);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated, %s", this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart, %s", this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume, %s", this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause, %s", this);
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
        return TAG + mId;
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView, %s", this);
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<ArticleDatas> data = adapter.getData();
        ArticleDatas articleDatas = data.get(position);
        ContentData contentData = new ContentData(articleDatas.getId(),
                articleDatas.getTitle(), articleDatas.getLink(), articleDatas.isCollect());

        View title = view.findViewById(R.id.tv_home_item_view_title);
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
    public void showCollectInSiteArticleSuccess() {
        showMessage(R.string.collect_successfully);
        setCollectState(true);
    }

    @Override
    public void showUncollectArticleListArticleSuccess() {
        showMessage(R.string.uncollect_successfully);
        setCollectState(false);
    }

    private int mClickCollectPositoin;
    private ImageView mIvCollect;

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
