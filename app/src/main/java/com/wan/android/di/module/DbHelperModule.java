package com.wan.android.di.module;

import android.content.Context;

import com.wan.android.data.db.AppDbHelper;
import com.wan.android.data.db.DbHelper;
import com.wan.android.data.network.model.DaoMaster;
import com.wan.android.di.ApplicationContext;
import com.wan.android.di.DatabaseInfo;
import com.wan.android.util.constant.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author wzc
 * @date 2018/8/8
 */
@Module
public class DbHelperModule {

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    DaoMaster.OpenHelper provideOpenHelper(@ApplicationContext Context context,
                                           @DatabaseInfo String name) {
        return new DaoMaster.DevOpenHelper(context, name);
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

}
