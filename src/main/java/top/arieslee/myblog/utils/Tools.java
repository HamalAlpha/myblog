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
     * 替换HTML脚本，防止XSS注入
     *
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "^-^");
        return value;
    }
}
