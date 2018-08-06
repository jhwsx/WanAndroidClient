package com.wan.android.data.network.model;


import com.wan.android.di.NotProguard;

import java.util.List;

/**
 * 单页文章列表数据类
 * @author wzc
 * @date 2018/3/12
 */
@NotProguard
public class ArticleData {

    private int curPage;
    private List<ArticleDatas> datas;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;


    public void setCurpage(int curpage) {
        this.curPage = curpage;
    }

    public int getCurpage() {
        return curPage;
    }


    public void setDatas(List<ArticleDatas> datas) {
        this.datas = datas;
    }

    public List<ArticleDatas> getDatas() {
        return datas;
    }


    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }


    public void setOver(boolean over) {
        this.over = over;
    }

    public boolean getOver() {
        return over;
    }


    public void setPagecount(int pagecount) {
        this.pageCount = pagecount;
    }

    public int getPagecount() {
        return pageCount;
    }


    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }


    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

}