package top.arieslee.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.arieslee.myblog.constant.Types;
import top.arieslee.myblog.constant.WebConstant;
import top.arieslee.myblog.dao.ContentVoDao;
import top.arieslee.myblog.dao.MetaVoDao;
import top.arieslee.myblog.exception.TipException;
import top.arieslee.myblog.modal.VO.ContentVo;
import top.arieslee.myblog.modal.VO.ContentVoExample;
import top.arieslee.myblog.service.IContentService;
import top.arieslee.myblog.utils.DateKit;
import top.arieslee.myblog.utils.PatternKit;

import java.util.List;

/**
 * @ClassName ContentServiceImpl
 * @Description Content业务层
 * @Author Aries
 * @Date 2018/7/10 15:52
 * @Version 1.0
 **/
@Service
public class ContentServiceImpl implements IContentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Autowired
    private ContentVoDao contentVoDao;

    @Autowired
    private MetaVoDao metaVoDao;


    @Override
    public void publish(ContentVo content) {
        if (content == null) {
            throw new TipException("文章对象不存在");
        }
        if (StringUtils.isBlank(content.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (StringUtils.isBlank(content.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        if (content.getTitle().length() > WebConstant.MAX_TITLE_LENGTH) {
            throw new TipException("文章标题太长了");
        }
        if (content.getContent().length() > WebConstant.MAX_CONTENT_LENGTH) {
            throw new TipException("文章内容太长了");
        }
        if (content.getAuthorId() == null) {
            throw new TipException("请先登录");
        }
        if (StringUtils.isNotBlank(content.getSlug())) {
            String slug = content.getSlug();
            if (slug.length() < 5) {
                throw new TipException("路径长度不小于5喔~");
            }
            if (!PatternKit.isPath(slug)) {
                throw new TipException("非法路径啦");
            }
            ContentVoExample example = new ContentVoExample();
            example.createCriteria().andSlugEqualTo(slug);
            if (contentVoDao.countByExample(example) > 0) {
                throw new TipException("该路径已经存在了");
            }
        } else {
            content.setSlug(null);
        }

        //填充content对象属性
        content.setCreated((long) DateKit.getCurrentUnixTime());
        content.setModified((long)DateKit.getCurrentUnixTime());
        content.setHits(0);
        content.setCommentsNum(0);
    }

    @Override
    //获取分页列表
    public PageInfo<ContentVo> getContent(Integer p, Integer limit) {
        LOGGER.debug("Enter the getContents(Integer p,Integer limit) method");
        //创建查询模板
        ContentVoExample example = new ContentVoExample();
        //排序方式
        example.setOrderByClause("created desc");
        //指定查询标准
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        //执行分页插件（三部曲）：设定分页条件->获取分页内容->执行分页操作
        PageHelper.startPage(p, limit);
        List<ContentVo> contentVos = contentVoDao.selectByExampleWithBLOBs(example);
        PageInfo<ContentVo> pageInfo = new PageInfo<>(contentVos);
        LOGGER.debug("Exit the getContents(Integer p,Integer limit) method");
        return pageInfo;
    }

    @Override
    //按主键查询content
    public ContentVo getContent(String cid) {
        LOGGER.debug("Enter the getContent(String cid) method");
        if (StringUtils.isNotBlank(cid)) {
            //两种情况：按照cid查找和按照slug查找
            if (PatternKit.isNum(cid)) {
                ContentVo contentVo = contentVoDao.selectByPrimaryKey(Integer.valueOf(cid));
                //每次请求都更新点击率，考虑到性能优化，不采用这种方式，而是定时批量更新
//                if (contentVo != null) {
//                    contentVo.setHits(contentVo.getHits() + 1);
//                    contentVoDao.updateByPrimaryKey(contentVo);
//                }
                LOGGER.debug("Exit the getContent(String cid) method");
                return contentVo;
            } else {
                //创建查询模板
                ContentVoExample example = new ContentVoExample();
                example.createCriteria().andSlugEqualTo(cid);
                List<ContentVo> contentVoList = contentVoDao.selectByExampleWithBLOBs(example);
                if (contentVoList.size() > 1) {
                    //slug冲突
                    throw new TipException("The Slug you selected is more than 1");
                }
                LOGGER.debug("Exit the getContent(String cid) method");
                return contentVoList.get(0);
            }
        }
        LOGGER.debug("Exit the getContent(String cid) method");
        return null;
    }

    @Override
    //获取分类文章
    public PageInfo<ContentVo> getArticles(Integer mid, int page, int limit) {
        //获取文章总数
        int total = metaVoDao.countByMid(mid);
        //分页插件三部曲
        PageHelper.startPage(page, limit);
        List<ContentVo> contentVos = contentVoDao.findByMid(mid);
        PageInfo<ContentVo> contentVoPageInfo = new PageInfo<>(contentVos);
        contentVoPageInfo.setTotal(total);
        return contentVoPageInfo;
    }


    @Override
    //更新文章内容
    public void updateContentByCid(ContentVo content) {
        if (content != null && content.getCid() != null) {
            contentVoDao.updateByPrimaryKeySelective(content);
        }
    }
}
