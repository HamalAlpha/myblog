package top.arieslee.myblog.service;

import top.arieslee.myblog.dto.ArchiveDto;

import java.util.List;

/**
 * @ClassName ISiteService
 * @Description 站内业务逻辑
 * @Author Aries
 * @Date 2018/7/24 10:01
 * @Version 1.0
 **/
public interface ISiteService {

    /**
     * @Description 获取归档列表
     * @Param []
     * @return java.util.List<top.arieslee.myblog.dto.ArchiveDto>
     **/
    List<ArchiveDto> getArchives();
}
