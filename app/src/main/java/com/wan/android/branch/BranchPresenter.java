package com.wan.android.branch;

import android.util.Log;

import com.wan.android.BuildConfig;
import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.client.BranchListClient;
import com.wan.android.data.client.CollectClient;
import com.wan.android.data.client.UncollectClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.source.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class BranchPresenter implements BranchContract.Presenter {
    private static final String TAG = BranchPresenter.class.getSimpleName();
    private final RetrofitClient mRetrofitClient;
    private final BranchContract.View mBranchView;

    public BranchPresenter(RetrofitClient retrofitClient, BranchContract.View homeView) {
        mRetrofitClient = retrofitClient;
        mBranchView = homeView;
        mBranchView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void swipeRefresh(int cid) {
        BranchListClient client = mRetrofitClient.create(BranchListClient.class);
        Call<CommonResponse<ArticleData>> call = client.getBranchList(0, cid);
        call.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "response: " + response);
                }
                if (response == null) {
                    mBranchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<ArticleData> body = response.body();
                if (body == null) {
                    mBranchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if ( body.getData() == null ) {
                    mBranchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                ArticleData data = body.getData();
                List<ArticleDatas> datas = data.getDatas();
                mBranchView.showSwipeRefreshSuccess(datas);
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                mBranchView.showSwipeRefreshFail(new CommonException(-1, t == null ? "swipe refresh fail" : t.toString()));

            }
        });
    }

    @Override
    public void loadMore(final int currPage, int cid) {
        BranchListClient client = mRetrofitClient.create(BranchListClient.class);
        Call<CommonResponse<ArticleData>> call = client.getBranchList(currPage, cid);
        call.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "response:" + response);
                }
                if (response == null) {
                    mBranchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<ArticleData> body = response.body();
                if (body == null) {
                    mBranchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getData() == null ) {
                    mBranchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                int nextPage = currPage + 1;
                if (nextPage < response.body().getData().getPagecount()) {
                    mBranchView.showLoadMoreComplete();
                } else {
                    mBranchView.showLoadMoreEnd();
                }

                ArticleData data = body.getData();
                List<ArticleDatas> datas = data.getDatas();
                mBranchView.showLoadMoreSuccess(datas);
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                mBranchView.showLoadMoreFail(new CommonException(-1, t == null ? "load more fail" : t.toString()));
            }
        });
    }

    @Override
    public void collect(int id) {
        CollectClient collectClient = mRetrofitClient.create(CollectClient.class);
        Call<CommonResponse<String>> call = collectClient.collect(id);
        call.enqueue(new Callback<CommonResponse<String>>() {
            @Override
            public void onResponse(Call<CommonResponse<String>> call, Response<CommonResponse<String>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "collect() onResponse response:" + response);
                }
                if (response == null) {
                    mBranchView.showCollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mBranchView.showCollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mBranchView.showCollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mBranchView.showCollectSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mBranchView.showCollectFail(new CommonException(-1, t == null ? "collect fail" : t.toString()));
            }
        });
    }

    @Override
    public void uncollect(int id) {
        UncollectClient uncollectClient = mRetrofitClient.create(UncollectClient.class);
        Call<CommonResponse<String>> call = uncollectClient.uncollect(id);
        call.enqueue(new Callback<CommonResponse<String>>() {
            @Override
            public void onResponse(Call<CommonResponse<String>> call, Response<CommonResponse<String>> response) {
                if (response == null) {
                    mBranchView.showUncollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mBranchView.showUncollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mBranchView.showUncollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mBranchView.showUncollectSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mBranchView.showUncollectFail(new CommonException(-1, t == null ? "uncollect fail" : t.toString()));
            }
        });
    }
}
