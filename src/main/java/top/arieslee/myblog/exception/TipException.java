package top.arieslee.myblog.exception;

/**
 * @ClassName TipException
 * @Description 自定义异常提示类
 * @Author Aries
 * @Date 2018/7/16 11:09
 * @Version 1.0
 **/
public class TipException extends RuntimeException{

    public TipException(String message){
        super(message);
    }

}
