package com.wan.android.data.network.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 分支数据类
 * @author wzc
 * @date 2018/3/12
 */
public class BranchData implements Serializable {

        private ArrayList<Leaf> children;
        private int courseId;
        private int id;
        private String name;
        private int order;
        private int parentChapterId;
        private int visible;


        public void setChildren(ArrayList<Leaf> children) {
            this.children = children;
        }
        public ArrayList<Leaf> getChildren() {
            return children;
        }


        public void setCourseid(int courseid) {
            this.courseId = courseid;
        }
        public int getCourseid() {
            return courseId;
        }


        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
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


        public void setParentchapterid(int parentchapterid) {
            this.parentChapterId = parentchapterid;
        }
        public int getParentchapterid() {
            return parentChapterId;
        }


        public void setVisible(int visible) {
            this.visible = visible;
        }
        public int getVisible() {
            return visible;
        }


        public static class Leaf implements Serializable{

            private ArrayList<String> children;
            private int courseId;
            private int id;
            private String name;
            private int order;
            private int parentChapterId;
            private int visible;


            public void setChildren(ArrayList<String> children) {
                this.children = children;
            }
            public ArrayList<String> getChildren() {
                return children;
            }


            public void setCourseid(int courseid) {
                this.courseId = courseid;
            }
            public int getCourseid() {
                return courseId;
            }


            public void setId(int id) {
                this.id = id;
            }
            public int getId() {
                return id;
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


            public void setParentchapterid(int parentchapterid) {
                this.parentChapterId = parentchapterid;
            }
            public int getParentchapterid() {
                return parentChapterId;
            }


            public void setVisible(int visible) {
                this.visible = visible;
            }
            public int getVisible() {
                return visible;
            }

        }

    }