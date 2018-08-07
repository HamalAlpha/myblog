package top.arieslee.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.arieslee.myblog.constant.Types;
import top.arieslee.myblog.dao.CommentVoDao;
import top.arieslee.myblog.dao.ContentVoDao;
import top.arieslee.myblog.dao.MetaVoDao;
import top.arieslee.myblog.dto.ArchiveDto;
import top.arieslee.myblog.dto.MetaDto;
import top.arieslee.myblog.dto.StatisticsDto;
import top.arieslee.myblog.modal.VO.*;
import top.arieslee.myblog.service.ISiteService;
import top.arieslee.myblog.utils.DateKit;

import java.util.Date;
import java.util.List;

/**
 * @ClassName SiteService
 * @Description 站点业务逻辑实现类
 * @Author Aries
 * @Date 2018/7/24 10:07
 * @Version 1.0
 **/
@Service
public class SiteServiceImpl implements ISiteService {

    @Autowired
    private ContentVoDao contentVoDao;

    @Autowired
    private CommentVoDao commentVoDao;

    @Autowired
    private MetaVoDao metaVoDao;

    @Override
    public List<ArchiveDto> getArchives() {
        //获取归档列表
        List<ArchiveDto> archives = contentVoDao.findArchiveDtoByMonth();
        //获取所属归档月的文章列表
        if (archives != null) {
            archives.forEach(archive -> {
                ContentVoExample example = new ContentVoExample();
                ContentVoExample.Criteria criteria = example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
                example.setOrderByClause("created desc");
                //获取归档日期
                String date = archive.getDate();
                //将字符串按指定格式解析为Date对象
                Date sd = DateKit.dateParse(date, "yyyy年MM月");
                //设定开始和结束时间
                int start = DateKit.getUnixTimeByDate(sd);
                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1));
                criteria.andCreatedGreaderThan(start);
                criteria.andCreatedLessThan(end);
                //查询该月份的文章列表
                List<ContentVo> contentVos = contentVoDao.selectByExample(example);
                archive.setArticles(contentVos);
            });
        }
        return archives;
    }

    @Override
    public List<CommentVo> recentComment(int limit) {
        if (limit < 0 || limit > 5) {
            limit = 5;
        }
        CommentVoExample example = new CommentVoExample();
        example.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        List<CommentVo> byPage = commentVoDao.selectByExampleWithBLOBs(example);
        return byPage;
    }

    @Override
    public List<ContentVo> recentContent(int limit) {
        if (limit < 0 || limit > 5) {
            limit = 5;
        }
        ContentVoExample example=new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        PageHelper.startPage(1,limit);
        List<ContentVo> byPage = contentVoDao.selectByExampleWithBLOBs(example);
        return byPage;
    }

    @Override
    public StatisticsDto currentStatistics() {

        StatisticsDto statistics = new StatisticsDto();

        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        Long articles = contentVoDao.countByExample(contentVoExample);

        Long comments = commentVoDao.countByExample(new CommentVoExample());

//        Long attachs = attachVoDao.countByExample(new AttachVoExample());

        MetaVoExample metaVoExample = new MetaVoExample();
        metaVoExample.createCriteria().andTypeEqualTo(Types.LINK.getType());
        Long links = metaVoDao.countByExample(metaVoExample);

        statistics.setArticles(articles);
        statistics.setComments(comments);
//        statistics.setAttachs(attachs);
        statistics.setLinks(links);
        return statistics;
    }


}
