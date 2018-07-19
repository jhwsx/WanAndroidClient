package com.wan.android.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.adapter.CommonListAdapter;
import com.wan.android.author.AuthorActivity;
import com.wan.android.branch.BranchActivity;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.constant.SpConstants;
import com.wan.android.content.ContentActivity;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ContentCollectEvent;
import com.wan.android.data.bean.HotkeyData;
import com.wan.android.loginregister.LoginActivity;
import com.wan.android.util.Colors;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.util.Utils;
import com.wan.android.view.MultiSwipeRefreshLayout;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * @author wzc
 * @date 2018/3/29
 */
public class SearchFragment extends Fragment implements View.OnClickListener, SearchContract.View {

    private ScrollView mScrollView;
    private TagFlowLayout mTagFlowLayout;
    private TagFlowLayout mTagFlowLayoutHistory;
    private TextView mTvNoHistory;
    private Button mBtnClearHistory;
    private MultiSwipeRefreshLayout mMultiSwipeRefreshLayout;
    private LoadService mLoadService;
    private RecyclerView mRecyclerView;
    private CommonListAdapter mCommonListAdapter;
    private SearchContract.Presenter mPresenter;
    private SearchView mSearchView;
    private String mCurrQuery;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        initData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_activity_search_search).getActionView();
        try {
            Field mSearchSrcTextViewField = SearchView.class.getDeclaredField("mSearchSrcTextView");
            mSearchSrcTextViewField.setAccessible(true);
            SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) mSearchSrcTextViewField.get(mSearchView);
            searchAutoComplete.setTextSize(getResources().getDimensionPixelSize(R.dimen.dp_8));
            searchAutoComplete.setHint(R.string.search_hint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 展开searchView
        mSearchView.setIconified(false);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                HashSet<String> hashSet = PreferenceUtils.getStringSet(mActivity, SpConstants.KEY_SEARCH_HISTORY);
                hashSet.add(query);
                PreferenceUtils.putStringSet(mActivity, SpConstants.KEY_SEARCH_HISTORY, hashSet);
                setSearchHistory();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void search(String query) {
        if (TextUtils.isEmpty(query)) {
            mScrollView.setVisibility(View.VISIBLE);
            mMultiSwipeRefreshLayout.setVisibility(View.GONE);
            return;
        }
        resetCurrPage();
        mCurrQuery = query;
        mCommonListAdapter.setEnableLoadMore(false);
        mPresenter.swipeRefresh(query);
    }

    private void initData() {
        // 获取热门搜索
        mPresenter.fetchHotkey();

        setSearchHistory();
    }

    private void setSearchHistory() {
        HashSet<String> hashSetHistory = PreferenceUtils.getStringSet(mActivity, SpConstants.KEY_SEARCH_HISTORY);
        final ArrayList<String> historyList = new ArrayList<>(hashSetHistory);
        final ArrayList<Integer> colors = Colors.randomList(historyList.size());
        mTagFlowLayoutHistory.setAdapter(new TagAdapter<String>(historyList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.flowlayout_item, mTagFlowLayoutHistory, false);
                textView.setText(s);
                textView.setTextColor(colors.get(position));
                return textView;
            }
        });
        mTagFlowLayoutHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mSearchView.setQuery(historyList.get(position), true);
                return true;
            }
        });

        if (PreferenceUtils.getStringSet(mActivity, SpConstants.KEY_SEARCH_HISTORY).size() > 0) {
            mBtnClearHistory.setVisibility(View.VISIBLE);
            mTvNoHistory.setVisibility(View.GONE);
        } else {
            mTvNoHistory.setVisibility(View.VISIBLE);
            mBtnClearHistory.setVisibility(View.GONE);
        }
    }

    private void initViews(View view) {
        mScrollView = (ScrollView) view.findViewById(R.id.scrollview_activity_search);
        mTagFlowLayout = (TagFlowLayout) view.findViewById(R.id.id_activity_search_flowlayout);
        mTagFlowLayoutHistory = (TagFlowLayout) view.findViewById(R.id.id_activity_search_history_flowlayout);
        mTvNoHistory = (TextView) view.findViewById(R.id.tv_search_no_history);
        mBtnClearHistory = (Button) view.findViewById(R.id.btn_search_clear_history);
        mBtnClearHistory.setOnClickListener(this);
        mMultiSwipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_activity_search);
        // 获取RecyclerView布局
        View recyclerView = LayoutInflater.from(mActivity).inflate(R.layout.recycler_view, null);
        // 获取LoadService,把RecyclerView添加进去
        mLoadService = LoadSir.getDefault().register(recyclerView, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                search(mCurrQuery);
            }
        });
        // 把状态管理页面添加到根布局中
        mMultiSwipeRefreshLayout.addView(mLoadService.getLoadLayout(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 设置可下拉刷新的子view
        mMultiSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
        mMultiSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mRecyclerView = (RecyclerView) recyclerView.findViewById(R.id.recyclerview_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                .color(R.color.color_ef).sizeResId(R.dimen.px_1).build());
        mMultiSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                search(mCurrQuery);
            }
        });
        initAdapter();
    }

    private List<ArticleDatas> mDatasList = new ArrayList<>();
    private int mCurrPage = 1;
    // 点击收藏的收藏图片
    private ImageView mCollectIv;
    // 点击收藏的位置
    private int mCollectPosition;
    // 点击条目的收藏图片
    private ImageView mItemIv;
    // 点击条目的位置
    private int mItemPosition;

    private void initAdapter() {
        mCommonListAdapter = new CommonListAdapter(R.layout.recycle_item, mDatasList);
        // 加载更多
        mCommonListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore(mCurrPage, mCurrQuery);
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
                ContentActivity.start(mActivity, datas.getTitle(), datas.getLink(), datas.getId());
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
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_clear_history:
                PreferenceUtils.putStringSet(mActivity, SpConstants.KEY_SEARCH_HISTORY, new HashSet<String>());
                setSearchHistory();
                break;
            default:
                break;
        }
    }

    @Override
    public void showHotkeySuccess(final List<HotkeyData> dataList) {
        final ArrayList<Integer> colors = Colors.randomList(dataList.size());
        mTagFlowLayout.setAdapter(new TagAdapter<HotkeyData>(dataList) {
            @Override
            public View getView(FlowLayout parent, int position, HotkeyData d) {
                TextView textView = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.flowlayout_item, mTagFlowLayout, false);
                textView.setText(d.getName());
                textView.setTextColor(colors.get(position));
                return textView;
            }
        });
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mSearchView.setQuery(dataList.get(position).getName(), true);
                return true;
            }
        });
    }

    @Override
    public void showHotkeyFail(CommonException e) {
        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSwipeRefreshSuccess(List<ArticleDatas> datas) {
        mCommonListAdapter.setEnableLoadMore(true);
        mMultiSwipeRefreshLayout.setRefreshing(false);
        mScrollView.setVisibility(View.GONE);
        mMultiSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mDatasList.clear();
        mDatasList.addAll(datas);
        mCommonListAdapter.notifyDataSetChanged();
        mLoadService.showSuccess();
    }

    @Override
    public void showSwipeRefreshFail(CommonException e) {
        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
        mCommonListAdapter.setEnableLoadMore(true);
        mMultiSwipeRefreshLayout.setRefreshing(false);
        mLoadService.showCallback(EmptyCallback.class);
        mMultiSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
    }

    @Override
    public void showLoadMoreSuccess(List<ArticleDatas> datas) {
        mDatasList.addAll(datas);
        mCommonListAdapter.notifyDataSetChanged();
        mLoadService.showSuccess();
    }

    @Override
    public void showLoadMoreFail(CommonException e) {
        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
        mCommonListAdapter.loadMoreFail();
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
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void resetCurrPage() {
        mCurrPage = 1;
    }
}
