package com.gnss.web.command.api;

import com.gnss.common.constants.CommandRequestTypeEnum;
import com.gnss.common.constants.ProtocolEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * <p>Description: 指令信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018/2/14
 */
@ApiModel("指令信息")
@Data
public class CommandDTO<T> {

    @ApiModelProperty(value = "终端ID", required = true, position = 1)
    @NotNull(message = "终端ID不能为空")
    private Long terminalId;

    @ApiModelProperty(value = "协议类型", required = true, position = 2)
    @NotNull(message = "协议类型不能为空")
    private ProtocolEnum protocol = ProtocolEnum.JT808;

    @ApiModelProperty(value = "下行指令", position = 3)
    private String downCommandId;

    @ApiModelProperty(value = "指令内容", position = 4)
    private Map<String, Object> params;

    @ApiModelProperty(value = "请求方式(同步/异步)", position = 5)
    private CommandRequestTypeEnum requestType = CommandRequestTypeEnum.SYNC;

    @ApiModelProperty(value = "超时时间(毫秒,请求方式为同步时有效)", position = 6)
    private Integer responseTimeout;

    @ApiModelProperty(value = "指令内容实体", position = 7)
    @Valid
    private T paramsEntity;

    public Class getParamsEntityClass() {
        return paramsEntity.getClass();
    }
}
