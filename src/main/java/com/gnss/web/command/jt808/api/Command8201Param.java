package com.gnss.web.command.jt808.api;

import com.gnss.web.annotations.DownCommand;
import com.gnss.web.common.service.IDownCommandService;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>Description: JT808 0x8201指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8201位置信息查询")
@Data
@DownCommand(messageId = 0x8201, respMessageId = 0x0201, desc = "位置信息查询")
public class Command8201Param implements IDownCommandService {

}
