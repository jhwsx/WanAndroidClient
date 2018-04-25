package com.wan.android.search;

import android.util.Log;

import com.wan.android.data.client.CollectClient;
import com.wan.android.data.client.HotkeyClient;
import com.wan.android.data.client.SearchClient;
import com.wan.android.data.client.UncollectClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.HotkeyData;
import com.wan.android.data.source.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class SearchPresenter implements SearchContract.Presenter {
    private final RetrofitClient mRetrofitClient;
    private final SearchContract.View mSearchView;

    public SearchPresenter(RetrofitClient retrofitClient, SearchContract.View searchView) {
        mRetrofitClient = retrofitClient;
        mSearchView = searchView;
        mSearchView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void fetchHotkey() {
        HotkeyClient client = mRetrofitClient.create(HotkeyClient.class);
        Call<CommonResponse<List<HotkeyData>>> call = client.getHotkey();
        call.enqueue(new Callback<CommonResponse<List<HotkeyData>>>() {
            @Override
            public void onResponse(Call<CommonResponse<List<HotkeyData>>> call, Response<CommonResponse<List<HotkeyData>>> response) {
                if (response == null) {
                    mSearchView.showHotkeyFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<List<HotkeyData>> body = response.body();
                if (body == null) {
                    mSearchView.showHotkeyFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    Log.d(TAG, body.getErrormsg());
                    mSearchView.showHotkeyFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }

                final List<HotkeyData> dataList = body.getData();
                if (dataList == null) {
                    mSearchView.showHotkeyFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }

                mSearchView.showHotkeySuccess(dataList);
            }

            @Override
            public void onFailure(Call<CommonResponse<List<HotkeyData>>> call, Throwable t) {
                mSearchView.showHotkeyFail(new CommonException(-1, t == null ? "fetch hotkey fail" : t.toString()));
            }
        });
    }

    @Override
    public void swipeRefresh(String query) {
        SearchClient client = mRetrofitClient.create(SearchClient.class);
        Call<CommonResponse<ArticleData>> search = client.search(0, query);
        search.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {
                if (response == null) {
                    mSearchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<ArticleData> body = response.body();
                if (body == null) {
                    mSearchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mSearchView.showSwipeRefreshFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                ArticleData data = body.getData();
                if (data == null) {
                    mSearchView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                List<ArticleDatas> datas = data.getDatas();
                mSearchView.showSwipeRefreshSuccess(datas);
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                mSearchView.showSwipeRefreshFail(new CommonException(-1, t == null ? "swipe refresh fail" : t.toString()));


            }
        });
    }

    @Override
    public void loadMore(final int currPage, String query) {
        SearchClient client = mRetrofitClient.create(SearchClient.class);
        Call<CommonResponse<ArticleData>> call = client.search(currPage, query);
        call.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {
                if (response == null) {
                    mSearchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<ArticleData> body = response.body();
                if (body == null) {
                    mSearchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mSearchView.showLoadMoreFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                ArticleData data = body.getData();
                if (data == null) {
                    mSearchView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                if (currPage + 1 < data.getPagecount()) {
                    mSearchView.showLoadMoreComplete();
                } else {
                    mSearchView.showLoadMoreEnd();
                }
                List<ArticleDatas> datas = data.getDatas();
                mSearchView.showLoadMoreSuccess(datas);
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                mSearchView.showLoadMoreFail(new CommonException(-1, t == null ? "load more fail" : t.toString()));
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

                if (response == null) {
                    mSearchView.showCollectFail(new CommonException(-1, "response cannot be null"));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mSearchView.showCollectFail(new CommonException(-1, "body cannot be null"));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mSearchView.showCollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mSearchView.showCollectSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mSearchView.showCollectFail(new CommonException(-1, t == null ? "collect fail" : t.toString()));
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
                    mSearchView.showUncollectFail(new CommonException(-1, "response cannot be null"));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mSearchView.showUncollectFail(new CommonException(-1, "body cannot be null"));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mSearchView.showUncollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mSearchView.showUncollectSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mSearchView.showUncollectFail(new CommonException(-1, t == null ? "uncollect fail" : t.toString()));
            }
        });
    }
}
