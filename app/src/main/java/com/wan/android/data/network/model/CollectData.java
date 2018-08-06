package com.wan.android.data.network.model;


import com.wan.android.di.NotProguard;

import java.util.List;

/**
 * 收藏列表单页数据类
 *
 * @author wzc
 * @date 2018/3/12
 */
@NotProguard
public class CollectData {

    private int curPage;
    private List<CollectDatas> datas;
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


    public void setDatas(List<CollectDatas> datas) {
        this.datas = datas;
    }

    public List<CollectDatas> getDatas() {
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
//        public class ArticleDatas {
//
//            private String author;
//            private int chapterId;
//            private String chapterName;
//            private int courseId;
//            private String desc;
//            private String envelopePic;
//            private int id;
//            private String link;
//            private String niceDate;
//            private String origin;
//            private int originId;
//            private long publishTime;
//            private String title;
//            private int userId;
//            private int visible;
//            private int zan;
//
//
//            public void setAuthor(String author) {
//                this.author = author;
//            }
//            public String getAuthor() {
//                return author;
//            }
//
//
//            public void setChapterid(int chapterid) {
//                this.chapterId = chapterid;
//            }
//            public int getChapterid() {
//                return chapterId;
//            }
//
//
//            public void setChaptername(String chaptername) {
//                this.chapterName = chaptername;
//            }
//            public String getChaptername() {
//                return chapterName;
//            }
//
//
//            public void setCourseid(int courseid) {
//                this.courseId = courseid;
//            }
//            public int getCourseid() {
//                return courseId;
//            }
//
//
//            public void setDesc(String desc) {
//                this.desc = desc;
//            }
//            public String getDesc() {
//                return desc;
//            }
//
//
//            public void setEnvelopepic(String envelopepic) {
//                this.envelopePic = envelopepic;
//            }
//            public String getEnvelopepic() {
//                return envelopePic;
//            }
//
//
//            public void setId(int id) {
//                this.id = id;
//            }
//            public int getId() {
//                return id;
//            }
//
//
//            public void setLink(String link) {
//                this.link = link;
//            }
//            public String getLink() {
//                return link;
//            }
//
//
//            public void setNicedate(String nicedate) {
//                this.niceDate = nicedate;
//            }
//            public String getNicedate() {
//                return niceDate;
//            }
//
//
//            public void setOrigin(String origin) {
//                this.origin = origin;
//            }
//            public String getOrigin() {
//                return origin;
//            }
//
//
//            public void setOriginid(int originid) {
//                this.originId = originid;
//            }
//            public int getOriginid() {
//                return originId;
//            }
//
//
//            public void setPublishtime(long publishtime) {
//                this.publishTime = publishtime;
//            }
//            public long getPublishtime() {
//                return publishTime;
//            }
//
//
//            public void setTitle(String title) {
//                this.title = title;
//            }
//            public String getTitle() {
//                return title;
//            }
//
//
//            public void setUserid(int userid) {
//                this.userId = userid;
//            }
//            public int getUserid() {
//                return userId;
//            }
//
//
//            public void setVisible(int visible) {
//                this.visible = visible;
//            }
//            public int getVisible() {
//                return visible;
//            }
//
//
//            public void setZan(int zan) {
//                this.zan = zan;
//            }
//            public int getZan() {
//                return zan;
//            }
//
//        }
}