package top.arieslee.myblog.service;

import com.github.pagehelper.PageInfo;
import top.arieslee.myblog.exception.TipException;
import top.arieslee.myblog.modal.VO.ContentVo;

/**
 * @ClassName IContentService
 * @Description ContentService业务层接口
 * @Author Aries
 * @Date 2018/7/10 15:57
 * @Version 1.0
 **/
public interface IContentService {
    
    /**
     * @Description : 获取分页列表
     * @Date : 8:35 2018/7/11
     * @Param [p：当前页码, limit：每页文章数量]
     * @return com.github.pagehelper.PageInfo 返回mybatis分页组件PageInfo
     **/
    PageInfo<ContentVo> getContent(Integer p, Integer limit);

    /**
     * @Description :根据cid或者slug获取指定文章
     * @Date : 17:28 2018/7/13
     * @Param [cid:文章标识]
     * @return 
     **/
    ContentVo getContent(String cid);
}
