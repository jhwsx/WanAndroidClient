package com.wan.android.data.db;

import com.wan.android.data.network.model.ArticleDatas;
import com.wan.android.data.network.model.ArticleDatasDao;
import com.wan.android.data.network.model.BannerData;
import com.wan.android.data.network.model.DaoMaster;
import com.wan.android.data.network.model.DaoSession;
import com.wan.android.data.network.model.SearchHistoryData;
import com.wan.android.data.network.model.SearchHistoryDataDao;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author wzc
 * @date 2018/8/8
 */
@Singleton
public class AppDbHelper implements DbHelper {

    private final DaoSession mDaoSession;

    @Inject
    public AppDbHelper(DaoMaster.OpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public Observable<List<ArticleDatas>> getDbHomeArticles() {
        // 返回一个 Observable 对象, 特点是当 observer 订阅它时, 才会调用指定的函数,并发送函数的返回值
        return Observable.fromCallable(new Callable<List<ArticleDatas>>() {
            @Override
            public List<ArticleDatas> call() throws Exception {
                return mDaoSession.getArticleDatasDao()
                        .queryBuilder()
                        // 按 id 降序排列, 否则取出的缓存新的在下边, 旧的在上边
                        .orderDesc(ArticleDatasDao.Properties.Id)
                        .build()
                        .list();
            }
        });
    }

    @Override
    public Observable<Boolean> saveHomeArticles2Db(final List<ArticleDatas> data) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                // 插入或替换给定的实体, 如果只用插入的话, id 重复时会报错
                mDaoSession.getArticleDatasDao().insertOrReplaceInTx(data);
                return true;
            }
        });
    }

    @Override
    public Observable<List<BannerData>> getDbBanner() {
        return Observable.fromCallable(new Callable<List<BannerData>>() {
            @Override
            public List<BannerData> call() throws Exception {
                return mDaoSession.getBannerDataDao().loadAll();
            }
        });
    }

    @Override
    public Observable<Boolean> saveBanner2Db(final List<BannerData> data) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getBannerDataDao().insertOrReplaceInTx(data);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveSearchHistory2Db(final SearchHistoryData data) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<SearchHistoryData> list = mDaoSession.getSearchHistoryDataDao().queryBuilder()
                        .where(SearchHistoryDataDao.Properties.Key.eq(data.getKey()))
                        .build()
                        .list();
                if (list.isEmpty()) {
                    mDaoSession.getSearchHistoryDataDao().insertOrReplace(data);
                }
                return true;
            }
        });
    }

    @Override
    public Observable<List<SearchHistoryData>> getDbSearchHistory() {
        return Observable.fromCallable(new Callable<List<SearchHistoryData>>() {
            @Override
            public List<SearchHistoryData> call() throws Exception {
                return mDaoSession.getSearchHistoryDataDao().loadAll();
            }
        });
    }

    @Override
    public Observable<Boolean> deleteDbSearchHistory() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getSearchHistoryDataDao().deleteAll();
                return true;
            }
        });
    }
}
