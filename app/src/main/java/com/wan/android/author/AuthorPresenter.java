package com.wan.android.author;

import com.wan.android.data.client.AuthorClient;
import com.wan.android.data.client.CollectClient;
import com.wan.android.data.client.UncollectClient;
import com.wan.android.data.bean.ArticleData;
import com.wan.android.data.bean.ArticleDatas;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.source.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/3/30
 */
public class AuthorPresenter implements AuthorContract.Presenter {
    private final RetrofitClient mRetrofitClient;
    private final AuthorContract.View mAuthorView;

    public AuthorPresenter(RetrofitClient retrofitClient, AuthorContract.View authorView) {
        mRetrofitClient = retrofitClient;
        mAuthorView = authorView;
        mAuthorView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void swipeRefresh(String author) {
        AuthorClient client = mRetrofitClient.create(AuthorClient.class);
        Call<CommonResponse<ArticleData>> call = client.getAuthorFilter(0, author);
        call.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {

                if (response == null) {
                    mAuthorView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<ArticleData> body = response.body();
                if (body == null) {
                    mAuthorView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if ( body.getData() == null ) {
                    mAuthorView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                ArticleData data = body.getData();
                List<ArticleDatas> datas = data.getDatas();
                mAuthorView.showSwipeRefreshSuccess(datas);
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                mAuthorView.showSwipeRefreshFail(new CommonException(-1, t == null ? "swipe refresh fail" : t.toString()));

            }
        });
    }

    @Override
    public void loadMore(final int currPage, String author) {
        AuthorClient client = mRetrofitClient.create(AuthorClient.class);
        Call<CommonResponse<ArticleData>> call = client.getAuthorFilter(currPage, author);
        call.enqueue(new Callback<CommonResponse<ArticleData>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArticleData>> call, Response<CommonResponse<ArticleData>> response) {

                if (response == null) {
                    mAuthorView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<ArticleData> body = response.body();
                if (body == null) {
                    mAuthorView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getData() == null ) {
                    mAuthorView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                int nextPage = currPage + 1;
                if (nextPage < response.body().getData().getPagecount()) {
                    mAuthorView.showLoadMoreComplete();
                } else {
                    mAuthorView.showLoadMoreEnd();
                }

                ArticleData data = body.getData();
                List<ArticleDatas> datas = data.getDatas();
                mAuthorView.showLoadMoreSuccess(datas);
            }

            @Override
            public void onFailure(Call<CommonResponse<ArticleData>> call, Throwable t) {
                mAuthorView.showLoadMoreFail(new CommonException(-1, t == null ? "load more fail" : t.toString()));
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
                    mAuthorView.showCollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mAuthorView.showCollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mAuthorView.showCollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mAuthorView.showCollectSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mAuthorView.showCollectFail(new CommonException(-1, t == null ? "collect fail" : t.toString()));
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
                    mAuthorView.showUncollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mAuthorView.showUncollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mAuthorView.showUncollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mAuthorView.showUncollectSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mAuthorView.showUncollectFail(new CommonException(-1, t == null ? "uncollect fail" : t.toString()));
            }
        });
    }
}
