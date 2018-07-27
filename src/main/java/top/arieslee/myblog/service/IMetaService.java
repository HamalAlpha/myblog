package top.arieslee.myblog.service;

import top.arieslee.myblog.dto.MetaDto;
import top.arieslee.myblog.modal.VO.MetaVo;

import java.util.List;

/**
 * @ClassName IMetaService
 * @Description meta表业务逻辑
 * @Author Aries
 * @Date 2018/7/23 11:35
 * @Version 1.0
 **/
public interface IMetaService {

    /**
     * @return top.arieslee.myblog.dto.MetaDto
     * @Description 根据分类类型和分类关键字获取分类文章数量
     * @Param [type, name]
     **/
    MetaDto getMetaCount(String type, String name);

    /**
     * @return java.util.List<top.arieslee.myblog.modal.VO.MetaVo>
     * @Description 获取友情链接
     * @Param 
     **/
    List<MetaVo> getLinks(String type);
}
