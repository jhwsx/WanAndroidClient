package com.wan.android.ui.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.data.network.model.NavigationData;
import com.wan.android.data.network.model.NavigationRightData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.ui.loadcallback.LoadingCallback;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * 导航 Fragment
 *
 * @author wzc
 * @date 2018/8/3
 */
public class NavigationFragment extends BaseFragment
        implements NavigationContract.View {

    private NavigationLeftFragment mNavigationLeftFragment;
    private NavigationRightFragment mNavigationRightFragment;
    private List<NavigationRightData> mRightData;
    private LoadService mLoadService;

    public static NavigationFragment newInstance() {
        Bundle args = new Bundle();
        NavigationFragment fragment = new NavigationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    NavigationPresenter<NavigationContract.View> mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_fragment, container, false);
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
                mPresenter.getNavigation();
            }
        });
        return mLoadService.getLoadLayout();
    }

    @Override
    protected void setUp(View view) {
        mNavigationLeftFragment = (NavigationLeftFragment) getChildFragmentManager()
                .findFragmentById(R.id.fl_navigation_left_container);
        if (mNavigationLeftFragment == null) {
            mNavigationLeftFragment = NavigationLeftFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fl_navigation_left_container, mNavigationLeftFragment)
                    .commit();
        }

        mNavigationRightFragment = (NavigationRightFragment) getChildFragmentManager()
                .findFragmentById(R.id.fl_navigation_right_container);
        if (mNavigationRightFragment == null) {
            mNavigationRightFragment = NavigationRightFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fl_navigation_right_container, mNavigationRightFragment)
                    .commit();
        }
        mPresenter.getNavigation();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    private List<NavigationData> mNavigationData;

    @Override
    public void showGetNavigationSuccess(List<NavigationData> data) {
        Timber.d("showGetNavigationSuccess: size=%s", data.size());
        mLoadService.showSuccess();
        mNavigationData = data;
        List<String> titles = new ArrayList<>();
        mRightData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            NavigationData navigationData = data.get(i);
            titles.add(navigationData.getName());
            NavigationRightData navigationRightData
                    = new NavigationRightData(true, navigationData.getName(),
                    null, i, NavigationRightData.TYPE_TITLE);
            mRightData.add(navigationRightData);
            for (ArticleDatas articleDatas : navigationData.getArticles()) {
                mRightData.add(new NavigationRightData(false, navigationData.getName(),
                        articleDatas, i, NavigationRightData.TYPE_CONTENT));
            }
        }
        mNavigationLeftFragment.setData(titles);
        mNavigationRightFragment.setData(mRightData);
    }


    @Override
    public void showGetNavigationFail() {
        mLoadService.showCallback(NetworkErrorCallback.class);
    }

    /**
     * 左侧联动右侧
     * @param position
     */
    public void onNavigationLeftItemClicked(int position) {
        int scrollToPosition = 0;
        for (int i = 0; i < position; i++) {
            scrollToPosition += mNavigationData.get(i).getArticles().size();
        }
        scrollToPosition += position;
        mNavigationRightFragment.smoothScrollToPosition(scrollToPosition);
    }

    /**
     * 右侧联动左侧
     * @param groupId
     */
    public void onNavigationRightHeaderGroupIdChanged(int groupId) {
        mNavigationLeftFragment.setCheckedPosition(groupId);
    }
}
