package com.wan.android.ui.search;

import com.wan.android.data.network.model.HotkeyData;
import com.wan.android.data.network.model.SearchHistoryData;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

import java.util.List;

/**
 * @author wzc
 * @date 2018/8/21
 */
public interface SearchContract {
    interface View extends MvpView {
        void showHotkeySuccess(List<HotkeyData> data);

        void showGetDbSearchHistorySuccess(List<SearchHistoryData> data);

        void showSaveSearchHistory2DbSuccess();

        void showDeleteDbSearchHistorySuccess();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {
        /**
         * 搜索热词
         */
        void getHotkey();

        /**
         * 保存搜索历史到数据库
         * @param data
         */
        void saveSearchHistory2Db(SearchHistoryData data);

        /**
         * 从数据库获取搜索历史
         */
        void getDbSearchHistory();

        /**
         * 删除数据库的搜索历史
         */
        void deleteDbSearchHistory();
    }


}
