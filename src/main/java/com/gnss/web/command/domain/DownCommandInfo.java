package com.gnss.web.command.domain;

import lombok.Data;

/**
 * <p>Description: 下行指令信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018/2/14
 */
@Data
public class DownCommandInfo {

    /**
     * 下行指令ID
     */
    private String downCommandId;

    /**
     * 响应指令ID
     */
    private String respCommandId;

    /**
     * 指令描述
     */
    private String desc;

    /**
     * 指令参数对应的Class
     */
    private Class<?> commandParamClass;
}
