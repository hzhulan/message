package com.hzhu.wechat.utils;

import com.hzhu.wechat.model.Constants;

import java.text.SimpleDateFormat;

/**
 * @fileName: DateUtils
 * @version: V1.0
 * @Description:
 * @Author: X6879
 * @Date: 2021/9/29 17:33
 */
public class DateUtils {

    private static final ThreadLocal<SimpleDateFormat> THREADLOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private DateUtils(){
    }

    /**
     * 获取当前绝对秒
     * @return
     */
    public static long currentSec() {
        return System.currentTimeMillis() / Constants.INT_1000;
    }

    /**
     * 获取当前绝对毫秒
     * @return
     */
    public static long currentMilSec() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间，格式化后的
     * @return
     */
    public static String currentTime() {
        return DateUtils.format(currentSec());
    }

    /**
     * 格式化
     * @param second
     * @return
     */
    public static String format(Long second) {
        if(second == null) {
            return "";
        }
        return THREADLOCAL.get().format(second * Constants.INT_1000);
    }
}
