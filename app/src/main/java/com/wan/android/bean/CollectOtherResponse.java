package com.wan.android.bean;

public class CollectOtherResponse {

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

        private String author;
        private int chapterId;
        private String chapterName;
        private int courseId;
        private String desc;
        private String envelopePic;
        private int id;
        private String link;
        private String niceDate;
        private String origin;
        private int originId;
        private long publishTime;
        private String title;
        private int userId;
        private int visible;
        private int zan;


        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthor() {
            return author;
        }


        public void setChapterid(int chapterid) {
            this.chapterId = chapterid;
        }

        public int getChapterid() {
            return chapterId;
        }


        public void setChaptername(String chaptername) {
            this.chapterName = chaptername;
        }

        public String getChaptername() {
            return chapterName;
        }


        public void setCourseid(int courseid) {
            this.courseId = courseid;
        }

        public int getCourseid() {
            return courseId;
        }


        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }


        public void setEnvelopepic(String envelopepic) {
            this.envelopePic = envelopepic;
        }

        public String getEnvelopepic() {
            return envelopePic;
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


        public void setNicedate(String nicedate) {
            this.niceDate = nicedate;
        }

        public String getNicedate() {
            return niceDate;
        }


        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getOrigin() {
            return origin;
        }


        public void setOriginid(int originid) {
            this.originId = originid;
        }

        public int getOriginid() {
            return originId;
        }


        public void setPublishtime(long publishtime) {
            this.publishTime = publishtime;
        }

        public long getPublishtime() {
            return publishTime;
        }


        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }


        public void setUserid(int userid) {
            this.userId = userid;
        }

        public int getUserid() {
            return userId;
        }


        public void setVisible(int visible) {
            this.visible = visible;
        }

        public int getVisible() {
            return visible;
        }


        public void setZan(int zan) {
            this.zan = zan;
        }

        public int getZan() {
            return zan;
        }

    }
}

