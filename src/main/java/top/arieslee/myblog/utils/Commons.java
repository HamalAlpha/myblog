package top.arieslee.myblog.utils;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.arieslee.myblog.constant.WebConstant;
import top.arieslee.myblog.modal.VO.ContentVo;

import javax.tools.Tool;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
     * @Description : 站点链接
     **/
    public static String site_url() {
        return site_url("");
    }

    /**
     * @Description : 站点链接
     **/
    public static String site_url(String sub) {
        return site_option("site_url") + sub;
    }

    /**
     * @Description : 文章链接
     **/
    public static String permlink(ContentVo contentVo) {
        return permlink(contentVo.getCid(), contentVo.getSlug());
    }

    /**
     * @Description : 文章链接完整地址
     **/
    public static String permlink(Integer cid, String slug) {
        String str = "/article/" + (StringUtils.isNotBlank(slug) ? slug : cid.toString());
        return site_url(str);
    }

    /**
     * @Description 跳转到管理员登录页
     **/
    public static String site_login() {
        return "/admin/login";
    }

    /**
     * @Description : 显示首页文章背景缩略图
     **/
    public static String show_thumbnail(ContentVo contentVo) {
        int size = contentVo.getCid() % 20;
        size = size == 0 ? 1 : size;
        return "/user/images/rand/" + size + ".jpg";
    }

    //图标库
    private static final String[] ICON_LIBRARY = {"bg-ico-book", "bg-ico-game", "bg-ico-note", "bg-ico-chat", "bg-ico-code", "bg-ico-image", "bg-ico-web", "bg-ico-link", "bg-ico-design", "bg-ico-lock"};

    /**
     * @Description 获取一张小图标
     **/
    public static String show_icon(Integer cid) {
        return ICON_LIBRARY[cid % ICON_LIBRARY.length];
    }

    /**
     * @Description 显示分类，可能归属多个类别
     **/
    public static String show_categories(String categories) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(categories)) {
            //分割多个类
            String str[] = categories.split(",");
            StringBuilder sb = new StringBuilder();
            for (String s : str) {
                sb.append("<a href=\"/category/" + URLEncoder.encode(s, "UTF-8") + "\">" + s + "</a>");
            }
            return sb.toString();
        }
        return show_categories("默认分类");
    }


    /**
     * @Description 时期格式化
     **/
    public static String fmtdate(Integer created) {
        return fmtdate(created, "yyyy-MM-dd");
    }

    public static String fmtdate(Integer created, String ftm) {
        if (created != null && StringUtils.isNotBlank(ftm)) {
            return DateKit.formatDateByUnixTime(created, ftm);
        }
        return "";
    }

    /**
     * @Description 返回gravatar头像地址
     **/
    public static String gravatar(String email) {
        String avatarUrl = "https://secure.gravatar.com/avatar";
        if (StringUtils.isBlank(email)) {
            return avatarUrl;
        }
        String hash = Tools.MD5encode(email.trim().toLowerCase());
        return avatarUrl + "/" + hash;
    }

    /**
     * @Description 将类似 :smile: 这样的字符串转为emoji表情
     **/
    public static String emoji(String value) {
        return EmojiParser.parseToUnicode(value);
    }

    /**
     * @return java.lang.String
     * @Description 将markdown文章内容解析为html
     * @Param [content 文章内容]
     **/
    public static String markdownParse(String content) {
        if (StringUtils.isNotBlank(content)) {
            //将注释去除
            content = content.replace("<!--more-->", "\r\n");
            //调用解析工具
            return Tools.mdToHtml(content);
        }
        return "";
    }

    /**
     * 判断分页中是否有数据
     *
     * @param paginator
     * @return
     */
    public static boolean is_empty(PageInfo paginator) {
        return paginator == null || (paginator.getList() == null) || (paginator.getList().size() == 0);
    }

    /**
     * @Description 获取随机数
     **/
    public static String random(int max, String str) {
        return Tools.getRandom(max, 1) + str;
    }
}
