package com.wan.android.loginregister;

import android.text.TextUtils;

import com.wan.android.constant.ErrorCodeConstants;
import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.client.LoginClient;
import com.wan.android.data.source.RetrofitClient;
import com.wan.android.util.DisposableUtil;
import com.wan.android.util.NetworkUtils;

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

    private final LoginContract.View mLoginView;

    public LoginPresenter(LoginContract.View loginView) {
        mLoginView = checkNotNull(loginView, "loginView cannot be null");

        mLoginView.setPresenter(this);
    }

    @Override
    public void login(String username, String password) {
        if (!NetworkUtils.isConnected()) {
            mLoginView.showNetworkError();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            mLoginView.showLoginUsernameEmpty();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mLoginView.showLoginPasswordEmpty();
            return;
        }
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
                                    CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != ErrorCodeConstants.CODE_OK) {
                            mLoginView.showLoginFail(
                                    new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }

                        AccountData data = response.getData();
                        if (data == null) {
                            mLoginView.showLoginFail(
                                    CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }

                        mLoginView.showLoginSuccess(response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginView.dismissProgressBar();
                        mLoginView.showLoginFail(
                                CommonException.convert(ErrorCodeMessageEnum.SERVER_ERROR));
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
