package com.wan.android.collect;

import com.wan.android.data.client.CollectListClient;
import com.wan.android.data.client.CollectOtherClient;
import com.wan.android.data.client.UncollectAllClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.bean.CollectData;
import com.wan.android.data.bean.CollectDatas;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.source.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/3/29
 */
public class CollectPresenter implements CollectContract.Presenter {
    private final RetrofitClient mRetrofitClient;
    private final CollectContract.View mCollectView;

    public CollectPresenter(RetrofitClient retrofitClient, CollectContract.View collectView) {
        mRetrofitClient = retrofitClient;
        mCollectView = collectView;
        mCollectView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void swipeRefresh() {
        CollectListClient client = mRetrofitClient.create(CollectListClient.class);
        Call<CommonResponse<CollectData>> call = client.getCollect(0);
        call.enqueue(new Callback<CommonResponse<CollectData>>() {
            @Override
            public void onResponse(Call<CommonResponse<CollectData>> call, Response<CommonResponse<CollectData>> response) {
                if (response == null) {
                    mCollectView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<CollectData> body = response.body();
                if (body == null) {
                    mCollectView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                CollectData data = body.getData();
                if (data == null) {
                    mCollectView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                List<CollectDatas> datas = data.getDatas();
                mCollectView.showSwipeRefreshSuccess(datas);
            }

            @Override
            public void onFailure(Call<CommonResponse<CollectData>> call, Throwable t) {
                mCollectView.showSwipeRefreshFail(new CommonException(-1, t == null ? "swipe refresh fail" : t.toString()));
            }
        });
    }

    @Override
    public void loadMore(final int currPage) {
        CollectListClient client = mRetrofitClient.create(CollectListClient.class);
        Call<CommonResponse<CollectData>> call = client.getCollect(currPage);
        call.enqueue(new Callback<CommonResponse<CollectData>>() {
            @Override
            public void onResponse(Call<CommonResponse<CollectData>> call, Response<CommonResponse<CollectData>> response) {
                if (response == null) {
                    mCollectView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<CollectData> body = response.body();
                if (body == null) {
                    mCollectView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                CollectData data = body.getData();
                if (data == null) {
                    mCollectView.showLoadMoreFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }
                if (currPage + 1 < data.getPagecount()) {
                    mCollectView.showLoadMoreComplete();
//                    mCollectAdapter.loadMoreComplete();
                } else {
                    mCollectView.showLoadMoreEnd();
//                    mCollectAdapter.loadMoreEnd();
                }
                mCollectView.showLoadMoreSuccess(data.getDatas());
//                List<CollectDatas> datas = data.getDatas();
//                mDatasList.addAll(datas);
//                mCollectAdapter.notifyDataSetChanged();
//                mLoadService.showSuccess();
            }

            @Override
            public void onFailure(Call<CommonResponse<CollectData>> call, Throwable t) {
                mCollectView.showLoadMoreFail(new CommonException(-1, t == null ? "load more fail" : t.toString()));
            }
        });
    }

    @Override
    public void uncollect(int id, int originalId) {
        UncollectAllClient client = mRetrofitClient.create(UncollectAllClient.class);
        Call<CommonResponse<String>> call = client.uncollectAll(id, originalId);
        call.enqueue(new Callback<CommonResponse<String>>() {
            @Override
            public void onResponse(Call<CommonResponse<String>> call, Response<CommonResponse<String>> response) {
                if (response == null) {
                    mCollectView.showUncollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<String> body = response.body();
                if (body == null) {
                    mCollectView.showUncollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mCollectView.showUncollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                mCollectView.showUncollectSuccess();
//                Toast.makeText(mActivity, R.string.uncollect_successfully, Toast.LENGTH_SHORT).show();
//                mDatasList.remove(position);
//                mCollectAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                mCollectView.showUncollectFail(new CommonException(-1, t == null ? "uncollect fail" : t.toString()));
            }
        });
    }

    @Override
    public void addCollect(String title, String author, String link) {
        CollectOtherClient client = mRetrofitClient.create(CollectOtherClient.class);
        Call<CommonResponse<CollectDatas>> call = client.collectOther(title, author, link);
        call.enqueue(new Callback<CommonResponse<CollectDatas>>() {
            @Override
            public void onResponse(Call<CommonResponse<CollectDatas>> call, Response<CommonResponse<CollectDatas>> response) {
                if (response == null) {
                    mCollectView.showAddCollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<CollectDatas> body = response.body();
                if (body == null) {
                    mCollectView.showAddCollectFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                if (body.getErrorcode() != 0) {
                    mCollectView.showAddCollectFail(new CommonException(body.getErrorcode(), body.getErrormsg()));
                    return;
                }
                CollectDatas data = body.getData();
                mCollectView.showAddCollectSuccess(data);
            }

            @Override
            public void onFailure(Call<CommonResponse<CollectDatas>> call, Throwable t) {

                mCollectView.showAddCollectFail(new CommonException(-1, t == null ? "addcollect fail" : t.toString()));

            }
        });
    }
}
