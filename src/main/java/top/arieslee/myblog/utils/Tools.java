package top.arieslee.myblog.utils;

/**
 * @ClassName Tools
 * @Description 工具类
 * @Author Aries
 * @Date 2018/7/16 10:56
 * @Version 1.0
 **/
public class Tools {

    /**
     * @Description : 判断字符串是否包含有效数字
     **/
    public static boolean isNum(String str) {
        if (str != null && str.trim().length() != 0 && str.matches("\\d*")) {
            return true;
        }
        return false;
    }
}
