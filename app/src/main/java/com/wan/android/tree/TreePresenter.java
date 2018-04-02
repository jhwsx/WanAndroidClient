package com.wan.android.tree;

import com.wan.android.data.bean.BranchData;
import com.wan.android.data.bean.CommonResponse;
import com.wan.android.data.client.TreeListClient;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.ErrorCodeMessageEnum;
import com.wan.android.data.source.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class TreePresenter implements TreeContract.Presenter {
    private final RetrofitClient mRetrofitClient;
    private final TreeContract.View mTreeView;

    public TreePresenter(RetrofitClient retrofitClient, TreeContract.View treeView) {
        mRetrofitClient = retrofitClient;
        mTreeView = treeView;
        mTreeView.setPresenter(this);
    }

    @Override
    public void swipeRefresh() {
        TreeListClient client = mRetrofitClient.create(TreeListClient.class);
        Call<CommonResponse<ArrayList<BranchData>>> call = client.getTree();
        call.enqueue(new Callback<CommonResponse<ArrayList<BranchData>>>() {
            @Override
            public void onResponse(Call<CommonResponse<ArrayList<BranchData>>> call, Response<CommonResponse<ArrayList<BranchData>>> response) {
                if (response == null) {
                    mTreeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_RESPONSE));
                    return;
                }
                CommonResponse<ArrayList<BranchData>> body = response.body();
                if (body == null) {
                    mTreeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_BODY));
                    return;
                }
                List<BranchData> data = body.getData();
                if (data == null || data.size() <= 0) {
                    mTreeView.showSwipeRefreshFail(CommonException.convert(ErrorCodeMessageEnum.NULL_DATA));
                    return;
                }

                mTreeView.showSwipeRefreshSuccess(data);
            }

            @Override
            public void onFailure(Call<CommonResponse<ArrayList<BranchData>>> call, Throwable t) {
                mTreeView.showSwipeRefreshFail(new CommonException(-1, t == null ? "swipe refresh fail" : t.toString()));
            }
        });
    }

    @Override
    public void start() {

    }
}
