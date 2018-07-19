package top.arieslee.myblog.constant;

/**
 * @ClassName Types
 * @Description 存放各种全局类型值
 * @Author Aries
 * @Date 2018/7/11 11:27
 * @Version 1.0
 **/
public enum Types {
    PUBLISH("publish"),
    ARTICLE("post"),
    CSRF_TOKEN("csrf_token"),
    COMMENT_FREQUENCY("comment_frequency");

    private String type;

    Types(String type){
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
