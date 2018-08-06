package com.wan.android.util.rx;

import android.content.Context;

import com.wan.android.R;
import com.wan.android.data.network.model.ApiException;
import com.wan.android.data.network.model.CommonResponse;
import com.wan.android.ui.base.MvpView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wzc
 * @date 2018/8/3
 */
public class RxUtils {
    private RxUtils() {
        //no instance
    }

    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<CommonResponse<T>,T> handleResult(final Context context,
                                                                              final MvpView view) {
        return new ObservableTransformer<CommonResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<CommonResponse<T>> response) {
                return response.flatMap(new Function<CommonResponse<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(CommonResponse<T> res) throws Exception {

                        if (view != null && !view.isNetworkConnected()) {
                            return Observable.error(new ApiException(context.getString(R.string.error_msg_network_error)));
                        }
                        if (res == null) {
                            return Observable.error(new ApiException(context.getString(R.string.error_msg_response_null)));
                        }
                        if (res.getErrorcode() != 0) {
                            return Observable.error(new ApiException(res.getErrormsg()));
                        }
                        if (res.getData() == null) {
                            return Observable.error(new ApiException(context.getString(R.string.error_msg_data_null)));
                        }

                        return createData(res.getData());
                    }
                });
            }
        };
    }

    private static <T> Observable<T> createData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }


}
