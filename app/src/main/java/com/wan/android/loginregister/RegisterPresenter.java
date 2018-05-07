package com.wan.android.loginregister;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.client.RegisterClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.Utils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class RegisterPresenter implements RegisterContract.Presenter {
    private static final String TAG = RegisterPresenter.class.getSimpleName();
    private final RegisterContract.View mRegisterView;


    public RegisterPresenter(RegisterContract.View registerView) {
        mRegisterView = checkNotNull(registerView, "registerView cannot be null");

        mRegisterView.setPresenter(this);
    }

    @Override
    public void register(String username, String password, final String repassword) {
        mRegisterView.showProgressBar();
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(RegisterClient.class)
                .register(username, password, repassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<AccountData>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<AccountData> response) {
                        mRegisterView.dismissProgressBar();

                        if (response == null) {
                            mRegisterView.showRegisterFail(
                                    new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                            : Utils.getContext().getString(R.string.register_fail)));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mRegisterView.showRegisterFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        mRegisterView.showRegisterSuccess(response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRegisterView.dismissProgressBar();
                        mRegisterView.showRegisterFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.register_fail)));
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
