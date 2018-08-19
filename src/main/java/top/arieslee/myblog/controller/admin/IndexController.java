package top.arieslee.myblog.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.arieslee.myblog.controller.BaseController;
import top.arieslee.myblog.dto.StatisticsDto;
import top.arieslee.myblog.exception.TipException;
import top.arieslee.myblog.modal.VO.CommentVo;
import top.arieslee.myblog.modal.VO.ContentVo;
import top.arieslee.myblog.modal.VO.LogVo;
import top.arieslee.myblog.service.ILogService;
import top.arieslee.myblog.service.ISiteService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName IndexController
 * @Description 管理主页控制器
 * @Author Aries
 * @Date 2018/8/6 14:47
 * @Version 1.0
 **/
@Controller("adminIndexController")
@RequestMapping(value = "/admin")
@Transactional(rollbackFor = TipException.class)
public class IndexController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ISiteService siteService;

    @Autowired
    private ILogService logService;

    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        LOGGER.debug("Enter the index method");
        List<CommentVo> commentVos=siteService.recentComment(5);//近期评论
        List<ContentVo> contentVos=siteService.recentContent(5);//近期文章
        List<LogVo> logVos=logService.getLogs(1,5);//近期日志
        StatisticsDto statisticsDto=siteService.currentStatistics();//站点信息

        request.setAttribute("comments", commentVos);
        request.setAttribute("articles", contentVos);
        request.setAttribute("statistics", statisticsDto);
        request.setAttribute("logs", logVos);
        LOGGER.debug("Exist the index method");
        return "admin/index";
    }
}
