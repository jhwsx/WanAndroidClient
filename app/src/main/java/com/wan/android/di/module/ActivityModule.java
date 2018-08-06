package com.wan.android.di.module;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.wan.android.R;
import com.wan.android.di.ActivityContext;
import com.wan.android.di.PerActivity;
import com.wan.android.ui.adapter.CommonListAdapter;
import com.wan.android.ui.login.LoginContract;
import com.wan.android.ui.login.LoginPresenter;
import com.wan.android.ui.login.RegisterContract;
import com.wan.android.ui.login.RegisterPresenter;
import com.wan.android.ui.main.MainContract;
import com.wan.android.ui.main.MainPresenter;

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
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
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
}
