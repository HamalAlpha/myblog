package top.arieslee.myblog.utils;

/**
 * @ClassName UUID
 * @Description 对Java UUID工具类进行封装
 * @Author Aries
 * @Date 2018/7/25 15:46
 * @Version 1.0
 **/
public class UUID {
    //64进制表示
    private static final char[] _UUID64 = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".toCharArray();
    //32进制表示
    private static final char[] _UUID32 = "".toCharArray();

    public static String UUID64() {
        return UUID64(java.util.UUID.randomUUID());
    }

    /**
     * @return java.lang.String
     * @Description 用64进制来表示UUID
     * @Param
     **/
    public static String UUID64(java.util.UUID uu) {
        //用于存放压缩后的uuid值
        char[] uuid64 = new char[22];
        //uuid64数组偏移指针
        int index = 0;
        //获取源uuid的前64位和后64位
        long most = uu.getMostSignificantBits();
        long least = uu.getLeastSignificantBits();
        //进行与运算(&)的基值，由于我们使用64进制，即可用6bit表示，所以我们取63（111111）来进行切割
        long mask = 63L;

        //将前64位切割10次，还剩余4位参与后64位切割
        for (int off = 58; off >= 4; off -= 6) {
            long hex = (most & (mask << off)) >>> off;
            uuid64[index++] = _UUID64[(int) hex];
        }
        //前64位的低4位和后64位的高2位拼接
        int i=(int) (((most & 15) << 2) | ((least & (3 << 62))>>>62));
        uuid64[index++] = _UUID64[i];
        //前64位剩余的62位先分割10次，剩余2位单独处理
        for (int off = 56; off >= 2; off -= 6) {
            long hex = (least & (mask << off) >>> off);
            uuid64[index++] = _UUID64[(int) hex];
        }
        //处理剩余2位
        uuid64[index] = _UUID64[(int) least & 3];
        //返回字符串
        return uuid64.toString();
    }
}
