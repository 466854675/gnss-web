package com.gnss.web.common.utils;

/**
 * <p>Description: GNSS工具</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
public class GnssUtils {

    private GnssUtils() {
    }

    /**
     * 构造指令事件Key
     *
     * @param terminalId
     * @param downCommandId
     * @return
     */
    public static String buildCommandEventKey(long terminalId, String downCommandId) {
        return String.format("%d-%s", terminalId, downCommandId);
    }

}
