package com.wan.android.data.network.model;

import java.io.Serializable;

/**
 * @author wzc
 * @date 2018/8/18
 */
public class ContentData implements Serializable {
    private Long id;
    private String title;
    private String url;
    private Boolean isCollect;

    public ContentData(Long id, String title, String url,Boolean isCollect) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.isCollect = isCollect;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getCollect() {
        return isCollect;
    }

    public void setCollect(Boolean collect) {
        isCollect = collect;
    }

    @Override
    public String toString() {
        return "ContentData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", isCollect=" + isCollect +
                '}';
    }
}
