package top.arieslee.myblog.service;

import com.github.pagehelper.PageInfo;
import top.arieslee.myblog.modal.VO.ContentVo;

/**
 * @ClassName ContentService
 * @Description ContentService业务层接口
 * @Author Aries
 * @Date 2018/7/10 15:57
 * @Version 1.0
 **/
public interface ContentService {
    
    /**
     * @Description : 获取分页列表
     * @Date : 8:35 2018/7/11
     * @Param [p：当前页码, limit：每页文章数量]
     * @return com.github.pagehelper.PageInfo 返回mybatis分页组件PageInfo
     **/
    public PageInfo<ContentVo> getContent(Integer p, Integer limit);
}
