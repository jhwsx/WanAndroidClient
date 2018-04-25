package com.wan.android.loginregister;

import android.util.Log;

import com.wan.android.BuildConfig;
import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.client.LoginClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.source.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Listens to user actions from the UI ({@link LoginFragment}), retrieves the data and updates the
 * UI as required.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();
    private final RetrofitClient mRetrofitClient;

    private final LoginContract.View mLoginView;

    public LoginPresenter(RetrofitClient retrofitClient, LoginContract.View loginView) {
        mRetrofitClient = checkNotNull(retrofitClient, "retrofitClient cannot be null");
        mLoginView = checkNotNull(loginView, "loginView cannot be null");

        mLoginView.setPresenter(this);
    }

    @Override
    public void login(String username, String password) {
        mLoginView.showProgressBar();

        LoginClient client = mRetrofitClient.create(LoginClient.class);
        Call<CommonResponse<AccountData>> call = client.login(username, password);
        call.enqueue(new Callback<CommonResponse<AccountData>>() {
            @Override
            public void onResponse(Call<CommonResponse<AccountData>> call, Response<CommonResponse<AccountData>> response) {
                mLoginView.dismissProgressBar();
                if (response == null) {
                    mLoginView.showLoginFail(new CommonException(-1, "response cannot be null"));
                    return;
                }
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "response:" + response);
                }
                CommonResponse<AccountData> body = response.body();
                if (body == null) {
                    mLoginView.showLoginFail(new CommonException(-1, "body cannot be null"));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, body.getErrormsg());
                    }
                    mLoginView.showLoginFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mLoginView.showLoginSuccess(body.getData());
            }

            @Override
            public void onFailure(Call<CommonResponse<AccountData>> call, Throwable t) {
                Log.d(TAG, "t:" + t);
                mLoginView.dismissProgressBar();

                mLoginView.showLoginFail(new CommonException(-1, t == null ? "login fail" : t.toString()));
            }
        });
    }

    @Override
    public void start() {

    }
}
