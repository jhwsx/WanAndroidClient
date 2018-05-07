package com.wan.android.loginregister;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.client.LoginClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.Utils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Listens to user actions from the UI ({@link LoginFragment}), retrieves the data and updates the
 * UI as required.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    private final LoginContract.View mLoginView;

    public LoginPresenter(LoginContract.View loginView) {
        mLoginView = checkNotNull(loginView, "loginView cannot be null");

        mLoginView.setPresenter(this);
    }

    @Override
    public void login(String username, String password) {
        mLoginView.showProgressBar();
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(LoginClient.class)
                .login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<AccountData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<AccountData> response) {
                        mLoginView.dismissProgressBar();
                        if (response == null) {
                            mLoginView.showLoginFail(
                                    new CommonException(-1, BuildConfig.DEBUG ? Utils.getContext().getString(R.string.response_cannot_be_null)
                                            : Utils.getContext().getString(R.string.login_fail)));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mLoginView.showLoginFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        mLoginView.showLoginSuccess(response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginView.dismissProgressBar();

                        mLoginView.showLoginFail(new CommonException(-1, e != null && BuildConfig.DEBUG ? e.toString()
                                : Utils.getContext().getString(R.string.login_fail)));
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
