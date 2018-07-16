package top.arieslee.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.arieslee.myblog.dao.ContentVoDao;
import top.arieslee.myblog.dto.Types;
import top.arieslee.myblog.exception.TipException;
import top.arieslee.myblog.modal.VO.ContentVo;
import top.arieslee.myblog.modal.VO.ContentVoExample;
import top.arieslee.myblog.service.ContentService;
import top.arieslee.myblog.utils.Tools;

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

    //注入dao层实例对象
    @Autowired
    private ContentVoDao contentVoDao;

    @Override
    //获取分页列表
    public PageInfo<ContentVo> getContent(Integer p, Integer limit) {
        LOGGER.debug("Enter the getContents(Integer p,Integer limit) method");
        //创建查询模板
        ContentVoExample example=new ContentVoExample();
        //排序方式
        example.setOrderByClause("created desc");
        //指定查询标准
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEauqlTo(Types.PUBLISH.getType());
        //执行分页插件（三部曲）：设定分页条件->获取分页内容->执行分页操作
        PageHelper.startPage(p,limit);
        List<ContentVo> contentVos=contentVoDao.selectByExampleWithBLOBs(example);
        PageInfo<ContentVo> pageInfo=new PageInfo<>(contentVos);
        LOGGER.debug("Exit the getContents(Integer p,Integer limit) method");
        return pageInfo;
    }

    @Override
    //按主键查询content
    public ContentVo getContent(String cid){
        LOGGER.debug("Enter the getContent(String cid) method");
        if(StringUtils.isNotBlank(cid)){
            //两种情况：按照cid查找和按照slug查找
            if(Tools.isNum(cid)){
                ContentVo contentVo = contentVoDao.selectByPrimaryKey(Integer.valueOf(cid));
                if(contentVo!=null){
                    contentVo.setHits(contentVo.getHits()+1);
                    contentVoDao.updateByPrimaryKey(contentVo);
                }
                LOGGER.debug("Enter the getContent(String cid) method");
                return contentVo;
            }else{
                //创建查询模板
                ContentVoExample example=new ContentVoExample();
                example.createCriteria().andSlugEqualTo(cid);
                List<ContentVo> contentVoList=contentVoDao.selectByExampleWithBLOBs(example);
                if(contentVoList.size()!=1){
                    //slug冲突
                    throw new TipException("The Slug you selected is more than 1");
                }
                LOGGER.debug("Enter the getContent(String cid) method");
                return contentVoList.get(0);
            }
        }
        LOGGER.debug("exit the getContent(String cid) method");
        return null;
    }
}
