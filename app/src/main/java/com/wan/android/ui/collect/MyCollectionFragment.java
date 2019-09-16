package com.wan.android.ui.collect;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.data.network.model.CollectDatas;
import com.wan.android.data.network.model.ContentData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.adapter.CollectAdapter;
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

/**
 * 我的收藏 Fragment
 *
 * @author wzc
 * @date 2018/8/28
 */
public class MyCollectionFragment extends BaseFragment
        implements MyCollectionContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private static final String TAG = MyCollectionFragment.class.getSimpleName();
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Inject
    CollectAdapter mAdapter;
    @Inject
    LayoutInflater mInflater;
    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    HorizontalDividerItemDecoration mHorizontalDividerItemDecoration;
    @Inject
    MyCollectionPresenter<MyCollectionContract.View> mPresenter;
    public static final int REQUEST_ADD_COLLECT_CODE = 0x1;
    private LoadService mLoadService;

    public static MyCollectionFragment newInstance() {
        Bundle args = new Bundle();
        MyCollectionFragment fragment = new MyCollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                        mPresenter.swipeRefresh();
                    }
                });
        return mLoadService.getLoadLayout();
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
        mPresenter.swipeRefresh();
    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void showSwipeRefreshSuccess(List<CollectDatas> datas) {
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
    public void showSwipeRefreshNoData() {
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showCallback(EmptyCallback.class);
    }

    @Override
    public void showLoadMoreSuccess(List<CollectDatas> datas) {
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
    public void showUncollectMyCollectionArticleSuccess() {
        showMessage(R.string.uncollect_successfully);
        mAdapter.remove(mClickCollectPosition);
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        mPresenter.swipeRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMore();
    }

    private int mClickCollectPosition;

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.iv_home_item_view_collect) {
            boolean isLogin = mPresenter.getLoginStaus();
            if (!isLogin) {
                onError(R.string.login_first);
                LoginActivity.start(getBaseActivity());
                return;
            }
            mClickCollectPosition = position;
            CollectDatas articleDatas = (CollectDatas) adapter.getData().get(position);
            int id = articleDatas.getId();
            mPresenter.uncollectMyCollectionArticle(id, articleDatas.getOriginid());
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<CollectDatas> data = adapter.getData();
        CollectDatas articleDatas = data.get(position);
        Long id = Long.valueOf(articleDatas.getId());
        ContentData contentData = new ContentData(id,
                articleDatas.getTitle(), articleDatas.getLink(), true);
        View title = view.findViewById(R.id.tv_home_item_view_title);
        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra(ContentActivity.EXTRA_CONTENT_DATA, contentData);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), title, getString(R.string.shared_title));
        getActivity().startActivity(intent, transitionActivityOptions.toBundle());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_collect, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_collect_add) {
            AddCollectArticleDialog dialog = new AddCollectArticleDialog();
            dialog.setTargetFragment(MyCollectionFragment.this, REQUEST_ADD_COLLECT_CODE);
            dialog.show(getFragmentManager(), AddCollectArticleDialog.class.getSimpleName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_ADD_COLLECT_CODE) {
            return;
        }
        CollectDatas datas = (CollectDatas) data.getSerializableExtra(
                AddCollectArticleDialog.RESPONSE_ADD_COLLECT_DATA);
        mAdapter.addData(0, datas);
        mAdapter.notifyItemInserted(0);
    }
}
