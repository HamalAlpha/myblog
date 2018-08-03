package top.arieslee.myblog.constant;

/**
 * @ClassName LogAction
 * @Description 封装日志行为
 * @Author Aries
 * @Date 2018/8/3 11:00
 * @Version 1.0
 **/
public enum LogAction {
    LOGIN("登录后台");

    private String action;

    private LogAction(String action){
        this.action=action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
