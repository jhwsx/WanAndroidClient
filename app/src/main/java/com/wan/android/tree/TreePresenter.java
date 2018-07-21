package com.wan.android.tree;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.client.TreeListClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class TreePresenter implements TreeContract.Presenter {
    private final TreeContract.View mTreeView;

    public TreePresenter(TreeContract.View treeView) {
        mTreeView = treeView;
        mTreeView.setPresenter(this);
    }

    @Override
    public void swipeRefresh() {

        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(TreeListClient.class)
                .getTree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<ArrayList<BranchData>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<ArrayList<BranchData>> response) {
                        if (response == null) {
                            mTreeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        List<BranchData> data = response.getData();
                        if (data == null || data.size() <= 0) {
                            mTreeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }

                        mTreeView.showSwipeRefreshSuccess(data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        DisposableUtil.dispose(disposable[0]);
                        mTreeView.showSwipeRefreshFail(
                                new CommonException(-1, t != null && BuildConfig.DEBUG ? t.toString()
                                        : Utils.getApp().getString(R.string.swipe_refresh_fail)));
                    }

                    @Override
                    public void onComplete() {
                        DisposableUtil.dispose(disposable[0]);
                    }
                });
    }

    @Override
    public void start() {

    }
}
