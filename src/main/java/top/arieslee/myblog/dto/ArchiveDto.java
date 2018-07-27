package top.arieslee.myblog.dto;

import top.arieslee.myblog.modal.VO.ContentVo;

import java.util.List;

/**
 * @ClassName ArchiveDto
 * @Description 归档页对象
 * @Author Aries
 * @Date 2018/7/24 9:52
 * @Version 1.0
 **/
public class ArchiveDto {

    //归档日期
    private String date;
    //文章数量
    private Integer count;
    //文章列表
    private List<ContentVo> articles;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ContentVo> getArticles() {
        return articles;
    }

    public void setArticles(List<ContentVo> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "ArchiveDto{" +
                "date='" + date + '\'' +
                ", count=" + count +
                ", articles=" + articles +
                '}';
    }
}
