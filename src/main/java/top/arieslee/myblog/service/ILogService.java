package top.arieslee.myblog.service;

import top.arieslee.myblog.modal.VO.LogVo;

import java.util.List;

/**
 * @ClassName ILogService
 * @Description 日志业务逻辑
 * @Author Aries
 * @Date 2018/8/3 10:21
 * @Version 1.0
 **/
public interface ILogService {

    /**
     * @Description 插入新的日志记录
     **/
    void insertLog(LogVo logVo);

    /**
     * @param action 行为
     * @param data 日志数据
     * @param authorId 管理员id
     * @param ip 来源ip地址
     * @return void
     * @Description 插入新的日志记录
     **/
    void insertLog(String action, String data, int authorId, String ip);

    /**
     * @param page 页码
     * @param limit 每页限制
     * @return java.util.List<top.arieslee.myblog.modal.VO.LogVo>
     * @Description 分页获取日志记录
     **/
    List<LogVo> getLogs(int page,int limit);
}
