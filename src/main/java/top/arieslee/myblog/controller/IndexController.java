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
import top.arieslee.myblog.service.ICommentService;
import top.arieslee.myblog.service.IContentService;
import top.arieslee.myblog.service.IMetaService;
import top.arieslee.myblog.service.ISiteService;
import top.arieslee.myblog.utils.IPKit;
import top.arieslee.myblog.utils.PatternKit;
import top.arieslee.myblog.utils.Tools;

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
        //页面跳转
        return super.rend("page");
    }

    /**
     * @Description : 配置评论组件
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
     * @Description 评论操作
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
            setCookie("comment_user_name", commentVo.getAuthor(), 1 * 24 * 60 * 60, response);
            setCookie("comment_user_mail", commentVo.getMail(), 1 * 24 * 60 * 60, response);
            setCookie("comment_user_url", commentVo.getUrl(), 1 * 24 * 60 * 60, response);
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
    public String categories(HttpServletRequest request, @PathVariable("keyword") String keyword, @RequestParam(value = "limit", defaultValue = "1") int limit) {
        return this.categories(request, keyword, 1, limit);
    }

    @GetMapping("category/{keyword}/{page}")
    public String categories(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") int page, @RequestParam(value = "limit", defaultValue = "1") int limit) {
        page = page <= 0 || page > WebConstant.MAX_PAGE ? 1 : page;
        MetaDto metaDto=metaService.getMetaCount(Types.CATEGORY.getType(),keyword);
        //没有找到关键字分类信息，返回404页面
        if(metaDto==null){
            return super.page404();
        }

        //查找分类下的归档页
        PageInfo<ContentVo> contentsPaginator=contentService.getArticles(metaDto.getMid(),page,limit);

        //设置页面参数
        request.setAttribute("articles",contentsPaginator);
        request.setAttribute("type","分类");
        request.setAttribute("meta",metaDto);

        return super.rend("page_category");
    }

    /**
     * @Description 归档页
     * @Param [request]
     * @return java.lang.String
     **/
    @GetMapping("archive")
    public String archives(HttpServletRequest request){
        List<ArchiveDto> achives=iSiteService.getArchives();
        request.setAttribute("achives",achives);
        return super.rend("archives");
    }

    /**
     * @return void
     * @Description 为用户添加cookie值
     * @Param [key：键, value：值, maxAge：有效期, response：响应对象]
     **/
    public void setCookie(String key, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);//表示可用于http和https传回cookie
        response.addCookie(cookie);
    }
}
