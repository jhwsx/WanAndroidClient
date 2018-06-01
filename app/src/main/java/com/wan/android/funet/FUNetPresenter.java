package com.wan.android.funet;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.bean.FUNetData;
import com.wan.android.data.client.FUNetClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.Utils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link FUNetFragment}), retrieves the data and updates the
 * UI as required.
 *
 * @author wzc
 * @date 2018/5/31
 */
public class FUNetPresenter implements FUNetConstract.Presenter {
    private FUNetConstract.View mFunetView;

    public FUNetPresenter(FUNetConstract.View funetView) {
        mFunetView = funetView;
        mFunetView.setPresenter(this);
    }

    @Override
    public void getFUNets() {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(FUNetClient.class)
                .getFUNets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<List<FUNetData>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<List<FUNetData>> response) {
                        if (response == null) {
                            mFunetView.showGetFUNetsFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mFunetView.showGetFUNetsFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        final List<FUNetData> data = response.getData();
                        if (data == null || data.size() == 0) {
                            mFunetView.showGetFUNetsFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mFunetView.showGetFUNetsSuccess(data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        DisposableUtil.dispose(disposable[0]);
                        mFunetView.showGetFUNetsFail(
                                new CommonException(-1, t != null && BuildConfig.DEBUG ? t.toString()
                                        : Utils.getContext().getString(R.string.get_friends_fail)));

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
