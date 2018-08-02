package top.arieslee.myblog.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.arieslee.myblog.dao.UserVoDao;
import top.arieslee.myblog.exception.TipException;
import top.arieslee.myblog.modal.VO.UserVo;
import top.arieslee.myblog.modal.VO.UserVoExample;
import top.arieslee.myblog.service.IUserService;
import top.arieslee.myblog.utils.Tools;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author Aries
 * @Date 2018/8/1 11:15
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements IUserService {

    //数据访问层接口
    @Autowired
    UserVoDao userVoDao;

    @Override
    public UserVo login(String username, String password) {
        //非空验证
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new TipException("用户名或密码不能为空");
        }
        UserVoExample example = new UserVoExample();
        UserVoExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        long count = userVoDao.countByExample(example);
        if (count < 1) {
            throw new TipException("用户名不存在");
        }
        //MD5处理密码,采用简单的拼接，提高破解难度
        password=Tools.MD5encode(username+password);
        criteria.andPasswordEqualTo(password);
        List<UserVo> userVoList=userVoDao.selectByExample(example);
        if(userVoList.size()!=1){
            throw new TipException("用户名或者密码错误");
        }
        return userVoList.get(0);
    }
}
