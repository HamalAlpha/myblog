package top.arieslee.myblog.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WebConstant
 * @Description 存放项目常量值
 * @Author Aries
 * @Date 2018/7/10 16:51
 * @Version 1.0
 **/
public class WebConstant {

    /**
     * @Description 管理员用户名cookie值
     **/
    public static final String USER_IN_COOKIE = "S_L_ID";

    /**
     * @Description session用户属性值
     **/
    public static final String LOGI_SESSION_KEY="login_user";

    public static final String AES_SALT="1a2b3c4d5e6f7g8h9i0j";

    /**
     * @Description :设置分页页码最大值
     **/
    public static final int MAX_PAGE = 100;

    /**
     * @Description : 保存前台页面配置信息
     **/
    public static final Map<String, String> initConfig = new HashMap<>();

    /**
     * @Description 临界点击数
     **/
    public static final int CRITICAL_HIT = 10;
}