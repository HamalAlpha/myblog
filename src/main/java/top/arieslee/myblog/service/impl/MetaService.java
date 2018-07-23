package top.arieslee.myblog.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.arieslee.myblog.dao.MetaVoDao;
import top.arieslee.myblog.dto.MetaDto;
import top.arieslee.myblog.service.IMetaService;

/**
 * @ClassName MetaService
 * @Description Meta接口实现类
 * @Author Aries
 * @Date 2018/7/23 11:46
 * @Version 1.0
 **/
@Service
public class MetaService implements IMetaService {

    @Autowired
    private MetaVoDao metaVoDao;

    @Override
    public MetaDto getMetaCount(String type, String name) {
        if(StringUtils.isNotBlank(type)&&StringUtils.isNotBlank(name)){
           return metaVoDao.selectByTypeAndName(type,name);
        }
        return null;
    }
}
