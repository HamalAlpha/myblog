/**
 * FileName: ArticleController
 * Author:   Aries
 * Date:     2018/8/26 18:10
 * Description: 文章编辑页控制器
 */
package top.arieslee.myblog.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.arieslee.myblog.constant.Types;
import top.arieslee.myblog.controller.BaseController;
import top.arieslee.myblog.dto.ResponseDto;
import top.arieslee.myblog.exception.TipException;
import top.arieslee.myblog.modal.VO.ContentVo;
import top.arieslee.myblog.modal.VO.MetaVo;
import top.arieslee.myblog.modal.VO.UserVo;
import top.arieslee.myblog.service.IContentService;
import top.arieslee.myblog.service.IMetaService;
import top.arieslee.myblog.utils.WebKit;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

    private final static Logger LOGGER=LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    public IMetaService metaService;

    @Autowired
    public IContentService contentService;

    /**
     * @Description 创建新文章
     **/
    @GetMapping("/publish")
    public String newArticle(HttpServletRequest request){
        List<MetaVo> metaVos=metaService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories",metaVos);
        return "admin/article_edit";
    }

    @PostMapping("/publish")
    @ResponseBody
    @Transactional(rollbackFor=TipException.class)
    public ResponseDto saveArticle(ContentVo content,HttpServletRequest request){
        UserVo user=WebKit.getUser(request);
        content.setAuthorId(user.getUid());
        content.setType(Types.ARTICLE.getType());
        try {
            contentService.publish(content);
        }catch (Exception e){
            String msg="发表文章失败";
            if(e instanceof TipException){
                msg=e.getMessage();
            }else{
                LOGGER.error(msg,e);
            }
            return ResponseDto.fail(msg);
        }
        return ResponseDto.ok();
    }

}
