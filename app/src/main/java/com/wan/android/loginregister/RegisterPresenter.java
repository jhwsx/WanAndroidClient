package com.wan.android.loginregister;

import android.util.Log;

import com.wan.android.data.bean.AccountData;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.client.RegisterClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.source.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class RegisterPresenter implements RegisterContract.Presenter {
    private static final String TAG = RegisterPresenter.class.getSimpleName();
    private final RetrofitClient mRetrofitClient;
    private final RegisterContract.View mRegisterView;


    public RegisterPresenter(RetrofitClient retrofitClient, RegisterContract.View registerView) {
        mRetrofitClient = checkNotNull(retrofitClient, "retrofitClient cannot be null");
        mRegisterView = checkNotNull(registerView, "registerView cannot be null");

        mRegisterView.setPresenter(this);
    }

    @Override
    public void register(String username, String password, final String repassword) {
        mRegisterView.showProgressBar();
        RegisterClient client = mRetrofitClient.create(RegisterClient.class);
        Call<CommonResponse<AccountData>> call = client.register(username, password, repassword);
        call.enqueue(new Callback<CommonResponse<AccountData>>() {
            @Override
            public void onResponse(Call<CommonResponse<AccountData>> call, Response<CommonResponse<AccountData>> response) {
                mRegisterView.dismissProgressBar();
                Log.d(TAG, "response:" + response);
                if (response == null) {
                    mRegisterView.showRegisterFail(new CommonException(-1, "response cannot be null"));
                    return;
                }
                CommonResponse<AccountData> body = response.body();
                if (body == null) {
                    mRegisterView.showRegisterFail(new CommonException(-1, "body cannot be null"));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    Log.d(TAG, body.getErrormsg());
                    mRegisterView.showRegisterFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }

                mRegisterView.showRegisterSuccess(body.getData());
            }

            @Override
            public void onFailure(Call<CommonResponse<AccountData>> call, Throwable t) {
                Log.d(TAG, "t:" + t);
                mRegisterView.dismissProgressBar();
                mRegisterView.showRegisterFail(new CommonException(-1, t == null ? "register fail" : t.toString()));
            }
        });
    }

    @Override
    public void start() {

    }
}
