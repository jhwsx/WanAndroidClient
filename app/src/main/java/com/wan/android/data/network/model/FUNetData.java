package com.wan.android.data.network.model;


import com.wan.android.di.NotProguard;

/**
 * @author wzc
 * @date 2018/5/31
 */
@NotProguard
public class FUNetData {
    private String desc;
    private String icon;
    private int id;
    private String link;
    private String name;
    private int order;
    private int userId;
    private int visible;


    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getIcon() {
        return icon;
    }


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


    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }


    public void setVisible(int visible) {
        this.visible = visible;
    }
    public int getVisible() {
        return visible;
    }

}
