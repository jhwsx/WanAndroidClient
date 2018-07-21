package com.wan.android.friend;

import com.wan.android.BuildConfig;
import com.wan.android.R;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.bean.FriendData;
import com.wan.android.data.client.FriendClient;
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
public class FriendPresenter implements FriendContract.Presenter {
    private  final FriendContract.View mHotView;

    public FriendPresenter(FriendContract.View hotView) {
        mHotView = hotView;
        mHotView.setPresenter(this);
    }

    @Override
    public void getFriend() {
        final Disposable[] disposable = new Disposable[1];
        RetrofitClient.getInstance()
                .create(FriendClient.class)
                .getFriend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse<List<FriendData>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(CommonResponse<List<FriendData>> response) {
                        if (response == null) {
                            mHotView.showGetFriengFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                            return;
                        }

                        if (response.getErrorcode() != 0) {
                            mHotView.showGetFriengFail(new CommonException(response.getErrorcode(), response.getErrormsg()));
                            return;
                        }
                        final List<FriendData> data = response.getData();
                        if (data == null || data.size() == 0) {
                            mHotView.showGetFriengFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                            return;
                        }
                        mHotView.showGetFriendSuccess(data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        DisposableUtil.dispose(disposable[0]);
                        mHotView.showGetFriengFail(
                                new CommonException(-1, t != null && BuildConfig.DEBUG ? t.toString()
                                        : Utils.getApp().getString(R.string.get_friends_fail) ));

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
