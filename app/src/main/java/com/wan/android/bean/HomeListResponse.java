package com.wan.android.bean;

import java.util.List;

public class HomeListResponse {

   private Data data;
   private int errorCode;
   private String errorMsg;


    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
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

    public class Data {

        private int curPage;
        private List<Datas> datas;
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


        public void setDatas(List<Datas> datas) {
            this.datas = datas;
        }

        public List<Datas> getDatas() {
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


        public class Datas {

            private String apkLink;
            private String author;
            private int chapterId;
            private String chapterName;
            private boolean collect;
            private int courseId;
            private String desc;
            private String envelopePic;
            private int id;
            private String link;
            private String niceDate;
            private String origin;
            private String projectLink;
            private long publishTime;
            private String title;
            private int visible;
            private int zan;

            public String getApkLink() {
                return apkLink;
            }

            public void setApkLink(String apkLink) {
                this.apkLink = apkLink;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public int getChapterId() {
                return chapterId;
            }

            public void setChapterId(int chapterId) {
                this.chapterId = chapterId;
            }

            public String getChapterName() {
                return chapterName;
            }

            public void setChapterName(String chapterName) {
                this.chapterName = chapterName;
            }

            public boolean isCollect() {
                return collect;
            }

            public void setCollect(boolean collect) {
                this.collect = collect;
            }

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getEnvelopePic() {
                return envelopePic;
            }

            public void setEnvelopePic(String envelopePic) {
                this.envelopePic = envelopePic;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getNiceDate() {
                return niceDate;
            }

            public void setNiceDate(String niceDate) {
                this.niceDate = niceDate;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public String getProjectLink() {
                return projectLink;
            }

            public void setProjectLink(String projectLink) {
                this.projectLink = projectLink;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(long publishTime) {
                this.publishTime = publishTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getVisible() {
                return visible;
            }

            public void setVisible(int visible) {
                this.visible = visible;
            }

            public int getZan() {
                return zan;
            }

            public void setZan(int zan) {
                this.zan = zan;
            }
        }

    }



    
}

   


   
