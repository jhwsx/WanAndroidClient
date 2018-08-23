package com.wan.android.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.wan.android.R;
import com.wan.android.data.network.model.HotkeyData;
import com.wan.android.data.network.model.SearchHistoryData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.ui.event.SearchResultFragmentDestroyEvent;
import com.wan.android.util.Colors;
import com.wan.android.util.InputMethodUtils;
import com.wan.android.util.constant.AppConstants;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * 搜索片段
 *
 * @author wzc
 * @date 2018/8/21
 */
public class SearchFragment extends BaseFragment implements SearchContract.View {
    public static final String TAG = SearchFragment.class.getSimpleName();
    @BindView(R.id.scrollview_activity_search)
    ScrollView mScrollView;
    @BindView(R.id.id_activity_search_flowlayout)
    TagFlowLayout mTagFlowLayoutHot;
    @BindView(R.id.id_activity_search_history_flowlayout)
    TagFlowLayout mTagFlowLayoutHistory;
    @BindView(R.id.tv_search_no_history)
    TextView mTvNoHistory;
    @BindView(R.id.btn_search_clear_history)
    Button mBtnClearHistory;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Inject
    SearchPresenter<SearchContract.View> mPresenter;
    @Inject
    LayoutInflater mInflater;
    private SearchView mSearchView;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.d("onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("onDetach");
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        Timber.d("onDestroy");
    }


    @Override
    protected void setUp(View view) {
        mPresenter.getHotkey();
        mPresenter.getDbSearchHistory();
        mPresenter.addRxBindingSubscribe(RxView.clicks(mBtnClearHistory)
        .throttleFirst(AppConstants.CLICK_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
        .subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Timber.d("clear search history");
                mPresenter.deleteDbSearchHistory();
            }
        }));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_search);
        mSearchView = (SearchView) searchItem.getActionView();
        try {
            Field mSearchSrcTextViewField = SearchView.class.getDeclaredField("mSearchSrcTextView");
            mSearchSrcTextViewField.setAccessible(true);
            SearchView.SearchAutoComplete searchAutoComplete
                    = (SearchView.SearchAutoComplete) mSearchSrcTextViewField.get(mSearchView);
            searchAutoComplete.setTextSize(getResources().getDimensionPixelSize(R.dimen.dp_8));
            searchAutoComplete.setHint(R.string.search_hint);
        } catch (Exception e) {
            // ignored
        }
        // 展开searchView
        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }

    public void search(String query) {
        if (TextUtils.isEmpty(query)) {
            Timber.d("search query is empty");
            return;
        }
        SearchHistoryData searchHistoryData = new SearchHistoryData();
        searchHistoryData.setKey(query);
        mPresenter.saveSearchHistory2Db(searchHistoryData);
        InputMethodUtils.hideInputMethod(mSearchView);
        FragmentManager fm = getBaseActivity().getSupportFragmentManager();
        SearchResultFragment searchResultFragment =
                (SearchResultFragment) fm.findFragmentByTag(SearchResultFragment.TAG);
        if (searchResultFragment == null) {
            searchResultFragment = SearchResultFragment.newInstance(query);
            fm.beginTransaction()
                    .add(R.id.contentFrame, searchResultFragment, SearchResultFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            searchResultFragment.query(query);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search_search) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void showHotkeySuccess(List<HotkeyData> data) {
        Timber.d("showHotkeySuccess: size = %s", data.size());
        final List<String> values = new ArrayList<>(data.size());
        final ArrayList<Integer> colors = Colors.randomList(data.size());
        for (HotkeyData hotkeyData : data) {
            values.add(hotkeyData.getName());
        }
        mTagFlowLayoutHot.setAdapter(new TagAdapter<String>(values) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_layout,
                        mTagFlowLayoutHot, false);
                tv.setText(s);
                tv.setTextColor(colors.get(position));
                return tv;
            }
        });
        mTagFlowLayoutHot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mSearchView.setQuery(values.get(position), false);
                search(values.get(position));
                return true;
            }
        });
    }

    @Override
    public void showGetDbSearchHistorySuccess(List<SearchHistoryData> data) {
        if (data.isEmpty()) {
            setHistoryVisiblity(true);
            return;
        }
        setHistoryVisiblity(false);
        final List<String> values = new ArrayList<>();
        for (SearchHistoryData shd : data) {
            values.add(shd.getKey());
        }
        final List<Integer> colors = Colors.randomList(data.size());
        mTagFlowLayoutHistory.setAdapter(new TagAdapter<String>(values) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_layout,
                        mTagFlowLayoutHot, false);
                tv.setText(s);
                tv.setTextColor(colors.get(position));
                return tv;
            }
        });
        mTagFlowLayoutHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mSearchView.setQuery(values.get(position), false);
                search(values.get(position));
                return true;
            }
        });
    }

    @Override
    public void showSaveSearchHistory2DbSuccess() {
        mPresenter.getDbSearchHistory();
    }

    private void setHistoryVisiblity(boolean isNoHistory) {
        mBtnClearHistory.setVisibility(isNoHistory ? View.GONE : View.VISIBLE);
        mTvNoHistory.setVisibility(isNoHistory ? View.VISIBLE : View.GONE);
        mTagFlowLayoutHistory.setVisibility(isNoHistory ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showDeleteDbSearchHistorySuccess() {
        mPresenter.getDbSearchHistory();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchResultFragmentDestroyEvent(SearchResultFragmentDestroyEvent event) {
        if (mSearchView != null) {
            mSearchView.setQuery(AppConstants.EMPTY_STRING, false);
        }
    }
}
