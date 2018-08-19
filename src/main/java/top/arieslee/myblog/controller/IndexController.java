package top.arieslee.myblog.controller;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.arieslee.myblog.constant.ErrorMsg;
import top.arieslee.myblog.constant.Types;
import top.arieslee.myblog.constant.WebConstant;
import top.arieslee.myblog.dto.ArchiveDto;
import top.arieslee.myblog.dto.MetaDto;
import top.arieslee.myblog.dto.ResponseDto;
import top.arieslee.myblog.exception.TipException;
import top.arieslee.myblog.dto.CommentDto;
import top.arieslee.myblog.modal.VO.CommentVo;
import top.arieslee.myblog.modal.VO.ContentVo;
import top.arieslee.myblog.modal.VO.MetaVo;
import top.arieslee.myblog.service.ICommentService;
import top.arieslee.myblog.service.IContentService;
import top.arieslee.myblog.service.IMetaService;
import top.arieslee.myblog.service.ISiteService;
import top.arieslee.myblog.utils.IPKit;
import top.arieslee.myblog.utils.PatternKit;
import top.arieslee.myblog.utils.Tools;
import top.arieslee.myblog.utils.WebKit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName IndexController
 * @Description 首页controller
 * @Author Aries
 * @Date 2018/7/6 15:38
 * @Version 1.0
 **/
@Controller
public class IndexController extends BaseController {
    //日志对象
    private final static Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    //注入相关业务层接口
    @Autowired
    private IContentService contentService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IMetaService metaService;

    @Autowired
    private ISiteService iSiteService;

    /**
     * @Author: Aries
     * @Description : 初始主页访问
     * @Date : 16:20 2018/7/10
     * @Param [request, limit:每页文章数量]
     **/
    @GetMapping(value = {"/", "index"})
    public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "1") int limit) {
        //调用文章分页接口处理
        return this.index(request, 1, limit);
    }

    /**
     * @Author: Aries
     * @Description :文章分页控制
     * @Date : 16:57 2018/7/10
     * @Param [request, p:当前页码, limit:每页文章数量]
     **/
    @GetMapping(value = {"page/{p}", "page/{p}.html"})
    public String index(HttpServletRequest request, @PathVariable("p") int p, @RequestParam(value = "limit", defaultValue = "1") int limit) {
        //判断页码是否合法,不合法置为1
        p = p < 1 || p > WebConstant.MAX_PAGE ? 1 : p;
        //调用业务层接口，获取mybatis分页插件执行结果
        PageInfo articles = contentService.getContent(p, limit);
        request.setAttribute("articles", articles);
        super.title(request, "第" + p + "页");
        return super.rend("index");
    }

    /**
     * @return java.lang.String
     * @Description : 根据文章id或者文章slug获取文章
     * @Date : 17:05 2018/7/13
     * @Param [request, cid]
     **/
    @GetMapping(value = "article/{cid}")
    public String getArticle(HttpServletRequest request, @PathVariable("cid") String cid) {
        //调用业务层接口处理
        ContentVo contentVo = contentService.getContent(cid);
        //文章不存在或者文章保存为草稿，转发到404页面
        if (contentVo == null || contentVo.getStatus().equals("draft")) {
            return super.page404();
        }
        //设置文章属性
        request.setAttribute("article", contentVo);
        //设置评论组件
        commentSet(request, contentVo);
        //定时更新点击率
        updateHits(Integer.valueOf(contentVo.getCid()),contentVo.getHits());
        //页面跳转
        return super.rend("page");
    }

    /**
     * @Description : 显示评论
     **/
    public void commentSet(HttpServletRequest request, ContentVo contentVo) {
        //文章允许评论
        if (contentVo.getAllowComment()) {
            //获取到当前页
            String cp = request.getParameter("cp");
            if (StringUtils.isBlank(cp)) {
                cp = "1";
            }
            //返回当前页数
            request.setAttribute("cp", cp);
            //执行评论分页查询
            PageInfo<CommentDto> pageInfo = commentService.getComment(Integer.valueOf(contentVo.getCid()), Integer.valueOf(cp), 6);
            request.setAttribute("comments", pageInfo);
        }
    }

    /**
     * @Description 评论提交
     **/
    @PostMapping("comment")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public ResponseDto publishComment(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam Integer cid, @RequestParam Integer coid,
                                      @RequestParam String author, @RequestParam String mail,
                                      @RequestParam String url, @RequestParam String text, @RequestParam String _csrf_token) {
        //验证请求中Referer和_csrf_token是否存在，此处可以利用正则表达式拦截外链
        String str = request.getHeader("Referer");
        if (StringUtils.isBlank(str) || StringUtils.isBlank(_csrf_token)) {
            return ResponseDto.fail(ErrorMsg.BAD_REQUEST);
        }

        //验证token是否正确
        String token = cachePool.get(Types.CSRF_TOKEN.getType(), _csrf_token);
        if (StringUtils.isBlank(token)) {
            return ResponseDto.fail(ErrorMsg.BAD_REQUEST);
        }

        //验证表单信息
        if (StringUtils.isBlank(text)) {
            return ResponseDto.fail("评论不能为空的哦~~");
        }
        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return ResponseDto.fail("国外名字也没你那么长");
        }
        if (StringUtils.isNotBlank(mail) && !PatternKit.isEmail(mail)) {
            return ResponseDto.fail("邮件是发到火星么？");
        }
        if (StringUtils.isNotBlank(url) && !PatternKit.isURL(url)) {
            return ResponseDto.fail("你应该是个伏地魔");
        }
        if (text.length() > 2000) {
            return ResponseDto.fail("评论字数不超过2000，好么？");
        }

        //获取IP地址
        String ip = IPKit.getIPAddrByRequest(request);
        //从缓存池中获取评论缓存
        Integer count = cachePool.get(Types.COMMENT_FREQUENCY.getType(), ip, cid.toString());
        if (count != null && count > 0) {
            return ResponseDto.fail("加...加藤鹰之手？");
        }

        //过滤敏感字符，防止XSS注入攻击
        author = Tools.cleanXSS(author);
        text = Tools.cleanXSS(text);

        //过滤emoji字符
        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        //构造评论实例
        CommentVo commentVo = new CommentVo();
        commentVo.setCid(cid);
        commentVo.setParent(coid);//父评论
        commentVo.setAuthor(author);
        commentVo.setMail(mail);
        commentVo.setIp(ip);
        commentVo.setUrl(url);
        commentVo.setContent(text);

        try {
            commentService.insertComment(commentVo);
            //添加cookie
            WebKit.setCookie("comment_user_name", commentVo.getAuthor(), 1 * 24 * 60 * 60, response);
            WebKit.setCookie("comment_user_mail", commentVo.getMail(), 1 * 24 * 60 * 60, response);
            WebKit.setCookie("comment_user_url", commentVo.getUrl(), 1 * 24 * 60 * 60, response);
            //设置评论频率缓存
            cachePool.set(Types.COMMENT_FREQUENCY.getType(), 1, 60, ip, cid.toString());
            //返回成功结果
            return ResponseDto.ok();
        } catch (Exception e) {
            String msg = "评论失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return ResponseDto.fail(msg);
        }
    }

    /**
     * @return java.lang.String
     * @Description 分类页请求
     **/
    @GetMapping("category/{keyword}")
    public String categories(HttpServletRequest request, @PathVariable("keyword") String keyword, @RequestParam(value = "limit", defaultValue = "6") int limit) {
        return this.categories(request, keyword, 1, limit);
    }

    @GetMapping("category/{keyword}/{page}")//前台分页尚未完成
    public String categories(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") int page, @RequestParam(value = "limit", defaultValue = "6") int limit) {
        MetaDto metaDto = metaService.getMetaCount(Types.CATEGORY.getType(), keyword);
        //没有找到关键字分类信息，返回404页面
        if (metaDto == null) {
            return super.page404();
        }

        //查找分类下的归档页
        page = page <= 0 || page > WebConstant.MAX_PAGE ? 1 : page;
        PageInfo<ContentVo> contentsPaginator = contentService.getArticles(metaDto.getMid(), page, limit);

        //设置页面参数
        request.setAttribute("articles", contentsPaginator);
        request.setAttribute("type", "分类");
        request.setAttribute("meta", metaDto);

        return super.rend("page_category");
    }

    /**
     * @return java.lang.String
     * @Description 归档页
     * @Param [request]
     **/
    @GetMapping("archives")
    public String archives(HttpServletRequest request) {
        List<ArchiveDto> archives = iSiteService.getArchives();
        request.setAttribute("archives", archives);
        return super.rend("archives");
    }

    /**
     * @return java.lang.String
     * @Description 友链页
     * @Param [request]
     **/
    @GetMapping("links")
    public String link(HttpServletRequest request) {
        List<MetaVo> links = metaService.getLinks(Types.LINK.getType());
        request.setAttribute("links", links);
        return super.rend("link");
    }

    /**
     * @return java.lang.String
     * @Description 自定义页面
     * @Param
     **/
    @GetMapping("/{pagename}")
    public String custom(HttpServletRequest request, @PathVariable("pagename") String pagename) {
        ContentVo contentVo;
        //页面为空或者有异常抛出，返回到404页面
        try {
            contentVo = contentService.getContent(pagename);
            if (contentVo == null) {
                return super.page404();
            }
        } catch (Exception e) {
            return super.page404();
        }
        if (contentVo.getAllowComment()) {
            //获取评论页
            String commentPage = request.getParameter("co");
            if (StringUtils.isBlank(commentPage)) {
                commentPage = "1";
            }
            PageInfo<CommentDto> commentDtoPageInfo = commentService.getComment(contentVo.getCid(), Integer.valueOf(commentPage), 6);
            request.setAttribute("comments", commentDtoPageInfo);
        }
        request.setAttribute("article", contentVo);
        return super.rend("custom");
    }


    /**
     * @return void
     * @Description 更新文章点击数
     * @Param [cid 文章id, chits 原文章点击数]
     **/
    @Transactional(rollbackFor = TipException.class)
    public void updateHits(Integer cid, Integer chits) {
        //从缓存中获取点击数
        Integer hits = cachePool.get(Types.ARTICLE.getType(), String.valueOf(cid));
        hits = hits == null ? 1 : hits + 1;
        chits = chits == null ? 0 : chits;
        //是否达到需要更新数据库的点击量
        if (hits >= WebConstant.CRITICAL_HIT) {
            ContentVo temp=new ContentVo();
            temp.setCid(cid);
            temp.setHits(chits+hits);
            contentService.updateContentByCid(temp);
            //更新缓存
            cachePool.set(Types.ARTICLE.getType(), hits, String.valueOf(cid));
        } else {
            cachePool.set(Types.ARTICLE.getType(), hits, String.valueOf(cid));
        }
    }
}
