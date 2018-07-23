package top.arieslee.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.arieslee.myblog.dao.CommentVoDao;
import top.arieslee.myblog.dao.ContentVoDao;
import top.arieslee.myblog.dto.CommentDto;
import top.arieslee.myblog.exception.TipException;
import top.arieslee.myblog.modal.VO.CommentVo;
import top.arieslee.myblog.modal.VO.CommentVoExample;
import top.arieslee.myblog.modal.VO.ContentVo;
import top.arieslee.myblog.service.ICommentService;
import top.arieslee.myblog.utils.PatternKit;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CommentServiceImpl
 * @Description 评论接口实现类
 * @Author Aries
 * @Date 2018/7/17 11:14
 * @Version 1.0
 **/
@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private ContentVoDao contentVoDao;

    @Autowired
    private CommentVoDao commentVoDao;

    @Override
    public PageInfo<CommentDto> getComment(Integer cid, Integer pageNum, Integer limit) {
        //当前线程绑定分页模板
        PageHelper.startPage(pageNum, limit);
        //构造Comment表查询模板
        CommentVoExample example = new CommentVoExample();
        example.createCriteria().andCidEqualTo(cid).andParentEqualTo(0);
        example.setOrderByClause("coid desc");
        List<CommentVo> parentComments = commentVoDao.selectByExampleWithBLOBs(example);
        PageInfo<CommentVo> voPageInfo = new PageInfo<>(parentComments);
        //构造评论业务对象
        PageInfo<CommentDto> boPageInfo = copyPageInfo(voPageInfo);
        if (parentComments.size() != 0) {
            List<CommentDto> bos = new ArrayList<>();
            parentComments.forEach(parentComment -> {
                CommentDto commentDto = new CommentDto(parentComment);
                bos.add(commentDto);
            });
            boPageInfo.setList(bos);
        }
        return boPageInfo;
    }

    @Override
    public void insertComment(CommentVo commentVo) {
        if (commentVo == null) {
            throw new TipException("评论对象为空");
        }
        if (StringUtils.isBlank(commentVo.getAuthor())) {
            commentVo.setAuthor("(╯▽╰)");
        }
        if (StringUtils.isNotBlank(commentVo.getMail()) && !PatternKit.isEmail(commentVo.getMail())) {
            throw new TipException("请输入正确的邮箱格式");
        }
        if (StringUtils.isBlank(commentVo.getContent())) {
            throw new TipException("评论不能为空");
        }
        if (commentVo.getContent().length() < 5 || commentVo.getContent().length() > 2000) {
            throw new TipException("评论字数请控制在5到2000之间");
        }
        if (commentVo.getCid() == null) {
            throw new TipException("评论文章不能为空");
        }
        ContentVo contentVo = contentVoDao.selectByPrimaryKey(commentVo.getCid());
        if (contentVo == null) {
            throw new TipException("评论文章不存在");
        }

        //补充默认字段
        commentVo.setOwnerId(contentVo.getCid());//评论归属文章id
        commentVo.setCreated((int) (System.currentTimeMillis() / 1000));//评论创建时间
        //执行插入操作
        commentVoDao.insertComment(commentVo);
        //更新评论数
        contentVo.setCommentsNum(contentVo.getCommentsNum() + 1);
        contentVoDao.updateByPrimaryKey(contentVo);

    }

    /**
     * @return com.github.pagehelper.PageInfo<T>
     * @Description 赋值分页插件的基本属性
     * @Param [origin]
     **/
    public <T> PageInfo<T> copyPageInfo(PageInfo ordinal) {
        PageInfo<T> returnBo = new PageInfo<>();
        returnBo.setPageSize(ordinal.getPageSize());
        returnBo.setPageNum(ordinal.getPageNum());
        returnBo.setEndRow(ordinal.getEndRow());
        returnBo.setTotal(ordinal.getTotal());
        returnBo.setHasNextPage(ordinal.isHasNextPage());
        returnBo.setHasPreviousPage(ordinal.isHasPreviousPage());
        returnBo.setIsFirstPage(ordinal.isIsFirstPage());
        returnBo.setIsLastPage(ordinal.isIsLastPage());
        returnBo.setNavigateFirstPage(ordinal.getNavigateFirstPage());
        returnBo.setNavigateLastPage(ordinal.getNavigateLastPage());
        returnBo.setNavigatepageNums(ordinal.getNavigatepageNums());
        returnBo.setSize(ordinal.getSize());
        returnBo.setPrePage(ordinal.getPrePage());
        returnBo.setNextPage(ordinal.getNextPage());
        return returnBo;
    }
}
