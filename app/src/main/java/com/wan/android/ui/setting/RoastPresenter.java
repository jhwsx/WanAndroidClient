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
public class RoastPresenter<V extends RoastContract.View> extends BasePresenter<V>
        implements RoastContract.Presenter<V> {
    @Inject
    public RoastPresenter(@ApplicationContext Context context, DataManager dataManager,
                          CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public String getRoastOpenid() {
        return getDataManager().getRoastOpenid();
    }

    @Override
    public void setRoastOpenid(String openid) {
        getDataManager().setRoastOpenid(openid);
    }

    @Override
    public int getRoastHeadPicId() {
        return getDataManager().getRoastHeadPicId();
    }

    @Override
    public void setRoastHeadPicId(int headPicId) {
        getDataManager().setRoastHeadPicId(headPicId);
    }

    @Override
    public String getUsername() {
        return getDataManager().getUsername();
    }
}
