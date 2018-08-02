package top.arieslee.myblog.controller.admin;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.arieslee.myblog.constant.WebConstant;
import top.arieslee.myblog.dto.ResponseDto;
import top.arieslee.myblog.exception.TipException;
import top.arieslee.myblog.modal.VO.UserVo;
import top.arieslee.myblog.service.IUserService;
import top.arieslee.myblog.utils.MapCache;
import top.arieslee.myblog.utils.WebKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName AuthController
 * @Description 管理员登录控制器
 * @Author Aries
 * @Date 2018/8/1 10:37
 * @Version 1.0
 **/
@Controller("AuthController")
@RequestMapping("/admin")
public class AuthController {

    //日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    //获取缓存对象
    private MapCache cachePool = MapCache.getCachePool();

    //业务层对象
    @Autowired
    private IUserService userService;

    /**
     * @return top.arieslee.myblog.dto.ResponseDto
     * @Description 登录控制
     * @Param [username, password, remember_me, request, response]
     **/
    @RequestMapping("/login")
    @ResponseBody
    public ResponseDto login(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam(required = false) String remember_me,
                             HttpServletRequest request, HttpServletResponse response) {
        //获取登录错误次数
        Integer errorCount = cachePool.get("login_error_count");
        if(errorCount!=null&&errorCount>=3){
            return ResponseDto.fail("您输入密码已经错误超过3次，请做40个仰卧起坐后再次尝试");
        }
        try {
            //用户名密码验证
            UserVo userVo = userService.login(username, password);
            request.getSession().setAttribute("user", userVo);
            //判断用户是否勾选了记住账户
            if (StringUtils.isNotBlank(remember_me)) {
                WebKit.setCookie(WebConstant.LOGI_SESSION_KEY, userVo.getUid().toString(), 60 * 30, response);
            }
            //日志记录 TODO

        } catch (Exception e) {
            errorCount = errorCount == null ? 1 : errorCount + 1;
            //记录登录错误次数
            cachePool.set("login_error_count",errorCount,60L);
            String msg = "登录失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return ResponseDto.fail(msg);
        }
        return ResponseDto.ok();
    }
}
