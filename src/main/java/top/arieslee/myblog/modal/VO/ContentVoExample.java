package top.arieslee.myblog.modal.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ContentVoExample
 * @Description content表查询模板
 * @Author Aries
 * @Date 2018/7/10 14:41
 * @Version 1.0
 **/
public class ContentVoExample {

    /**
     * @Description : 子句排序
     **/
    protected String orderByClause;

    /**
     * @Description : 去重
     **/
    protected boolean distinct;

    /**
     * @Description : 标准模板集合
     **/
    protected List<Criteria> criteriaList;

    /**
     * @Description : 当前页码
     **/
    private Integer p;

    /**
     * @Description : 每页显示数量
     **/
    private Integer limit;

    public ContentVoExample() {
        //实例化标准模板集合
        this.criteriaList = new ArrayList<>();
    }

    //加入指定标准模板
    protected void or(Criteria criteria) {
        criteriaList.add(criteria);
    }

    //加入新的标准模板
    protected Criteria or() {
        Criteria criteria = createCriteriaInternal();
        criteriaList.add(criteria);
        return criteria;
    }

    //创建模板，并返回
    public Criteria createCriteria() {
        if (criteriaList.size() == 0) {
            Criteria criteria = createCriteriaInternal();
            criteriaList.add(criteria);
        }
        return criteriaList.get(0);
    }

    //创建内部Criteria类对象
    protected Criteria createCriteriaInternal() {
        return new Criteria();
    }

    //清理
    protected void clear() {
        criteriaList.clear();
        distinct = false;
        orderByClause = null;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @Description : 标准模板抽象类
     **/
    protected abstract static class GeneratedCriteria {
        /**
         * @Description : 标准实体集合
         **/
        private List<Criterion> criterionList;

        protected GeneratedCriteria() {
            criterionList = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criterionList.size() > 0;
        }

        public List<Criterion> getCriterionList() {
            return criterionList;
        }

        public void setCriterionList(List<Criterion> criterionList) {
            this.criterionList = criterionList;
        }

        /**
         * @Description : 类型相等条件
         **/
        public Criteria andTypeEqualTo(String value) {
            addCriterion("type=", value, "type");
            return (Criteria) this;
        }

        /**
         * @Description : 状态相等条件
         **/
        public Criteria andStatusEauqlTo(String value) {
            addCriterion("status=", value, "status");
            return (Criteria) this;
        }

        /**
         * @Description slug相等条件
         **/
        public Criteria andSlugEqualTo(String value) {
            addCriterion("slug=", value, "slug");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaderThan(Integer value) {
            addCriterion("created >", value, "created");
            return (Criteria)this;
        }

        public Criteria andCreatedLessThan(Integer value){
            addCriterion("created <",value,"created");
            return (Criteria)this;
        }

        /**
         * @return void
         * @Description :添加
         * @Date : 15:43 2018/7/11
         * @Param [condition：条件, value：条件值, property：条件名称]
         **/
        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                //值为空，抛出运行时异常
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            //插入新的标准实体
            criterionList.add(new Criterion(condition, value));
        }
    }

    /**
     * @Description : 标准模板实现类
     **/
    public static class Criteria extends GeneratedCriteria {
        public Criteria() {
            super();
        }
    }

    /**
     * @Description : 标准实体
     **/
    public static class Criterion {
        /**
         * @Description : 条件
         **/
        private String condition;
        /**
         * @Description : 条件值
         **/
        private Object value;
        /**
         * @Description : 第二个条件值，用于between等判断
         **/
        private Object secondValue;
        /**
         * @Description : 值是否为空
         **/
        private boolean noValue;
        /**
         * @Description : 是否为单个值
         **/
        private boolean singleValue;
        /**
         * @Description : 是否为between
         **/
        private boolean betweenValue;
        /**
         * @Description : 是否为值集合
         **/
        private boolean listValue;
        /**
         * @Description : 类型处理
         **/
        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        /**
         * @Description : 只有条件没有值的情况
         **/
        protected Criterion(String condition) {
            this.condition = condition;
            this.noValue = true;
            this.typeHandler = null;
        }

        /**
         * @Description : 单个值情况
         **/
        protected Criterion(String condition, Object value, String typeHandler) {
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            //判断值类型
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        /**
         * @Description : 多个值情况，用于betweens处理SQL
         **/
        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
