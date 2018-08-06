package com.wan.android.di.component;

import com.wan.android.di.PerActivity;
import com.wan.android.di.module.ActivityModule;
import com.wan.android.ui.login.LoginActivity;
import com.wan.android.ui.login.LoginFragment;
import com.wan.android.ui.login.RegisterActivity;
import com.wan.android.ui.login.RegisterFragment;
import com.wan.android.ui.main.MainActivity;

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
}
