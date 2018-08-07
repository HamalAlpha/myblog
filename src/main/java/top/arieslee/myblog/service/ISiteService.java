package top.arieslee.myblog.service;

import top.arieslee.myblog.dto.ArchiveDto;
import top.arieslee.myblog.dto.CommentDto;
import top.arieslee.myblog.dto.StatisticsDto;
import top.arieslee.myblog.modal.VO.CommentVo;
import top.arieslee.myblog.modal.VO.ContentVo;
import top.arieslee.myblog.modal.VO.LogVo;

import java.util.List;

/**
 * @ClassName ISiteService
 * @Description 站内业务逻辑
 * @Author Aries
 * @Date 2018/7/24 10:01
 * @Version 1.0
 **/
public interface ISiteService {

    /**
     * @Description 获取归档列表
     * @Param []
     * @return java.util.List<top.arieslee.myblog.dto.ArchiveDto>
     **/
    List<ArchiveDto> getArchives();

    /**
     * @param limit 获取多少条数据
     * @return java.util.List<top.arieslee.myblog.modal.VO.CommentVo>
     * @Description 获取近期评论
     **/
    List<CommentVo> recentComment(int limit);

    /**
     * @param limit
     * @return java.util.List<top.arieslee.myblog.modal.VO.ContentVo>
     * @Description 
     **/
    List<ContentVo> recentContent(int limit);

    /**
     * @Description 查看当前站点信息
     **/
    StatisticsDto currentStatistics();
}
