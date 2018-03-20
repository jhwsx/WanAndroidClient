package com.wan.android.bean;

import java.util.ArrayList;

/**
 * @author wzc
 * @date 2018/3/20
 */
public class NavigationData {
    private ArrayList<ArticleDatas> articles;
    private int cid;
    private String name;


    public void setArticles(ArrayList<ArticleDatas> articles) {
        this.articles = articles;
    }
    public ArrayList<ArticleDatas> getArticles() {
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
