package com.wan.android.di.module;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.wan.android.R;
import com.wan.android.di.ActivityContext;
import com.wan.android.di.PerActivity;
import com.wan.android.ui.adapter.CollectAdapter;
import com.wan.android.ui.adapter.CommonListAdapter;
import com.wan.android.ui.adapter.ProjectAdapter;
import com.wan.android.ui.adapter.TreeAdapter;
import com.wan.android.ui.collect.AddCollectContract;
import com.wan.android.ui.collect.AddCollectPresenter;
import com.wan.android.ui.collect.MyCollectionContract;
import com.wan.android.ui.collect.MyCollectionPresenter;
import com.wan.android.ui.content.ContentContract;
import com.wan.android.ui.content.ContentPresenter;
import com.wan.android.ui.home.HomeContract;
import com.wan.android.ui.home.HomePresenter;
import com.wan.android.ui.login.LoginContract;
import com.wan.android.ui.login.LoginPresenter;
import com.wan.android.ui.login.RegisterContract;
import com.wan.android.ui.login.RegisterPresenter;
import com.wan.android.ui.main.MainContract;
import com.wan.android.ui.main.MainPresenter;
import com.wan.android.ui.navigation.NavigationContract;
import com.wan.android.ui.navigation.NavigationPresenter;
import com.wan.android.ui.project.ProjectChildContract;
import com.wan.android.ui.project.ProjectChildPresenter;
import com.wan.android.ui.project.ProjectContract;
import com.wan.android.ui.project.ProjectPresenter;
import com.wan.android.ui.search.SearchContract;
import com.wan.android.ui.search.SearchPresenter;
import com.wan.android.ui.search.SearchResultContract;
import com.wan.android.ui.search.SearchResultPresenter;
import com.wan.android.ui.setting.RoastContract;
import com.wan.android.ui.setting.RoastPresenter;
import com.wan.android.ui.setting.SettingsContract;
import com.wan.android.ui.setting.SettingsPresenter;
import com.wan.android.ui.tree.BranchContract;
import com.wan.android.ui.tree.BranchPresenter;
import com.wan.android.ui.tree.TreeContract;
import com.wan.android.ui.tree.TreePresenter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author wzc
 * @date 2018/8/2
 */
@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    CommonListAdapter provideCommonListAdapter() {
        return new CommonListAdapter(R.layout.recycle_item);
    }

    @Provides
    CollectAdapter provideCollectAdapter() {
        return new CollectAdapter();
    }

    @Provides
    TreeAdapter provideTreeAdapter() {
        return new TreeAdapter();
    }

    @Provides
    ProjectAdapter provideProjectAdapter() {
        return new ProjectAdapter();
    }

    @Provides
    LayoutInflater provideLayoutInflater(Activity activity) {
        return LayoutInflater.from(activity);
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(Activity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    HorizontalDividerItemDecoration provideHorizontalDividerItemDecoration(Activity activity) {
        return new HorizontalDividerItemDecoration.Builder(activity)
                .colorResId(R.color.color_ef).sizeResId(R.dimen.px_1).build();
    }

    @Provides
    @PerActivity
    MainContract.Presenter<MainContract.View> provideMainPresenter(
            MainPresenter<MainContract.View> presenter) {
        return presenter;
    }

    // TODO: 2018/8/3 这里加不加 @PerActivity 呢?
    @Provides
    LoginContract.Presenter<LoginContract.View> provideLoginPresenter(
            LoginPresenter<LoginContract.View> presenter) {
        return presenter;
    }

    @Provides
    RegisterContract.Presenter<RegisterContract.View> provideRegisterPresenter(
            RegisterPresenter<RegisterContract.View> presenter) {
        return presenter;
    }

    @Provides
    HomeContract.Presenter<HomeContract.View> provideHomePresenter(
            HomePresenter<HomeContract.View> presenter) {
        return presenter;
    }

    @Provides
    SearchContract.Presenter<SearchContract.View> provideSearchPresenter(
            SearchPresenter<SearchContract.View> presenter) {
        return presenter;
    }

    @Provides
    SearchResultContract.Presenter<SearchResultContract.View> provideSearchResultPresenter(
            SearchResultPresenter<SearchResultContract.View> presenter) {
        return presenter;
    }

    @Provides
    TreeContract.Presenter<TreeContract.View> provideTreePresenter(
            TreePresenter<TreeContract.View> presenter) {
        return presenter;
    }

    @Provides
    BranchContract.Presenter<BranchContract.View> provideBranchPresenter(
            BranchPresenter<BranchContract.View> presenter) {
        return presenter;
    }

    @Provides
    NavigationContract.Presenter<NavigationContract.View> provideNavigationPresenter(
            NavigationPresenter<NavigationContract.View> presenter) {
        return presenter;
    }

    @Provides
    ProjectContract.Presenter<ProjectContract.View> provideProjectPresenter(
            ProjectPresenter<ProjectContract.View> presenter) {
        return presenter;
    }

    @Provides
    ProjectChildContract.Presenter<ProjectChildContract.View> provideProjectChildPresenter(
            ProjectChildPresenter<ProjectChildContract.View> presenter) {
        return presenter;
    }

    @Provides
    ContentContract.Presenter<ContentContract.View> provideContentPresenter(
            ContentPresenter<ContentContract.View> presenter) {
        return presenter;
    }

    @Provides
    MyCollectionContract.Presenter<MyCollectionContract.View> provideMyCollectionPresenter(
            MyCollectionPresenter<MyCollectionContract.View> presenter) {
        return presenter;
    }

    @Provides
    AddCollectContract.Presenter<AddCollectContract.View> provideAddCollectPresenter(
            AddCollectPresenter<AddCollectContract.View> presenter) {
        return presenter;
    }

    @Provides
    SettingsContract.Presenter<SettingsContract.View> provideSettingsPresenter(
            SettingsPresenter<SettingsContract.View> presenter) {
        return presenter;
    }

    @Provides
    RoastContract.Presenter<RoastContract.View> provideRoastPresenter(
            RoastPresenter<RoastContract.View> presenter) {
        return presenter;
    }
}
