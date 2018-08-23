package com.wan.android.di.component;

import com.wan.android.di.PerActivity;
import com.wan.android.di.module.ActivityModule;
import com.wan.android.ui.content.ContentActivity;
import com.wan.android.ui.content.ContentFragment;
import com.wan.android.ui.home.HomeFragment;
import com.wan.android.ui.login.LoginActivity;
import com.wan.android.ui.login.LoginFragment;
import com.wan.android.ui.login.RegisterActivity;
import com.wan.android.ui.login.RegisterFragment;
import com.wan.android.ui.main.MainActivity;
import com.wan.android.ui.search.SearchActivity;
import com.wan.android.ui.search.SearchFragment;
import com.wan.android.ui.search.SearchResultFragment;
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
    void inject(MainActivity activity);

    void inject(LoginActivity activity);

    void inject(LoginFragment fragment);

    void inject(RegisterActivity activity);

    void inject(RegisterFragment fragment);

    void inject(HomeFragment fragment);

    void inject(TreeFragment fragment);

    void inject(ContentActivity activity);

    void inject(ContentFragment fragment);

    void inject(SearchActivity activity);

    void inject(SearchFragment fragment);

    void inject(SearchResultFragment fragment);

    void inject(BranchActivity activity);

    void inject(BranchFragment fragment);
}
