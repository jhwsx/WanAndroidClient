package com.wan.android.data.network.model;


import com.wan.android.di.NotProguard;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * banner数据类
 *
 * @author wzc
 * @date 2018/3/12
 */
@NotProguard
@Entity
public class BannerData {

    @Id
    private Long id;
    private String desc;
    private String imagePath;
    private int isVisible;
    private int order;
    private String title;
    private int type;
    private String url;


    @Generated(hash = 364356521)
    public BannerData(Long id, String desc, String imagePath, int isVisible,
                      int order, String title, int type, String url) {
        this.id = id;
        this.desc = desc;
        this.imagePath = imagePath;
        this.isVisible = isVisible;
        this.order = order;
        this.title = title;
        this.type = type;
        this.url = url;
    }

    @Generated(hash = 1707770751)
    public BannerData() {
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsVisible() {
        return this.isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }
}