package top.arieslee.myblog.modal.BO;

import top.arieslee.myblog.modal.VO.CommentVo;

import java.util.List;

/**
 * @ClassName CommentBo
 * @Description 评论业务对象，包括父子评论
 * @Author Aries
 * @Date 2018/7/17 15:44
 * @Version 1.0
 **/
public class CommentBo extends CommentVo{
    //层级
    private int levels;
    //子评论集
    private List<CommentVo> childCommentVos;

    public CommentBo(CommentVo comments){
        setAuthor(comments.getAuthor());
        setMail(comments.getMail());
        setCoid(comments.getCoid());
        setAuthorId(comments.getAuthorId());
        setUrl(comments.getUrl());
        setCreated(comments.getCreated());
        setAgent(comments.getAgent());
        setIp(comments.getIp());
        setContent(comments.getContent());
        setOwnerId(comments.getOwnerId());
        setCid(comments.getCid());
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public List<CommentVo> getChildCommentVos() {
        return childCommentVos;
    }

    public void setChildCommentVos(List<CommentVo> childCommentVos) {
        this.childCommentVos = childCommentVos;
    }
}
