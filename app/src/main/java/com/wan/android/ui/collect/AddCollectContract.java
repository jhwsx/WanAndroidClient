package com.wan.android.ui.collect;

import com.wan.android.data.network.model.CollectDatas;
import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

/**
 * @author wzc
 * @date 2018/8/28
 */
public interface AddCollectContract {
    interface View extends MvpView {
        void showCollectOutOfSiteArticleSuccess(CollectDatas datas);

        void showCollectOutOfSiteArticleFail();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {
        void collectOutOfSiteArticle(String title, String author, String link);
    }

}
