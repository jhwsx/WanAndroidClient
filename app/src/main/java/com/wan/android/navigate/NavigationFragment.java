package com.wan.android.navigate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.adapter.NavigationPagerAdapter;
import com.wan.android.base.BaseFragment;
import com.wan.android.callback.EmptyCallback;
import com.wan.android.callback.LoadingCallback;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.NavigationData;
import com.wan.android.data.bean.NightModeEvent;
import com.wan.android.skin.VerticalTabLayoutCompat;
import com.wan.android.skin.VerticalViewPagerCompat;
import com.wan.android.util.EdgeEffectUtils;
import com.wan.android.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.kaelaela.verticalviewpager.transforms.ZoomOutTransformer;

/**
 * 导航片段
 * @author wzc
 * @date 2018/3/20
 */
public class NavigationFragment extends BaseFragment implements NavigationContract.View{
    private static final String TAG = NavigationFragment.class.getSimpleName();
    private VerticalTabLayoutCompat  mVerticalTabLayout;
    private VerticalViewPagerCompat  mVerticalViewPager;
    private LoadService mLoadService;
    private NavigationContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fixme
        mPresenter =  new NavigationPresenter(this);
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.navigation_fragment, null);
        // 获取LoadService,把RecyclerView添加进去
        mLoadService = LoadSir.getDefault().register(inflate, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                mPresenter.fetchNavigation();
            }
        });
        // 把状态管理页面添加到根布局中
        mSwipeRefreshLayout.addView(mLoadService.getLoadLayout(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mVerticalTabLayout = (VerticalTabLayoutCompat) inflate.findViewById(R.id.verticaltablayout);
        mVerticalViewPager = (VerticalViewPagerCompat ) inflate.findViewById(R.id.verticalviewpager);
        EdgeEffectUtils.setViewPagerEdgeEffect(mVerticalViewPager);
        mVerticalViewPager.setVisibility(View.VISIBLE);

        mPresenter.fetchNavigation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showNavigationSuccess(List<NavigationData> dataList) {
        mLoadService.showSuccess();
        List<NavigationListFragment> navigationListFragments = new ArrayList<>();
        for (NavigationData navigationData : dataList) {
            ArrayList<ArticleDatas> articles = navigationData.getArticles();
            navigationListFragments.add(NavigationListFragment.newInstance(articles));
        }
        mVerticalViewPager.setAdapter(new NavigationPagerAdapter(getChildFragmentManager(), dataList, navigationListFragments));
        mVerticalViewPager.setPageTransformer(true, new ZoomOutTransformer());
        mVerticalTabLayout.setupWithViewPager(mVerticalViewPager);
    }

    @Override
    public void showNavigationFail(CommonException e) {
        mLoadService.showCallback(EmptyCallback.class);
        Toast.makeText(Utils.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(NavigationContract.Presenter presenter) {
//        mPresenter = presenter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(NightModeEvent nightModeEvent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "receive nightModeEvent");
        }
        EdgeEffectUtils.setViewPagerEdgeEffect(mVerticalViewPager);
    }
}
