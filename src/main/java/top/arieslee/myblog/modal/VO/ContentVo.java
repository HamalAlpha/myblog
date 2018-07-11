package top.arieslee.myblog.modal.VO;

/**
 * @ClassName ContentVo
 * @Description content表实体类
 * @Author Aries
 * @Date 2018/7/11 8:36
 * @Version 1.0
 **/
public class ContentVo {
    /**
     * @Description : 内容id
     **/
    private Integer cid;
    /**
     * @Description : 内容标题
     **/
    private String title;
    /**
     * @Description :缩写
     **/
    private String slug;
    /**
     * @Description : 创建时间戳
     **/
    private Long created;
    /**
     * @Description : 最后一次修改时间戳
     **/
    private Long modified;
    /**
     * @Description : 内容
     **/
    private String content;
    /**
     * @Description : 内容所属者id
     **/
    private Integer authodId;
    /**
     * @Description : 内容类型
     **/
    private String type;
    /**
     * @Description : 内容状态
     **/
    private String stauts;
    /**
     * @Description : 内容标签
     **/
    private String tags;
    /**
     * @Description :分类
     **/
    private String catagories;
    /**
     * @Description : 点击次数
     **/
    private Integer hits;
    /**
     * @Description : 评论数量
     **/
    private Integer commentsNum;
    /**
     * @Description : 是否允许评论
     **/
    private Boolean allowComment;
    /**
     * @Description : 是否允许ping
     **/
    private Boolean allowPing;
    /**
     * @Description : 是否聚合
     **/
    private Boolean allowFeed;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAuthodId() {
        return authodId;
    }

    public void setAuthodId(Integer authodId) {
        this.authodId = authodId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStauts() {
        return stauts;
    }

    public void setStauts(String stauts) {
        this.stauts = stauts;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCatagories() {
        return catagories;
    }

    public void setCatagories(String catagories) {
        this.catagories = catagories;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public Boolean getAllowPing() {
        return allowPing;
    }

    public void setAllowPing(Boolean allowPing) {
        this.allowPing = allowPing;
    }

    public Boolean getAllowFeed() {
        return allowFeed;
    }

    public void setAllowFeed(Boolean allowFeed) {
        this.allowFeed = allowFeed;
    }

    @Override
    public String toString() {
        return "ContentVo{" +
                "cid=" + cid +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", content='" + content + '\'' +
                ", authodId=" + authodId +
                ", type='" + type + '\'' +
                ", stauts='" + stauts + '\'' +
                ", tags='" + tags + '\'' +
                ", catagories='" + catagories + '\'' +
                ", hits=" + hits +
                ", commentsNum=" + commentsNum +
                ", allowComment=" + allowComment +
                ", allowPing=" + allowPing +
                ", allowFeed=" + allowFeed +
                '}';
    }
}
