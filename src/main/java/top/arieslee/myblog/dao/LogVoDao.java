package top.arieslee.myblog.dao;

import org.springframework.stereotype.Repository;
import top.arieslee.myblog.modal.VO.LogVo;

/**
 * @ClassName LogVoDao
 * @Description 日志表数据访问层
 * @Author Aries
 * @Date 2018/8/3 10:21
 * @Version 1.0
 **/
@Repository
public interface LogVoDao {

    /**
     * @Description 插入一条日志数据
     **/
    int insert(LogVo logVo);
}
