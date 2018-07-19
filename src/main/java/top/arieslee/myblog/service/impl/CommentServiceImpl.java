package top.arieslee.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.arieslee.myblog.dao.CommentVoDao;
import top.arieslee.myblog.dto.CommentDto;
import top.arieslee.myblog.modal.VO.CommentVo;
import top.arieslee.myblog.modal.VO.CommentVoExample;
import top.arieslee.myblog.service.ICommentService;

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
    private CommentVoDao commentVoDao;

    @Override
    public PageInfo<CommentDto> getComment(Integer cid, Integer pageNum, Integer limit) {
        //当前线程绑定分页模板
        PageHelper.startPage(pageNum,limit);
        //构造Comment表查询模板
        CommentVoExample example=new CommentVoExample();
        example.createCriteria().andCidEqualTo(cid).andParentEqualTo(0);
        example.setOrderByClause("coid desc");
        List<CommentVo> parentComments=commentVoDao.selectByExampleWithBLOBs(example);
        PageInfo<CommentVo> voPageInfo = new PageInfo<>(parentComments);
        //构造评论业务对象
        PageInfo<CommentDto> boPageInfo = copyPageInfo(voPageInfo);
        if(parentComments.size()!=0){
            List<CommentDto> bos=new ArrayList<>();
            parentComments.forEach(parentComment->{
                CommentDto commentDto =new CommentDto(parentComment);
                bos.add(commentDto);
            });
            boPageInfo.setList(bos);
        }
        return boPageInfo;
    }
    
    /**
     * @Description 赋值分页插件的基本属性
     * @Param [origin]
     * @return com.github.pagehelper.PageInfo<T>
     **/
    public <T> PageInfo<T> copyPageInfo(PageInfo ordinal){
        PageInfo<T> returnBo=new PageInfo<>();
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
