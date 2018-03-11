package com.wan.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.adapter.SearchAdapter;
import com.wan.android.bean.CollectRepsonse;
import com.wan.android.bean.HotkeyResponse;
import com.wan.android.bean.SearchResponse;
import com.wan.android.bean.UncollectRepsonse;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.client.CollectClient;
import com.wan.android.client.HotkeyClient;
import com.wan.android.client.SearchClient;
import com.wan.android.client.UncollectClient;
import com.wan.android.constant.SpConstants;
import com.wan.android.retrofit.RetrofitClient;
import com.wan.android.util.Colors;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.view.MultiSwipeRefreshLayout;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wan.android.WanAndroidApplication.getContext;

/**
 * 搜索页面
 *
 * @author wzc
 * @date 2018/3/6
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private TagFlowLayout mTagFlowLayout;
    private static final String TAG = SearchActivity.class.getSimpleName();
    private ScrollView mScrollView;
    private MultiSwipeRefreshLayout mMultiSwipeRefreshLayout;
    private LoadService mLoadService;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;
    private SearchView mSearchView;
    private TagFlowLayout mTagFlowLayoutHistory;
    private Button mBtnClearHistory;
    private TextView mTvNoHistory;

    public static void start(Context context) {
        Intent starter = new Intent(context, SearchActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_search);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initViews();

        initData();

        initRefreshLayout();

    }

    private void initRefreshLayout() {
        mMultiSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        search(mCurrQuery);
    }

    private void initData() {
        setHotkey();

        setSearchHistory();
    }

    private void setSearchHistory() {
        HashSet<String> hashSetHistory = PreferenceUtils.getStringSet(mContext, SpConstants.KEY_SEARCH_HISTORY);
        final ArrayList<String> historyList = new ArrayList<>(hashSetHistory);
        final ArrayList<Integer> colors = Colors.randomList(historyList.size());
        mTagFlowLayoutHistory.setAdapter(new TagAdapter<String>(historyList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tv, mTagFlowLayoutHistory, false);
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

        if (PreferenceUtils.getStringSet(mContext, SpConstants.KEY_SEARCH_HISTORY).size() > 0) {
            mBtnClearHistory.setVisibility(View.VISIBLE);
            mTvNoHistory.setVisibility(View.GONE);
        } else {
            mTvNoHistory.setVisibility(View.VISIBLE);
            mBtnClearHistory.setVisibility(View.GONE);
        }
    }


    private void setHotkey() {
        HotkeyClient client = RetrofitClient.create(HotkeyClient.class);
        Call<HotkeyResponse> call = client.getHotkey();
        call.enqueue(new Callback<HotkeyResponse>() {
            @Override
            public void onResponse(Call<HotkeyResponse> call, Response<HotkeyResponse> response) {
                HotkeyResponse body = response.body();
                if (body.getErrorcode() != 0) {
                    Log.d(TAG, body.getErrormsg());
                    return;
                }
                final List<HotkeyResponse.Data> dataList = body.getData();
                final ArrayList<Integer> colors = Colors.randomList(dataList.size());
                mTagFlowLayout.setAdapter(new TagAdapter<HotkeyResponse.Data>(dataList) {
                    @Override
                    public View getView(FlowLayout parent, int position, HotkeyResponse.Data d) {
                        TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tv, mTagFlowLayout, false);
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
            public void onFailure(Call<HotkeyResponse> call, Throwable t) {

            }
        });
    }

    private void initViews() {
        mScrollView = (ScrollView) findViewById(R.id.scrollview_activity_search);
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.id_activity_search_flowlayout);
        mTagFlowLayoutHistory = (TagFlowLayout) findViewById(R.id.id_activity_search_history_flowlayout);
        mTvNoHistory = (TextView) findViewById(R.id.tv_search_no_history);
        mBtnClearHistory = (Button) findViewById(R.id.btn_search_clear_history);
        mBtnClearHistory.setOnClickListener(this);
        mMultiSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_activity_search);
        // 获取RecyclerView布局
        View recyclerView = LayoutInflater.from(mContext).inflate(R.layout.recycler_view, null);
        // 获取LoadService,把RecyclerView添加进去
        mLoadService = LoadSir.getDefault().register(recyclerView, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                refresh();
            }
        });
        // 把状态管理页面添加到根布局中
        mMultiSwipeRefreshLayout.addView(mLoadService.getLoadLayout(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 设置可下拉刷新的子view
        mMultiSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
        mMultiSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        mRecyclerView = (RecyclerView) recyclerView.findViewById(R.id.recyclerview_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        initAdapter();
    }

    private List<SearchResponse.Data.Datas> mDatasList = new ArrayList<>();

    private void initAdapter() {
        mSearchAdapter = new SearchAdapter(R.layout.home_item_view, mDatasList);
        // 加载更多
        mSearchAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.d(TAG, "onLoadMoreRequested() called");
                loadMore();
            }
        }, mRecyclerView);
        mSearchAdapter.setEmptyView(R.layout.empty_view);
        mRecyclerView.setAdapter(mSearchAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchResponse.Data.Datas datas = mDatasList.get(position);
                ContentActivity.start(mContext, datas.getTitle(), datas.getLink(), datas.getId());
            }
        });
        mSearchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_home_item_view_collect:
                        if (mDatasList.get(position).getCollect()) {
                            unCollect(view, position);
                        } else {
                            collect(view, position);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void collect(final View view, final int position) {
        CollectClient collectClient = RetrofitClient.create(CollectClient.class);
        Call<CollectRepsonse> call = collectClient.collect(mDatasList.get(position).getId());
        call.enqueue(new Callback<CollectRepsonse>() {
            @Override
            public void onResponse(Call<CollectRepsonse> call, Response<CollectRepsonse> response) {
                CollectRepsonse body = response.body();
                if (body.getErrorcode() != 0) {
                    Toast.makeText(mContext, body.getErrormsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext, R.string.collect_successfully, Toast.LENGTH_SHORT).show();
                mDatasList.get(position).setCollect(true);
                ((ImageView) view).setImageResource(R.drawable.ic_favorite);
            }

            @Override
            public void onFailure(Call<CollectRepsonse> call, Throwable t) {
                Toast.makeText(mContext, getString(R.string.collect_failed) + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void unCollect(final View view, final int position) {
        UncollectClient uncollectClient = RetrofitClient.create(UncollectClient.class);
        Call<UncollectRepsonse> call = uncollectClient.uncollect(mDatasList.get(position).getId());
        call.enqueue(new Callback<UncollectRepsonse>() {
            @Override
            public void onResponse(Call<UncollectRepsonse> call, Response<UncollectRepsonse> response) {
                UncollectRepsonse body = response.body();
                if (body.getErrorcode() != 0) {
                    Toast.makeText(mContext, body.getErrormsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext, R.string.uncollect_successfully, Toast.LENGTH_SHORT).show();
                mDatasList.get(position).setCollect(false);
                ((ImageView) view).setImageResource(R.drawable.ic_favorite_empty);
            }

            @Override
            public void onFailure(Call<UncollectRepsonse> call, Throwable t) {
                Toast.makeText(mContext, getString(R.string.uncollect_failed) + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int mNextPage = 1;

    private void loadMore() {
        SearchClient client = RetrofitClient.create(SearchClient.class);
        Call<SearchResponse> call = client.search(mNextPage, mCurrQuery);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                mNextPage++;
                if (mNextPage < response.body().getData().getPagecount()) {
                    mSearchAdapter.loadMoreComplete();
                } else {
                    mSearchAdapter.loadMoreEnd();
                }

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "response: " + response);
                }

                SearchResponse body = response.body();
                SearchResponse.Data data = body.getData();
                List<SearchResponse.Data.Datas> datas = data.getDatas();
                mDatasList.addAll(datas);
                mSearchAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                mSearchAdapter.loadMoreFail();
                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "t:" + t);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_activity_search_search).getActionView();
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
                HashSet<String> hashSet = PreferenceUtils.getStringSet(mContext, SpConstants.KEY_SEARCH_HISTORY);
                hashSet.add(query);
                PreferenceUtils.putStringSet(mContext, SpConstants.KEY_SEARCH_HISTORY, hashSet);
                setSearchHistory();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
        return true;
    }

    private int mPage = 0;
    private String mCurrQuery;

    private void search(String query) {
        if (TextUtils.isEmpty(query)) {
            mScrollView.setVisibility(View.VISIBLE);
            mMultiSwipeRefreshLayout.setVisibility(View.GONE);
            return;
        }
        mCurrQuery = query;
        SearchClient client = RetrofitClient.create(SearchClient.class);
        Call<SearchResponse> search = client.search(0, query);
        search.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "search response:" + response);
                }
                mSearchAdapter.setEnableLoadMore(true);
                mMultiSwipeRefreshLayout.setRefreshing(false);
                mScrollView.setVisibility(View.GONE);
                mMultiSwipeRefreshLayout.setVisibility(View.VISIBLE);
                SearchResponse body = response.body();
                SearchResponse.Data data = body.getData();
                List<SearchResponse.Data.Datas> datas = data.getDatas();
                if (datas == null) {
                    mLoadService.showCallback(EmptyCallback.class);
                    return;
                }
                mDatasList.clear();
                mDatasList.addAll(datas);
                mSearchAdapter.notifyDataSetChanged();
                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "search onFailure t:" + t);
                }
                mSearchAdapter.setEnableLoadMore(true);
                mMultiSwipeRefreshLayout.setRefreshing(false);
                mLoadService.showCallback(EmptyCallback.class);
                mMultiSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_view, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_clear_history:
                PreferenceUtils.putStringSet(mContext, SpConstants.KEY_SEARCH_HISTORY, new HashSet<String>());
                setSearchHistory();
                break;
            default:
                break;
        }
    }
}
