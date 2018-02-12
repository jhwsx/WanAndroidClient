package com.wan.android.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class TreeListResponse {

   private ArrayList<Data> data;
   private int errorCode;
   private String errorMsg;


    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
    public ArrayList<Data> getData() {
        return data;
    }
    

    public void setErrorcode(int errorcode) {
        this.errorCode = errorcode;
    }
    public int getErrorcode() {
        return errorCode;
    }
    

    public void setErrormsg(String errormsg) {
        this.errorMsg = errormsg;
    }
    public String getErrormsg() {
        return errorMsg;
    }

    public class Data implements Serializable {

        private ArrayList<Children> children;
        private int courseId;
        private int id;
        private String name;
        private int order;
        private int parentChapterId;
        private int visible;


        public void setChildren(ArrayList<Children> children) {
            this.children = children;
        }
        public ArrayList<Children> getChildren() {
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


        public class Children implements Serializable{

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
}


   



   
