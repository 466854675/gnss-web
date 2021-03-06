package com.gnss.web.command.jt808.api;

import com.gnss.web.annotations.DownCommand;
import com.gnss.web.common.service.IDownCommandService;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>Description: JT808 0x8107指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-12-14
 */
@ApiModel("0x8107查询终端属性")
@Data
@DownCommand(messageId = 0x8107, respMessageId = 0x0107, desc = "查询终端属性")
public class Command8107Param implements IDownCommandService {

}
