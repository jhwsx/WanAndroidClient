package com.wan.android.collect;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wan.android.R;
import com.wan.android.adapter.CollectAdapter;
import com.wan.android.author.AuthorActivity;
import com.wan.android.base.BaseListFragment;
import com.wan.android.branch.BranchActivity;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.content.ContentActivity;
import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.CollectDatas;
import com.wan.android.data.bean.CommonException;
import com.wan.android.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wzc
 * @date 2018/3/5
 */
public class CollectionFragement extends BaseListFragment implements CollectContract.View {
    private CollectAdapter mCollectAdapter;
    private CollectContract.Presenter mPresenter;
    private AlertDialog mDialog;

    public static CollectionFragement newInstance() {

        Bundle args = new Bundle();
        CollectionFragement fragment = new CollectionFragement();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        initRefreshLayout();
        swipeRefresh();
    }

    @Override
    protected void swipeRefresh() {
        // 防止下拉刷新时,还可以上拉加载
        mCollectAdapter.setEnableLoadMore(false);
        mPresenter.swipeRefresh();
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh();
            }
        });
    }

    private List<CollectDatas> mDatasList = new ArrayList<>();

    private void initAdapter() {
        mCollectAdapter = new CollectAdapter(R.layout.recycle_item, mDatasList);
        mCollectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(mCollectAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                CollectDatas data = mDatasList.get(position);
                ContentActivity.start(mActivity, data.getTitle(), data.getLink(), data.getId());
            }
        });
        mCollectAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CollectDatas collectDatas = mDatasList.get(position);
                switch (view.getId()) {
                    case R.id.iv_home_item_view_collect:
                        // 取消收藏
                        mPosition = position;
                        mPresenter.uncollect(collectDatas.getId(), collectDatas.getOriginid());
                        break;
                    case R.id.tv_home_item_view_chapter_name:
                        String title = collectDatas.getChaptername();
                        BranchData.Leaf leaf = new BranchData.Leaf();
                        leaf.setChildren(new ArrayList<String>());
                        leaf.setCourseid(collectDatas.getCourseid());
                        leaf.setId(collectDatas.getChapterid());
                        leaf.setName(collectDatas.getChaptername());
                        ArrayList<BranchData.Leaf> data = new ArrayList<BranchData.Leaf>();
                        data.add(leaf);
                        BranchActivity.start(mActivity, title, data);
                        break;
                    case R.id.tv_home_item_view_author:
                        AuthorActivity.start(mActivity, collectDatas.getAuthor());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private int mCurrPage = 1;

    private void loadMore() {
        mPresenter.loadMore(mCurrPage);
        mCurrPage++;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_collect, menu);
    }

    private int mPosition;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_collect_add:
                LayoutInflater factory = LayoutInflater.from(mActivity);
                final View textEntryView = factory.inflate(R.layout.collection_add_collect_dialog, null);
                final View textEntryTitle = factory.inflate(R.layout.collection_add_title_dialog, null);
                final EditText etTitle = (EditText) textEntryView.findViewById(R.id.title_edit);
                final EditText etAuthor = (EditText) textEntryView.findViewById(R.id.author_edit);
                final EditText etLink = (EditText) textEntryView.findViewById(R.id.link_edit);
                mDialog = new AlertDialog.Builder(mActivity)
                        .setCustomTitle(textEntryTitle)
                        .setView(textEntryView)
                        .setCancelable(false)
                        .setPositiveButton(R.string.alert_dialog_ok, null)
                        .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                mDialog.show();

                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = etTitle.getText().toString();
                        String author = etAuthor.getText().toString();
                        String link = etLink.getText().toString();
                        if (TextUtils.isEmpty(title)) {
                            Toast.makeText(mActivity, R.string.title_cannot_be_null, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(link)) {
                            Toast.makeText(mActivity, R.string.link_cannot_be_null, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mPresenter.addCollect(title, author, link);
                    }
                });
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSwipeRefreshSuccess(List<CollectDatas> datas) {
        mCollectAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        if (datas.size() == 0) {
            mLoadService.showCallback(EmptyCallback.class);
            return;
        }
        mDatasList.clear();
        mDatasList.addAll(datas);
        mCollectAdapter.notifyDataSetChanged();
        mLoadService.showSuccess();
    }

    @Override
    public void showSwipeRefreshFail(CommonException e) {
        Toast.makeText(Utils.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        mCollectAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showCallback(EmptyCallback.class);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
    }

    @Override
    public void showLoadMoreSuccess(List<CollectDatas> datas) {
        mDatasList.addAll(datas);
        mCollectAdapter.notifyDataSetChanged();
        mLoadService.showSuccess();
    }

    @Override
    public void showLoadMoreFail(CommonException e) {
        Toast.makeText(Utils.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        mCollectAdapter.loadMoreFail();
    }

    @Override
    public void showLoadMoreComplete() {
        mCollectAdapter.loadMoreComplete();
    }

    @Override
    public void showLoadMoreEnd() {
        mCollectAdapter.loadMoreEnd();
    }

    @Override
    public void showUncollectSuccess() {
        Toast.makeText(Utils.getContext(), R.string.uncollect_successfully, Toast.LENGTH_SHORT).show();
        mDatasList.remove(mPosition);
        mCollectAdapter.notifyItemRemoved(mPosition);
    }

    @Override
    public void showUncollectFail(CommonException e) {
        Toast.makeText(Utils.getContext(), mActivity.getString(R.string.uncollect_failed, e.toString()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddCollectSuccess(CollectDatas data) {
        mDatasList.add(0, data);
        mCollectAdapter.notifyItemInserted(0);
        mRecyclerView.scrollToPosition(0);
        Toast.makeText(Utils.getContext(), R.string.add_collect_successfully, Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
    }

    @Override
    public void showAddCollectFail(CommonException e) {
        Toast.makeText(Utils.getContext(), mActivity.getString(R.string.add_collect_failed, e.toString()), Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
    }

    @Override
    public void setPresenter(CollectContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
