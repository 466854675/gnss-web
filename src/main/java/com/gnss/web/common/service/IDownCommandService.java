package com.gnss.web.common.service;

/**
 * <p>Description: 下行指令服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
public interface IDownCommandService {

    /**
     * 构造协议包消息体
     *
     * @return
     * @throws Exception
     */
    default byte[] buildMessageBody() throws Exception {
        return null;
    }
}
