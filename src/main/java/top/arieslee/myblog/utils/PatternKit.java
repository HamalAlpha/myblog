package top.arieslee.myblog.utils;

import java.util.regex.Pattern;

/**
 * @ClassName PatternKit
 * @Description 正则表达式工具
 * @Author Aries
 * @Date 2018/7/19 16:35
 * @Version 1.0
 **/
public class PatternKit {


    /**
     * @Description 验证邮箱
     * @Param [email：邮箱]
     * @return boolean
     **/
    public static boolean isEmail(String email){
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证URL地址
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }
}
