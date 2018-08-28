package com.wan.android.ui.content;

import com.wan.android.ui.base.MvpPresenter;
import com.wan.android.ui.base.MvpView;

/**
 * @author wzc
 * @date 2018/8/28
 */
public interface ContentContract {
    interface View extends MvpView {
        void showCollectInSiteArticleSuccess();

        void showUncollectArticleListArticleSuccess();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {

        void collectInSiteArticle(int id);

        void uncollectArticleListArticle(int id);

        boolean getLoginStaus();
    }

}
