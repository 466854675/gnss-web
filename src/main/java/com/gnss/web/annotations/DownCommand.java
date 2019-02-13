package com.gnss.web.annotations;

import com.gnss.common.constants.CommonConstants;
import com.gnss.common.constants.ProtocolEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownCommand {

    /**
     * 协议类型
     *
     * @return
     */
    ProtocolEnum protocol() default ProtocolEnum.JT808;

    /**
     * 指令类型
     *
     * @return
     */
    int messageId() default CommonConstants.DEFAULT_MESSAGE_ID;

    /**
     * 字符串指令类型
     *
     * @return
     */
    String strMessageId() default "";

    /**
     * 响应指令类型
     *
     * @return
     */
    int respMessageId() default CommonConstants.DEFAULT_MESSAGE_ID;

    /**
     * 字符串响应指令类型
     *
     * @return
     */
    String strRespMessageId() default "";

    /**
     * 消息类型描述
     *
     * @return
     */
    String desc() default "不支持消息";

}
