package top.arieslee.myblog.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName IPKit
 * @Description 封装IP工具方法
 * @Author Aries
 * @Date 2018/7/19 17:01
 * @Version 1.0
 **/
public class IPKit {

    /**
     * @Description 从request中获取IP地址，考虑是否采用代理服务器，以及通过不同代理服务器获取IP地址
     * 所需要的请求头信息。
     * @Param [request：用户请求]
     * @return java.lang.String 返回IP地址
     **/
    public static String getIPAddrByRequest(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
