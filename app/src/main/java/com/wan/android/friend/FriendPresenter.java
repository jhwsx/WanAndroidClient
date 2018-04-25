package com.wan.android.friend;

import com.wan.android.data.client.FriendClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.FriendData;
import com.wan.android.data.source.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class FriendPresenter implements FriendContract.Presenter {
    private final RetrofitClient mRetrofitClient;
    private  final FriendContract.View mHotView;

    public FriendPresenter(RetrofitClient retrofitClient, FriendContract.View hotView) {
        mRetrofitClient = retrofitClient;
        mHotView = hotView;
        mHotView.setPresenter(this);
    }

    @Override
    public void getFriend() {
        FriendClient client = mRetrofitClient.create(FriendClient.class);
        Call<CommonResponse<List<FriendData>>> call = client.getFriend();
        call.enqueue(new Callback<CommonResponse<List<FriendData>>>() {
            @Override
            public void onResponse(Call<CommonResponse<List<FriendData>>> call, Response<CommonResponse<List<FriendData>>> response) {
                if (response == null) {
                    mHotView.showGetFriengFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<List<FriendData>> body = response.body();
                if (body == null) {
                    mHotView.showGetFriengFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mHotView.showGetFriengFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                final List<FriendData> data = body.getData();
                if (data == null) {
                    mHotView.showGetFriengFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                mHotView.showGetFriendSuccess(data);

            }

            @Override
            public void onFailure(Call<CommonResponse<List<FriendData>>> call, Throwable t) {
                mHotView.showGetFriengFail(new CommonException(-1, t == null ? "get friend fail" : t.toString()));
            }
        });
    }

    @Override
    public void start() {

    }
}
