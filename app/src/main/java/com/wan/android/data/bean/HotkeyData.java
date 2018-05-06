package com.wan.android.data.bean;

import com.wan.android.annotation.NotProguard;

/**
 * 热搜数据类
 *
 * @author wzc
 * @date 2018/3/12
 */
@NotProguard
public class HotkeyData {

    private int id;
    private String link;
    private String name;
    private int order;
    private int visible;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getVisible() {
        return visible;
    }

}