package com.wan.android.di.module;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.wan.android.R;
import com.wan.android.di.ActivityContext;
import com.wan.android.di.PerActivity;
import com.wan.android.ui.adapter.CommonListAdapter;
import com.wan.android.ui.adapter.TreeAdapter;
import com.wan.android.ui.content.X5WebView;
import com.wan.android.ui.home.HomeContract;
import com.wan.android.ui.home.HomePresenter;
import com.wan.android.ui.login.LoginContract;
import com.wan.android.ui.login.LoginPresenter;
import com.wan.android.ui.login.RegisterContract;
import com.wan.android.ui.login.RegisterPresenter;
import com.wan.android.ui.main.MainContract;
import com.wan.android.ui.main.MainPresenter;
import com.wan.android.ui.search.SearchContract;
import com.wan.android.ui.search.SearchPresenter;
import com.wan.android.ui.search.SearchResultContract;
import com.wan.android.ui.search.SearchResultPresenter;
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
    TreeAdapter provideTreeAdapter() {
        return new TreeAdapter();
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
    X5WebView provideX5WebView(@ActivityContext Context context) {
        return new X5WebView(context);
    }
}
