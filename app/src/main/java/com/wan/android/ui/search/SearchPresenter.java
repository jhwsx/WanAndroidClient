package com.wan.android.ui.search;

import android.content.Context;

import com.wan.android.data.DataManager;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.data.network.model.HotkeyData;
import com.wan.android.data.network.model.SearchHistoryData;
import com.wan.android.di.ApplicationContext;
import com.wan.android.ui.base.BaseObserver;
import com.wan.android.ui.base.BasePresenter;
import com.wan.android.util.rx.RxUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @author wzc
 * @date 2018/8/21
 */
public class SearchPresenter<V extends SearchContract.View> extends BasePresenter<V>
        implements SearchContract.Presenter<V> {
    @Inject
    public SearchPresenter(@ApplicationContext Context context, DataManager dataManager,
                           CompositeDisposable compositeDisposable) {
        super(context, dataManager, compositeDisposable);
    }

    @Override
    public void getHotkey() {
        getCompositeDisposable().add(getDataManager().getHotkey()
                .compose(RxUtils.<CommonResponse<List<HotkeyData>>>rxSchedulerHelper())
                .compose(RxUtils.<List<HotkeyData>>handleResult(getApplicationContext(), getMvpView()))
                .subscribeWith(new BaseObserver<List<HotkeyData>>(getMvpView()) {
                    @Override
                    public void onNext(List<HotkeyData> data) {
                        super.onNext(data);
                        getMvpView().showHotkeySuccess(data);
                    }
                }));
    }

    @Override
    public void saveSearchHistory2Db(SearchHistoryData data) {
        getCompositeDisposable().add(getDataManager().saveSearchHistory2Db(data)
                .compose(RxUtils.<Boolean>rxSchedulerHelper())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        getMvpView().showSaveSearchHistory2DbSuccess();
                    }
                }));
    }

    @Override
    public void getDbSearchHistory() {
        getCompositeDisposable().add(getDataManager().getDbSearchHistory()
                .compose(RxUtils.<List<SearchHistoryData>>rxSchedulerHelper())
                .subscribe(new Consumer<List<SearchHistoryData>>() {
                    @Override
                    public void accept(List<SearchHistoryData> data) throws Exception {
                        getMvpView().showGetDbSearchHistorySuccess(data);
                    }
                }));
    }

    @Override
    public void deleteDbSearchHistory() {
        getCompositeDisposable().add(getDataManager().deleteDbSearchHistory()
                .compose(RxUtils.<Boolean>rxSchedulerHelper())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        getMvpView().showDeleteDbSearchHistorySuccess();
                    }
                }));
    }
}
