package com.wan.android.ui.navigation;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wan.android.R;
import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.data.network.model.ContentData;
import com.wan.android.data.network.model.NavigationRightData;
import com.wan.android.ui.adapter.NavigationRightAdapter;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.ui.content.ContentActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wzc
 * @date 2018/8/24
 */
public class NavigationRightFragment extends BaseFragment
        implements BaseQuickAdapter.OnItemClickListener,
        OnNavigationRightGroupIdChangeListener {

    private static final String TAG = NavigationRightFragment.class.getSimpleName();
    private NavigationRightAdapter mAdapter;
    private GridLayoutManager mManager;
    private ItemHeaderDecoration mItemHeaderDecoration;
    private NavigationFragment mNavigationFragment;

    public static NavigationRightFragment newInstance() {
        Bundle args = new Bundle();
        NavigationRightFragment fragment = new NavigationRightFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recyclerview_navigation_right_fragment)
    RecyclerView mRecyclerViewRight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigationFragment = (NavigationFragment) getParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_right_fragment,
                container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    private List<NavigationRightData> mData = new ArrayList<>();

    @Override
    protected void setUp(View view) {


    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    @Override
    protected boolean hasChildFragment() {
        return true;
    }

    public void setData(List<NavigationRightData> rightData) {
        mData.addAll(rightData);
        mAdapter = new NavigationRightAdapter(mData);
        mManager = new GridLayoutManager(getActivity(), 2);
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return mData.get(position).isTitle() ? 2 : 1;
            }
        });
        mRecyclerViewRight.setLayoutManager(mManager);
        mItemHeaderDecoration = new ItemHeaderDecoration(getBaseActivity(), mData);
        mItemHeaderDecoration.setOnNavigationRightGroupIdChangeListener(this);
        mRecyclerViewRight.addItemDecoration(mItemHeaderDecoration);
        mRecyclerViewRight.addOnScrollListener(new RecyclerViewOnScrollListener());
        mRecyclerViewRight.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private int mScrollToPosition;
    private boolean mIsMoved = false;

    public void smoothScrollToPosition(int scrollToPosition) {
        mScrollToPosition = scrollToPosition;
        mRecyclerViewRight.stopScroll();
        int firstVisibleItemPosition = mManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = mManager.findLastVisibleItemPosition();
        if (scrollToPosition <= firstVisibleItemPosition) {
            mRecyclerViewRight.scrollToPosition(scrollToPosition);
        } else if (scrollToPosition <= lastVisibleItemPosition) {
            int top = mRecyclerViewRight.getChildAt(scrollToPosition - firstVisibleItemPosition).getTop();
            mRecyclerViewRight.scrollBy(0, top);
        } else {
            mRecyclerViewRight.scrollToPosition(scrollToPosition);
            mIsMoved = true;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<NavigationRightData> data = mAdapter.getData();
        NavigationRightData nrd = data.get(position);
        if (!nrd.isTitle()) {
            ArticleDatas ad = nrd.getArticleDatas();
            ContentData cd = new ContentData(ad.getId(), ad.getTitle(), ad.getLink(), ad.isCollect());
            ContentActivity.start(getBaseActivity(), cd);
        }
    }

    @Override
    public void onNavigationRightGroupIdChanged(int groupId) {
        mNavigationFragment.onNavigationRightHeaderGroupIdChanged(groupId);
    }

    class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mIsMoved) {
                mIsMoved = false;
                int n = mScrollToPosition - mManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRecyclerViewRight.getChildCount()) { // mRv.getChildCount() 返回的是当前在屏幕上的 item 的数目
                    int top = mRecyclerViewRight.getChildAt(n).getTop(); // 最后一个可见的item 距离顶部的距离
                    mRecyclerViewRight.scrollBy(0, top); // 移动最后一个可见的item距离顶部的距离
                }
            }
        }


    }
}


