package top.arieslee.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.arieslee.myblog.dto.Types;
import top.arieslee.myblog.modal.VO.ContentVo;
import top.arieslee.myblog.modal.VO.ContentVoExample;
import top.arieslee.myblog.service.ContentService;

import java.util.List;

/**
 * @ClassName ContentServiceImpl
 * @Description Content业务层
 * @Author Aries
 * @Date 2018/7/10 15:52
 * @Version 1.0
 **/
@Service
public class ContentServiceImpl implements ContentService {
    //log日志对象
    private static final Logger LOGGER=LoggerFactory.getLogger(ContentServiceImpl.class);

    @Override
    public PageInfo<ContentVo> getContent(Integer p, Integer limit) {
        //记录日志
        LOGGER.debug("Enter getContents method");
        //创建分页模板
        ContentVoExample example=new ContentVoExample();
        //排序方式
        example.setOrderByClause("created desc");
        //指定查询标准
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEauqlTo(Types.PUBLISH.getType());
        //执行分页插件（三部曲）：设定分页条件->获取分页内容->执行分页操作
        PageHelper.startPage(p,limit);
        List<ContentVo> contentVos=null;// TODO
        PageInfo<ContentVo> pageInfo=new PageInfo<>(contentVos);
        //记录日志
        LOGGER.debug("Exit getContents method");
        return pageInfo;
    }
}
