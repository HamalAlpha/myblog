package top.arieslee.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.arieslee.myblog.constant.WebConstant;
import top.arieslee.myblog.dao.LogVoDao;
import top.arieslee.myblog.modal.VO.LogVo;
import top.arieslee.myblog.modal.VO.LogVoExample;
import top.arieslee.myblog.service.ILogService;
import top.arieslee.myblog.utils.DateKit;

import java.util.List;

/**
 * @ClassName LogService
 * @Description TODO
 * @Author Aries
 * @Date 2018/8/3 10:22
 * @Version 1.0
 **/
@Service
public class LogService implements ILogService {

    @Autowired
    private LogVoDao logVoDao;

    @Override
    public void insertLog(LogVo logVo) {

    }

    @Override
    public void insertLog(String action, String data, int authorId, String ip) {
        LogVo logVo=new LogVo();
        logVo.setAction(action);
        logVo.setData(data);
        logVo.setId(authorId);
        logVo.setIp(ip);
        logVo.setCreated(DateKit.getCurrentUnixTime());
        logVoDao.insert(logVo);
    }

    @Override
    public List<LogVo> getLogs(int page, int limit) {
        if (page <= 0 || page>WebConstant.MAX_PAGE) {
            page = 1;
        }
        if (limit < 1 || limit > WebConstant.MAX_LIMIT) {
            limit = 10;
        }
        LogVoExample logVoExample = new LogVoExample();
        logVoExample.setOrderByClause("id desc");
        PageHelper.startPage((page - 1) * limit, limit);
        List<LogVo> logVos = logVoDao.selectByExample(logVoExample);
        return logVos;
    }
}
