package com.wan.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.adapter.NavigationPagerAdapter;
import com.wan.android.bean.ArticleDatas;
import com.wan.android.bean.CommonResponse;
import com.wan.android.bean.NavigationData;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.client.NavigateClient;
import com.wan.android.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import me.kaelaela.verticalviewpager.VerticalViewPager;
import me.kaelaela.verticalviewpager.transforms.ZoomOutTransformer;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 导航片段
 * @author wzc
 * @date 2018/3/20
 */
public class NavigationFragment extends BaseFragment {
    private static final String TAG = NavigationFragment.class.getSimpleName();
    private VerticalTabLayout mVerticalTabLayout;
    private VerticalViewPager mVerticalViewPager;
    private LoadService mLoadService;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.fragment_navigation, null);
        // 获取LoadService,把RecyclerView添加进去
        mLoadService = LoadSir.getDefault().register(inflate, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                getNavigation();
            }
        });
        // 把状态管理页面添加到根布局中
        mRootView.addView(mLoadService.getLoadLayout(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mVerticalTabLayout = (VerticalTabLayout) inflate.findViewById(R.id.verticaltablayout);
        mVerticalViewPager = (VerticalViewPager) inflate.findViewById(R.id.verticalviewpager);
        mVerticalViewPager.setVisibility(View.VISIBLE);
        getNavigation();
    }

    private void getNavigation() {
        NavigateClient client = RetrofitClient.create(NavigateClient.class);
        Call<CommonResponse<List<NavigationData>>> call = client.getNavigation();
        call.enqueue(new Callback<CommonResponse<List<NavigationData>>>() {
            @Override
            public void onResponse(Call<CommonResponse<List<NavigationData>>> call, Response<CommonResponse<List<NavigationData>>> response) {
                mLoadService.showSuccess();
                CommonResponse<List<NavigationData>> body = response.body();
                if (body.getErrorcode() != 0) {
                    Toast.makeText(mActivity, body.getErrormsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<NavigationData> data = body.getData();
                List<NavigationListFragment> navigationListFragments = new ArrayList<>();
                for (NavigationData navigationData : data) {
                    ArrayList<ArticleDatas> articles = navigationData.getArticles();
                    navigationListFragments.add(NavigationListFragment.newInstance(articles));
                }
                mVerticalViewPager.setAdapter(new NavigationPagerAdapter(getChildFragmentManager(), data, navigationListFragments));
                mVerticalViewPager.setPageTransformer(true, new ZoomOutTransformer());
                mVerticalTabLayout.setupWithViewPager(mVerticalViewPager);

            }

            @Override
            public void onFailure(Call<CommonResponse<List<NavigationData>>> call, Throwable t) {
                mLoadService.showCallback(EmptyCallback.class);
            }
        });
    }
}
