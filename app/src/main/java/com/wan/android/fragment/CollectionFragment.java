package com.wan.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.activity.LoginActivity;
import com.wan.android.adapter.CollectAdapter;
import com.wan.android.bean.CollectListResponse;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.client.CollectListClient;
import com.wan.android.constant.SpConstants;
import com.wan.android.util.PreferenceUtils;
import com.wan.android.view.MultiSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

/**
 * @author wzc
 * @date 2018/2/1
 */
public class CollectionFragment extends BaseFragment {
    private static final String TAG = CollectionFragment.class.getSimpleName();
    private LinearLayout mLinearLayoutLogin;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private LoadService mLoadService;
    private RecyclerView mRecyclerView;
    private CollectAdapter mTreeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        mLinearLayoutLogin = (LinearLayout) view.findViewById(R.id.linearlayout_fragment_collect_login);
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_fragment_collect);
        String username = PreferenceUtils.getString(getActivity(), SpConstants.KEY_USERNAME, "");
        if (TextUtils.isEmpty(username)) {
            mLinearLayoutLogin.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.INVISIBLE);
        } else {
            mLinearLayoutLogin.setVisibility(View.INVISIBLE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        // 获取RecyclerView布局
        View recyclerView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, null);
        // 获取LoadService,把RecyclerView添加进去
        mLoadService = LoadSir.getDefault().register(recyclerView, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
//                refresh();
            }
        });
        // 把状态管理页面添加到根布局中
        mSwipeRefreshLayout.addView(mLoadService.getLoadLayout(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mSwipeRefreshLayout.setSwipeableChildren(R.id.recyclerview_fragment_home, R.id.ll_error, R.id.ll_empty, R.id.ll_loading);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);

        mRecyclerView = (RecyclerView) recyclerView.findViewById(R.id.recyclerview_fragment_home);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        initAdapter();
        if (!TextUtils.isEmpty(username)) {
            refresh();
        }

        TextView textView = (TextView) view.findViewById(R.id.tv_login);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), 2000);
            }
        });
        return view;
    }

    private void refresh() {
        String API_BASE_URL = "http://wanandroid.com/";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();
        CollectListClient client = retrofit.create(CollectListClient.class);
        Call<CollectListResponse> call = client.getCollect(0);
        call.enqueue(new Callback<CollectListResponse>() {
            @Override
            public void onResponse(Call<CollectListResponse> call, Response<CollectListResponse> response) {
                Log.d("CollectionFragment", "response:" + response);
            }

            @Override
            public void onFailure(Call<CollectListResponse> call, Throwable t) {
                Log.d("CollectionFragment", "t:" + t);
            }
        });
    }

    private List<CollectListResponse.Data.Datas> mDatasList = new ArrayList<>();

    private void initAdapter() {
        mTreeAdapter = new CollectAdapter(R.layout.knowledge_item_view, mDatasList);
        mTreeAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mTreeAdapter);
        mTreeAdapter.setEnableLoadMore(false);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                CollectListResponse.Data.Datas data = mDatasList.get(position);

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode != 2000) {
            return;
        }
        String username = PreferenceUtils.getString(getActivity(), SpConstants.KEY_USERNAME, "");
        if (TextUtils.isEmpty(username)) {
            mLinearLayoutLogin.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.INVISIBLE);
        } else {
            mLinearLayoutLogin.setVisibility(View.INVISIBLE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(username)) {
            refresh();
        }
    }
}
