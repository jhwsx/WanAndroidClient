package com.wan.android.ui.project;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wan.android.R;
import com.wan.android.data.network.model.PageData;
import com.wan.android.data.network.model.ProjectData;
import com.wan.android.di.component.ActivityComponent;
import com.wan.android.ui.base.BaseFragment;
import com.wan.android.ui.base.BaseMainFragment;
import com.wan.android.ui.loadcallback.LoadingCallback;
import com.wan.android.ui.loadcallback.NetworkErrorCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * 项目 Fragment
 *
 * @author wzc
 * @date 2018/8/3
 */
public class ProjectFragment extends BaseMainFragment implements ProjectContract.View {

    private LoadService mLoadService;
    private static final String TAG = ProjectFragment.class.getSimpleName();
    public static ProjectFragment newInstance() {
        Bundle args = new Bundle();
        ProjectFragment fragment = new ProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @Inject
    ProjectContract.Presenter<ProjectContract.View> mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_fragment, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }

        mLoadService = LoadSir.getDefault().register(view, new com.kingja.loadsir.callback.Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                mPresenter.getProject();
            }
        });
        return mLoadService.getLoadLayout();
    }

    @Override
    protected void setUp(View view) {
        mPresenter.getProject();
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

    private List<PageData> mPageData;
    private int mCurrPagePosition;
    @Override
    public void showGetProjectSuccess(List<ProjectData> data) {
        Timber.d("showGetProjectSuccess: size=%s", data.size());
        mLoadService.showSuccess();
        mPageData = new ArrayList<>(data.size());
        for (ProjectData pd : data) {
            mPageData.add(new PageData(pd.getName(), ProjectChildFragment.newInstance(pd.getId())));
        }
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mPageData.get(position).getFragment();
            }

            @Override
            public int getCount() {
                return mPageData.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mPageData.get(position).getTitle();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                mCurrPagePosition = position;
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void showGetProjectFail() {
        Timber.d("showGetProjectFail");
        mLoadService.showCallback(NetworkErrorCallback.class);
    }

    @Override
    public void scrollToTop() {
        BaseFragment fragment = (BaseFragment) mPageData.get(mCurrPagePosition).getFragment();
        fragment.scrollToTop();
    }
}
