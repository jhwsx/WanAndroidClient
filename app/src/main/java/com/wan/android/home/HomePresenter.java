package com.wan.android.home;

import android.util.Log;

import com.wan.android.BuildConfig;
import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.BannerData;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.client.BannerClient;
import com.wan.android.data.client.CollectClient;
import com.wan.android.data.client.HomeListClient;
import com.wan.android.data.client.UncollectClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.source.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link HomeFragment}), retrieves the data and updates the
 * UI as required.
 */
public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = HomePresenter.class.getSimpleName();
    private final RetrofitClient mRetrofitClient;
    private final HomeContract.View mHomeView;


    public HomePresenter(RetrofitClient retrofitClient, HomeContract.View homeView) {
        mRetrofitClient = checkNotNull(retrofitClient, "retrofitClient cannot be null");
        mHomeView = checkNotNull(homeView, "homeView cannot be null");
        mHomeView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void swipeRefresh() {
        HomeListClient client = mRetrofitClient.create(HomeListClient.class);
        Call<CommonResponse<ArticleData>> call = client.getHomeFeed(0);
        call.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "response: " + response);
                }
                if (response == null) {
                    mHomeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<ArticleData> body = response.body();
                if (body == null) {
                    mHomeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                ArticleData data = body.getData();
                if (data == null) {
                    mHomeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                List<ArticleDatas> datas = data.getDatas();
                mHomeView.showSwipeRefreshSuccess(datas);
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "t:" + t);
                }
                mHomeView.showSwipeRefreshFail(new CommonException(-1, t == null ? "swipe refresh fail" : t.toString()));
            }
        });
    }

    @Override
    public void loadMore(final int currPage) {
        HomeListClient client = mRetrofitClient.create(HomeListClient.class);
        Call<CommonResponse<ArticleData>> call = client.getHomeFeed(currPage);
        call.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {

                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "response: " + response);
                }
                if (response == null) {
                    mHomeView.showLoadMoreFail(new CommonException(-1, "response cannot be null"));
                    return;
                }
                CommonResponse<ArticleData> body = response.body();
                if (body.getData() == null) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "loadMore() onResponse no data");
                    }
                    mHomeView.showLoadMoreFail(new CommonException(-1, "no data"));
                    return;
                }

                int nextPage = currPage + 1;
                if (nextPage < response.body().getData().getPagecount()) {
                    mHomeView.showLoadMoreComplete();
                } else {
                    mHomeView.showLoadMoreEnd();
                }


                ArticleData data = body.getData();
                List<ArticleDatas> datas = data.getDatas();
                mHomeView.showLoadMoreSuccess(datas);
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    Log.d("HomeFragment", "t:" + t);
                }

                mHomeView.showLoadMoreFail(new CommonException(-1, t == null ? "load more fail" : t.toString()));

            }
        });
    }

    @Override
    public void fetchBanner() {
        BannerClient client = mRetrofitClient.create(BannerClient.class);
        Call<CommonResponse<List<BannerData>>> call = client.getBanner();
        call.enqueue(new Callback<CommonResponse<List<BannerData>>>() {
            @Override
            public void onResponse(Call<CommonResponse<List<BannerData>>> call, Response<CommonResponse<List<BannerData>>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "getBanner() onResponse response:" + response);
                }
                if (response == null) {
                    mHomeView.showBannerFail(new CommonException(-1, "response cannot be null"));
                    return;
                }
                CommonResponse<List<BannerData>> body = response.body();
                if (body == null) {
                    mHomeView.showBannerFail(new CommonException(-1, "body cannot be null"));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "getBanner() onResponse ic_error msg: " + body.getErrormsg());
                    }
                    mHomeView.showBannerFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                if (body.getData() == null || body.getData().size() == 0) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "getBanner() onResponse no data");
                    }
                    mHomeView.showBannerFail(new CommonException(-1, "no data"));
                    return;
                }
                mHomeView.showBannerSuccess(body.getData());
            }

            @Override
            public void onFailure(Call<CommonResponse<List<BannerData>>> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "getBanner() onFailure: t = " + t);
                }
                mHomeView.showBannerFail(new CommonException(-1, t == null ? "fetch banner fail" : t.toString()));

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
                    mHomeView.showCollectFail(new CommonException(-1, "response cannot be null"));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mHomeView.showCollectFail(new CommonException(-1, "body cannot be null"));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mHomeView.showCollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mHomeView.showCollectSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mHomeView.showCollectFail(new CommonException(-1, t == null ? "collect fail" : t.toString()));
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
                    mHomeView.showUncollectFail(new CommonException(-1, "response cannot be null"));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mHomeView.showUncollectFail(new CommonException(-1, "body cannot be null"));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mHomeView.showUncollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mHomeView.showUncollectSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mHomeView.showUncollectFail(new CommonException(-1, t == null ? "uncollect fail" : t.toString()));
            }
        });
    }
}
