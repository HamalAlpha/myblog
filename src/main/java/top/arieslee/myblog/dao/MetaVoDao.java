package top.arieslee.myblog.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.arieslee.myblog.dto.MetaDto;

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
     * @Description 获取指定类别信息数量
     * @Param [mid]
     * @return java.lang.Integer
     **/
    Integer countByMid(Integer mid);

    /**
     * @Description 根据分类类别和分类关键字获取MetaDto对象
     * @Param [type, name]
     * @return top.arieslee.myblog.dto.MetaDto
     **/
    MetaDto selectByTypeAndName(@Param("type") String type,@Param("name") String name);
}
