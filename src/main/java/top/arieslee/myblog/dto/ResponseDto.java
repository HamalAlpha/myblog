package top.arieslee.myblog.dto;

/**
 * @ClassName ResponseDto
 * @Description 封装响应请求传输数据
 * @Author Aries
 * @Date 2018/7/19 15:23
 * @Version 1.0
 **/
public class ResponseDto<T> {
    /**
     * @Description 响应状态
     **/
    private boolean status;

    /**
     * @Description 状态码
     **/
    private int code;

    /**
     * @Description 封装数据
     **/
    private T data;

    /**
     * @Description 响应信息
     **/
    private String message;

    public ResponseDto(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResponseDto fail(String msg){
        return new ResponseDto(false,msg);
    }
}
