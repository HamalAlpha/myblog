package top.arieslee.myblog.service;

import com.github.pagehelper.PageInfo;
import top.arieslee.myblog.modal.BO.CommentBo;

/**
 * @ClassName ICommentService
 * @Description 评论接口规范
 * @Author Aries
 * @Date 2018/7/17 11:10
 * @Version 1.0
 **/
public interface ICommentService {

    /**
     * @Description : 获取分页评论列表
     * @Date : 15:59 2018/7/17
     * @Param 
     * @return 
     **/
    PageInfo<CommentBo> getComment(Integer cid,Integer pageNum,Integer limit);
}
