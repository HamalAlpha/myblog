package top.arieslee.myblog.utils;

import org.apache.commons.lang3.StringUtils;
import top.arieslee.myblog.constant.WebConstant;
import top.arieslee.myblog.modal.VO.UserVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    /**
     * @param name    cookie名
     * @param request
     * @return java.lang.String
     * @Description 获取指定的cookie
     **/
    public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * @param name     缓存名
     * @param response 缓存值
     * @return void
     * @Description 清除cookie缓存
     **/
    public static void clearCookie(String name, HttpServletResponse response) {
        //构造同名cookie进行覆盖
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * @param request
     * @return top.arieslee.myblog.modal.VO.UserVo
     * @Description 从session中获取用户
     **/
    public static UserVo getUser(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                return (UserVo) session.getAttribute(WebConstant.LOGIN_SESSION_KEY);
            }
        }
        return null;
    }

    /**
     * @param name
     * @param request
     * @return java.lang.Integer
     * @Description 从cookie中获取解码后的uid
     **/
    public static Integer getUid(String name, HttpServletRequest request) {
        Cookie cookie = getCookie(name, request);
        if (cookie != null && cookie.getValue() != null) {
            //解密AES
            try {
                String uid = Tools.deAES(cookie.getValue(), WebConstant.AES_SALT);
                //uid不为空且为有效数字
                return StringUtils.isNotBlank(uid) && PatternKit.isNum(uid) ? Integer.valueOf(uid) : null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
