package com.wan.android.data.network.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author wzc
 * @date 2018/7/20
 */
public class ProjectTreeBranchData implements Serializable{

    private Integer courseId;
    private Integer id;
    private String name;
    private Integer order;
    private Integer parentChapterId;
    private Integer visible;
    private ArrayList<?> children;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(Integer parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public ArrayList<?> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<?> children) {
        this.children = children;
    }
}
