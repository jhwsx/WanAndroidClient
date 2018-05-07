package com.wan.android.navigate;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.bean.NavigationData;
import com.wan.android.data.client.NavigateClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.Utils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class NavigationPresenter implements NavigationContract.Presenter {
    private NavigationContract.View mNavigationView;

    public NavigationPresenter(NavigationContract.View navigationView) {
        mNavigationView = navigationView;
        mNavigationView.setPresenter(this);
    }

    @Override
    public void fetchNavigation() {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(NavigateClient.class)
                .getNavigation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<List<NavigationData>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<List<NavigationData>> response) {
                        if (response == null) {
                            mNavigationView.showNavigationFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mNavigationView.showNavigationFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        List<NavigationData> data = response.getData();
                        if (data == null) {
                            mNavigationView.showNavigationFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mNavigationView.showNavigationSuccess(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mNavigationView.showNavigationFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.fetch_navigation_fail)));
                        DisposableUtil.dispose(disposable[0]);
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
