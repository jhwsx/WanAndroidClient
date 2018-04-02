package com.wan.android.navigate;

import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.NavigationData;
import com.wan.android.data.client.NavigateClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.source.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class NavigationPresenter implements NavigationContract.Presenter {
    private RetrofitClient mRetrofitClient;
    private NavigationContract.View mNavigationView;

    public NavigationPresenter(RetrofitClient retrofitClient, NavigationContract.View navigationView) {
        mRetrofitClient = retrofitClient;
        mNavigationView = navigationView;
        mNavigationView.setPresenter(this);
    }

    @Override
    public void fetchNavigation() {
        NavigateClient client = mRetrofitClient.create(NavigateClient.class);
        Call<CommonResponse<List<NavigationData>>> call = client.getNavigation();
        call.enqueue(new Callback<CommonResponse<List<NavigationData>>>() {
            @Override
            public void onResponse(Call<CommonResponse<List<NavigationData>>> call, Response<CommonResponse<List<NavigationData>>> response) {
                if (response == null) {
                    mNavigationView.showNavigationFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<List<NavigationData>> body = response.body();
                if (body == null) {
                    mNavigationView.showNavigationFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mNavigationView.showNavigationFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }

                List<NavigationData> data = body.getData();
                if (data == null) {
                    mNavigationView.showNavigationFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                mNavigationView.showNavigationSuccess(data);

            }

            @Override
            public void onFailure(Call<CommonResponse<List<NavigationData>>> call, Throwable t) {
                mNavigationView.showNavigationFail(new CommonException(-1, t == null ? "fetch navigation fail" : t.toString()));
            }
        });
    }

    @Override
    public void start() {

    }
}
