package com.gnss.web.command.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 流媒体设置信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-2-12
 */
@ApiModel("流媒体设置信息")
@Data
public class MediaConfigDTO {

    @ApiModelProperty(value = "服务器IP", position = 1)
    private String serverIp;

    @ApiModelProperty(value = "TCP端口", position = 2)
    private int tcpPort;

    @ApiModelProperty(value = "UDP端口", position = 3)
    private int udpPort;
}
