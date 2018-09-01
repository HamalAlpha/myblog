package top.arieslee.myblog.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.arieslee.myblog.constant.Types;
import top.arieslee.myblog.constant.WebConstant;
import top.arieslee.myblog.dao.UserVoDao;
import top.arieslee.myblog.modal.VO.UserVo;
import top.arieslee.myblog.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName BaseInterceptor
 * @Description 自定义拦截器
 * @Author Aries
 * @Date 2018/7/12 16:29
 * @Version 1.0
 **/
@Component
public class BaseInterceptor implements HandlerInterceptor {

    @Autowired
    private Commons commons;

    @Autowired
    private UserVoDao userVoDao;

    private MapCache mapCache = MapCache.getCachePool();

    @Override
    //该方法将在请求处理之前进行调用，只有该方法返回true，才会继续执行后续的Interceptor和Controller，
    //当返回值为true时就会继续调用下一个Interceptor的preHandle方法，
    //如果已经是最后一个Interceptor的时候就会是调用当前请求的Controller方法。
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String uri = request.getRequestURI();

        //上下文路径
        String contextPath=request.getContextPath();

        //尝试从session中获取管理员用户
        UserVo user = WebKit.getUser(request);

        //尝试从cookie中获取uid，并获取user
        if (user == null) {
            //cookie可以伪造，待改进
            Integer uid = WebKit.getUid(WebConstant.USER_IN_COOKIE, request);
            if (uid != null) {
                user = userVoDao.selectByPrimaryKey(uid);
                //设置session
                request.getSession().setAttribute(WebConstant.LOGIN_SESSION_KEY, user);
            }
        }

        //拦截未登录的管理员请求
        if (uri.startsWith(contextPath+"/admin") && !uri.startsWith(contextPath+"/admin/login") && user == null) {
            response.sendRedirect(contextPath + "/admin/login");
            return false;
        }

//        //对已登录的login请求，直接跳转到首页
//        if (user != null && uri.startsWith(contextPath+"/admin/login")) {
//            response.sendRedirect(contextPath + "/admin/index");
//            return false;
//        }

        //设置get请求csrf_token
        if (request.getMethod().equals("GET")) {
            String csrf_token = UUID.UUID64();
            //存入缓冲池，默认保存30分钟
            mapCache.set(Types.CSRF_TOKEN.getType(), uri, 60 * 30, csrf_token);
            request.setAttribute("csrf_token", csrf_token);
        }
        return true;
    }

    @Override
    //该方法将在请求处理之后，DispatcherServlet进行视图返回渲染之前进行调用，
    //可以在这个方法中对Controller 处理之后的ModelAndView 对象进行操作。
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //将公共函数对象传到页面
        httpServletRequest.setAttribute("commons", commons);
    }

    @Override
    //该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行，
    //该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。用于进行资源清理。
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
