package pri.hzhu.file.common.utils;

import pri.hzhu.file.common.model.Constants;

/**
 * @author: pp_lan
 * @date: 2022/1/2
 */
public class DateUtils {

    /**
     * 获取档期那绝对秒
     * @return
     */
    public static Long currentSecond() {
        return System.currentTimeMillis() / Constants.INT_1000;
    }
}
