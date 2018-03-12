package com.wan.android.helper;

import android.widget.Toast;

import com.wan.android.R;
import com.wan.android.bean.CommonResponse;
import com.wan.android.client.CollectClient;
import com.wan.android.listener.OnCollectSuccessListener;
import com.wan.android.retrofit.RetrofitClient;
import com.wan.android.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wzc
 * @date 2018/3/12
 */
public class CollectHelper {

    public static void collect(int id, final OnCollectSuccessListener listener) {
        CollectClient collectClient = RetrofitClient.create(CollectClient.class);
        Call<CommonResponse<String>> call = collectClient.collect(id);
        call.enqueue(new Callback<CommonResponse<String>>() {
            @Override
            public void onResponse(Call<CommonResponse<String>> call, Response<CommonResponse<String>> response) {
                CommonResponse<String> body = response.body();
                if (body.getErrorcode() != 0) {
                    Toast.makeText(Utils.getContext(), body.getErrormsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(Utils.getContext(), R.string.collect_successfully, Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onCollectSuccess();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse<String>> call, Throwable t) {
                Toast.makeText(Utils.getContext(), Utils.getContext().getString(R.string.collect_failed) + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
