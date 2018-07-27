package top.arieslee.myblog.controller;

import top.arieslee.myblog.utils.MapCache;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName BaseController
 * @Description 全局控制器，封装基本页面控制层接口
 * @Author Aries
 * @Date 2018/7/10 16:02
 * @Version 1.0
 **/
public class BaseController {

    //主题属性
    public static String THEME = "themes/default";

    //全局缓存池
    public static MapCache cachePool=MapCache.getCachePool();

    /**
     * @Description : 页面跳转控制
     **/
    protected String rend(String viewName) {
        return THEME + "/" + viewName;
    }

    /**
     * @Description : 分页标题控制
     **/
    protected void title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
    }

    /**
     * @Description : 404页面返回
     **/
    protected String page404() {
        return "common/404_page";
    }
}
