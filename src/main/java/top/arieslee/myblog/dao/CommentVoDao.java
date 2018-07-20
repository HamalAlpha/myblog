package top.arieslee.myblog.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import top.arieslee.myblog.modal.VO.CommentVo;
import top.arieslee.myblog.modal.VO.CommentVoExample;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CommentVoDao
 * @Description comment表访问接口
 * @Author Aries
 * @Date 2018/7/17 16:12
 * @Version 1.0
 **/
@Repository
public interface CommentVoDao {

    /**
     * @Description :依据查询模板获取评论列表
     * @Date : 16:38 2018/7/17
     * @Param example：查询模板
     * @return
     **/
    List<CommentVo> selectByExampleWithBLOBs(CommentVoExample example);

    /**
     * @Description 插入评论
     * @return void
     **/
    void insertComment(CommentVo commentVo);
}
