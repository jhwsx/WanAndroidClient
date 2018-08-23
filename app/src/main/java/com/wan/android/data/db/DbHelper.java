package com.wan.android.data.db;

import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.SearchHistoryData;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author wzc
 * @date 2018/8/8
 */
public interface DbHelper {
    Observable<List<ArticleDatas>> getDbHomeArticles();

    Observable<Boolean> saveHomeArticles2Db(List<ArticleDatas> data);

    Observable<List<BannerData>> getDbBanner();

    Observable<Boolean> saveBanner2Db(List<BannerData> data);

    Observable<Boolean> saveSearchHistory2Db(SearchHistoryData data);

    Observable<List<SearchHistoryData>> getDbSearchHistory();

    Observable<Boolean> deleteDbSearchHistory();
}
