package top.arieslee.myblog.dao;

import org.springframework.stereotype.Component;
import top.arieslee.myblog.modal.VO.ContentVo;
import top.arieslee.myblog.modal.VO.ContentVoExample;

import java.util.List;

/**
 * @ClassName ContentVoDao
 * @Description content表数据访问层接口
 * @Author Aries
 * @Date 2018/7/10 12:00
 * @Version 1.0
 **/
@Component
public interface ContentVoDao {

    /**
     * @Description :
     * @Date : 16:21 2018/7/11
     * @Param [example：模板]
     **/
    List<ContentVo> selectByExampleWithBLOBs(ContentVoExample example);
}
