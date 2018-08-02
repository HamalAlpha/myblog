package top.arieslee.myblog.dao;

import org.springframework.stereotype.Repository;
import top.arieslee.myblog.modal.VO.UserVo;
import top.arieslee.myblog.modal.VO.UserVoExample;

import java.util.List;

/**
 * @ClassName UserVoDao
 * @Description 管理员数据访问层
 * @Author Aries
 * @Date 2018/8/1 11:18
 * @Version 1.0
 **/
@Repository
public interface UserVoDao {
    /**
     * @return long
     * @Description
     * @Param [example]
     **/
    long countByExample(UserVoExample example);

    /**
     * @return java.util.List<top.arieslee.myblog.modal.VO.UserVo>
     * @Description 查询用户登录信息
     * @Param [example]
     **/
    List<UserVo> selectByExample(UserVoExample example);
}
