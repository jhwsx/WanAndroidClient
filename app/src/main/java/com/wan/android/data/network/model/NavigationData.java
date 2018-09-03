package com.wan.android.data.network.model;


import com.wan.android.di.NotProguard;

import java.util.List;

/**
 * @author wzc
 * @date 2018/3/20
 */
@NotProguard
public class NavigationData {
    private List<ArticleDatas> articles;
    private int cid;
    private String name;


    public void setArticles(List<ArticleDatas> articles) {
        this.articles = articles;
    }

    public List<ArticleDatas> getArticles() {
        return articles;
    }


    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCid() {
        return cid;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
