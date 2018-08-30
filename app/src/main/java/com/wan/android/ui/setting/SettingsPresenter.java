package com.wan.android.ui.setting;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author wzc
 * @date 2018/8/30
 */
public class SettingsPresenter<V extends SettingsContract.View> extends BasePresenter<V>
        implements SettingsContract.Presenter<V> {

    @Inject
    public SettingsPresenter(@ApplicationContext Context context, DataManager dataManager,
                             CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public boolean getLoginStatus() {
        return getDataManager().getLoginStatus();
    }
}
