package top.arieslee.myblog.service;

import top.arieslee.myblog.modal.VO.UserVo;

/**
 * @ClassName IUserService
 * @Description 用户登录相关业务逻辑
 * @Author Aries
 * @Date 2018/8/1 11:12
 * @Version 1.0
 **/
public interface IUserService {

    /**
     * @return top.arieslee.myblog.modal.VO.UserVo
     * @Description 管理员登录业务
     * @Param [username 账户, password 密码]
     **/
    UserVo login(String username, String password);
}
