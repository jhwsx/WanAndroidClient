package com.wan.android.di.component;

import com.wan.android.di.PerActivity;
import com.wan.android.di.module.ActivityModule;
import com.wan.android.ui.collect.AddCollectArticleDialog;
import com.wan.android.ui.collect.MyCollectionFragment;
import com.wan.android.ui.collect.MyCollectionActivity;
import com.wan.android.ui.content.ContentActivity;
import com.wan.android.ui.content.ContentFragment;
import com.wan.android.ui.home.HomeFragment;
import com.wan.android.ui.login.LoginActivity;
import com.wan.android.ui.login.LoginFragment;
import com.wan.android.ui.login.RegisterActivity;
import com.wan.android.ui.login.RegisterFragment;
import com.wan.android.ui.main.MainActivity;
import com.wan.android.ui.navigation.NavigationFragment;
import com.wan.android.ui.project.ProjectChildFragment;
import com.wan.android.ui.project.ProjectFragment;
import com.wan.android.ui.search.SearchActivity;
import com.wan.android.ui.search.SearchFragment;
import com.wan.android.ui.search.SearchResultFragment;
import com.wan.android.ui.setting.RoastActivity;
import com.wan.android.ui.setting.RoastFragment;
import com.wan.android.ui.setting.SettingsActivity;
import com.wan.android.ui.setting.SettingsFragment;
import com.wan.android.ui.splash.SplashActivity;
import com.wan.android.ui.tree.BranchActivity;
import com.wan.android.ui.tree.BranchFragment;
import com.wan.android.ui.tree.TreeFragment;

import dagger.Component;

/**
 * @author wzc
 * @date 2018/8/2
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(LoginActivity activity);

    void inject(LoginFragment fragment);

    void inject(RegisterActivity activity);

    void inject(RegisterFragment fragment);

    void inject(HomeFragment fragment);

    void inject(TreeFragment fragment);

    void inject(NavigationFragment fragment);

    void inject(ProjectFragment fragment);

    void inject(ProjectChildFragment fragment);

    void inject(ContentActivity activity);

    void inject(ContentFragment fragment);

    void inject(SearchActivity activity);

    void inject(SearchFragment fragment);

    void inject(SearchResultFragment fragment);

    void inject(BranchActivity activity);

    void inject(BranchFragment fragment);

    void inject(MyCollectionActivity activity);

    void inject(MyCollectionFragment fragment);

    void inject(AddCollectArticleDialog fragment);

    void inject(SettingsActivity activity);

    void inject(SettingsFragment fragment);

    void inject(RoastActivity activity);

    void inject(RoastFragment fragment);
}
