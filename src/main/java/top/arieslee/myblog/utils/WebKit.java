package top.arieslee.myblog.utils;

import org.apache.commons.lang3.StringUtils;
import top.arieslee.myblog.constant.WebConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName WebKit
 * @Description 封装web相关的工具类
 * @Author Aries
 * @Date 2018/8/1 15:11
 * @Version 1.0
 **/
public class WebKit {

    /**
     * @return void
     * @Description 设置cookie值
     * @Param [response, value]
     **/
    public static void setCookie(String name, String value, int maxAge, HttpServletResponse response) {
        setCookie(name, value, maxAge, "", "", false, response);
    }
    public static void setCookie(String name, String value, int maxAge, String path, String domain, boolean isSSL, HttpServletResponse response) {
        try {
            //AES加密
            value = Tools.enAES(value, WebConstant.AES_SALT);
            System.out.println(Tools.deAES("zHU574bYI1Q3DLtjitDfwA==",WebConstant.AES_SALT));
            Cookie cookie = new Cookie(name, value);
            if (StringUtils.isNotBlank(path)) {
                cookie.setPath(path);
            }
            if (StringUtils.isNotBlank(domain)) {
                cookie.setDomain(domain);
            }
            cookie.setMaxAge(maxAge);
            cookie.setSecure(isSSL);
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
