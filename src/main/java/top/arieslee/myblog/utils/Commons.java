package top.arieslee.myblog.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.arieslee.myblog.constant.WebConstant;
import top.arieslee.myblog.modal.VO.ContentVo;

/**
 * @ClassName Commons
 * @Description 前台页面公共函数
 * @Author Aries
 * @Date 2018/7/12 16:48
 * @Version 1.0
 **/
@Component
public class Commons {

    /**
     * @Description : 网站配置项
     * @Date : 9:30 2018/7/13
     * @Param key : 键
     **/
    public static String site_option(String key) {
        return site_option(key, "");
    }

    /**
     * @Description : 网站配置项
     * @Param defaultValue：默认值
     **/
    public static String site_option(String key, String defaultValue) {
        if (StringUtils.isBlank(key)) {
            return defaultValue;
        }
        //从配置map中取出值
        String value = WebConstant.initConfig.get(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        return defaultValue;
    }

    /**
     * @Description : 返回网站链接
     **/
    public static String site_url() {
        return site_url("");
    }

    /**
     * @Description : 返回完整网址
     **/
    public static String site_url(String sub) {
        return site_option("site_url") + sub;
    }

    /**
     * @Description : 获取文章链接
     **/
    public static String permlink(ContentVo contentVo) {
        return permlink(contentVo.getCid(), contentVo.getSlug());
    }

    /**
     * @Description : 文章链接拼接
     **/
    public static String permlink(Integer cid, String slug) {
        String str = "/ariticle/" + (StringUtils.isNotBlank(slug) ? slug : cid.toString());
        return site_url(str);
    }

    /**
     * @Description : 显示首页文章背景缩略图
     **/
    public static String show_thumbnail(ContentVo contentVo) {
        int size = contentVo.getCid() % 20;
        size = size == 0 ? 1 : size;
        return "user/img/rand/"+size+".jpg";
    }
}
