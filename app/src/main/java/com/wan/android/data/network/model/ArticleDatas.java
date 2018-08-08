package com.wan.android.data.network.model;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 文章列表条目数据类
 *
 * @author wzc
 * @date 2018/3/12
 */
@Entity
public class ArticleDatas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    private String apkLink;
    private String author;
    private Integer chapterId;
    private String chapterName;
    private Boolean collect;
    private Integer courseId;
    private String desc;
    private String envelopePic;
    private Boolean fresh;
    private String link;
    private String niceDate;
    private String origin;
    private String projectLink;
    private Long publishTime;
    private Integer superChapterId;
    private String superChapterName;
    @Convert(columnType = String.class, converter = TagsBeanConverter.class)
    private List<TagsBean> tags;
    private int userId;
    private String title;
    private Integer type;
    private Integer visible;
    private Integer zan;

    @Generated(hash = 619487330)
    public ArticleDatas(Long id, String apkLink, String author, Integer chapterId,
            String chapterName, Boolean collect, Integer courseId, String desc,
            String envelopePic, Boolean fresh, String link, String niceDate,
            String origin, String projectLink, Long publishTime,
            Integer superChapterId, String superChapterName, List<TagsBean> tags,
            int userId, String title, Integer type, Integer visible, Integer zan) {
        this.id = id;
        this.apkLink = apkLink;
        this.author = author;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.collect = collect;
        this.courseId = courseId;
        this.desc = desc;
        this.envelopePic = envelopePic;
        this.fresh = fresh;
        this.link = link;
        this.niceDate = niceDate;
        this.origin = origin;
        this.projectLink = projectLink;
        this.publishTime = publishTime;
        this.superChapterId = superChapterId;
        this.superChapterName = superChapterName;
        this.tags = tags;
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.visible = visible;
        this.zan = zan;
    }

    @Generated(hash = 348759666)
    public ArticleDatas() {
    }

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

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Boolean isCollect() {
        return collect;
    }

    public void setCollect(Boolean collect) {
        this.collect = collect;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
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

    public Boolean isFresh() {
        return fresh;
    }

    public void setFresh(Boolean fresh) {
        this.fresh = fresh;
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

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getSuperChapterId() {
        return superChapterId;
    }

    public void setSuperChapterId(Integer superChapterId) {
        this.superChapterId = superChapterId;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public Integer getZan() {
        return zan;
    }

    public void setZan(Integer zan) {
        this.zan = zan;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Boolean getCollect() {
        return this.collect;
    }

    public Boolean getFresh() {
        return this.fresh;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public static class TagsBean implements Serializable {
        /**
         * name : 项目
         * url : /project/list/1?cid=294
         */
        private static final long serialVersionUID = 2L;
        @Expose
        private String name;
        @Expose
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}