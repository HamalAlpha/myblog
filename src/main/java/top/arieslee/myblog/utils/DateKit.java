package top.arieslee.myblog.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName DateKit
 * @Description 日期工具类
 * @Author Aries
 * @Date 2018/7/24 10:39
 * @Version 1.0
 **/
public class DateKit {

    public static final int INTERVAL_DAY=1;
    public static final int INTERVAL_WEEK=2;
    public static final int INTERVAL_MONTH=3;
    public static final int INTERVAL_YEAR=4;
    public static final int INTERVAL_HOUR=5;
    public static final int INTERVAL_MINUTE=6;
    public static final int INTERVAL_SECOND=7;


    /**
     * @return java.util.Date
     * @Description 将字符串解析为日期对象
     * @Param [date 字符串, format 解析类型]
     **/
    public static Date dateParse(String date, String format) {
        if (StringUtils.isBlank(date)) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static int getTimeByDate(Date date) {
        return (int)(date.getTime() / 1000L);
    }

    /**
     * @return java.util.Date
     * @Description 在原有时间上增加一段时间，这里待改进，没有考虑每月不一定为30天的情况
     * @Param [interval 增加幅度：年、月等, date 原时间, n 增加数量]
     **/
    public static Date dateAdd(int interval, Date date, int n) {
        Long time = date.getTime() / 1000L;
        switch (interval) {
            case 1:
                time += (long) (n * 86400);//天
                break;
            case 2:
                time += (long) (n * 604800);//星期
                break;
            case 3:
                time += (long) (n * 2678400);//月
                break;
            case 4:
                time += (long) (n * 31536000);//年
                break;
            case 5:
                time += (long) (n * 3600);//时
                break;
            case 6:
                time += (long) (n * 60);//分
                break;
            case 7:
                time += (long) n;//秒
        }
        Date result = new Date();
        result.setTime(time * 1000L);
        return result;
    }
}
