package top.arieslee.myblog.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.arieslee.myblog.dto.MetaDto;
import top.arieslee.myblog.modal.VO.MetaVo;
import top.arieslee.myblog.modal.VO.MetaVoExample;

import java.util.List;

/**
 * @ClassName MetaDao
 * @Description Meta表数据访问接口
 * @Author Aries
 * @Date 2018/7/23 15:04
 * @Version 1.0
 **/
@Repository
public interface MetaVoDao {

    /**
     * @return java.lang.Integer
     * @Description 获取指定类别信息数量
     * @Param [mid]
     **/
    Integer countByMid(Integer mid);

    /**
     * @return top.arieslee.myblog.dto.MetaDto
     * @Description 根据分类类别和分类关键字获取MetaDto对象
     * @Param [type, name]
     **/
    MetaDto selectByTypeAndName(@Param("type") String type, @Param("name") String name);

    /**
     * @return java.util.List<top.arieslee.myblog.modal.VO.MetaVo>
     * @Description 获取链接meta
     * @Param
     **/
    List<MetaVo> selectByExample(MetaVoExample example);
}
