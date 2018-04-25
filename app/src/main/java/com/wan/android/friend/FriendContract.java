package com.wan.android.friend;

import com.wan.android.BasePresenter;
import com.wan.android.BaseView;
import com.wan.android.data.bean.CommonException;
import com.wan.android.data.bean.FriendData;

import java.util.List;

/**
 * @author wzc
 * @date 2018/3/29
 */
public interface FriendContract {
    interface View extends BaseView<Presenter> {
        // 获取朋友成功
        void showGetFriendSuccess(List<FriendData> data);
        // 获取朋友失败
        void showGetFriengFail(CommonException e);
    }
    interface Presenter extends BasePresenter {
        void getFriend();
    }

}
