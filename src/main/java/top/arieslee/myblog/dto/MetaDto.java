package top.arieslee.myblog.dto;

import top.arieslee.myblog.modal.VO.MetaVo;

/**
 * @ClassName MetaDto
 * @Description 元数据dto
 * @Author Aries
 * @Date 2018/7/23 11:44
 * @Version 1.0
 **/
public class MetaDto extends MetaVo {
    /**
     * @Description 记录该分类下共有多少篇文章
     **/
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
