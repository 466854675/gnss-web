package com.gnss.web.common.constant;

import lombok.Getter;

/**
 * <p>Description: 响应结果编码</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
@Getter
public enum ResultCodeEnum {
    /**
     * 未登录
     */
    NO_LOGIN(-1, "no login"),
    /**
     * 成功
     */
    SUCCESS(0, "success"),
    /**
     * 失败
     */
    FAIL(1, "fail"),
    /**
     * 没有权限
     */
    NO_PERMISSION(2, "no permission");

    private Integer code;

    private String desc;

    ResultCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
