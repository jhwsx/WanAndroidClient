package com.wan.android.content;

import com.wan.android.data.client.CollectClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.source.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class ContentPresenter implements ContentContract.Presenter {
    private final RetrofitClient mRetrofitClient;
    private final ContentContract.View mContentView;

    public ContentPresenter(RetrofitClient retrofitClient, ContentContract.View contentView) {
        mRetrofitClient = retrofitClient;
        mContentView = contentView;
        mContentView.setPresenter(this);
    }

    @Override
    public void collect(int id) {
        CollectClient collectClient = mRetrofitClient.create(CollectClient.class);
        Call<CommonResponse<String>> call = collectClient.collect(id);
        call.enqueue(new Callback<CommonResponse<String>>() {
            @Override
            public void onResponse(Call<CommonResponse<String>> call, Response<CommonResponse<String>> response) {
                if (response == null) {
                    mContentView.showCollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mContentView.showCollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mContentView.showCollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mContentView.showCollectSuccess();

            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mContentView.showCollectFail(new CommonException(-1, t == null ? "collect fail" : t.toString()));
            }
        });
    }

    @Override
    public void start() {

    }
}
