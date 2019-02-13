package com.gnss.web.command.jt808.api;

import com.gnss.common.constants.CommonConstants;
import com.gnss.common.exception.ApplicationException;
import com.gnss.web.annotations.DownCommand;
import com.gnss.web.common.service.IDownCommandService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: JT808 0x8105指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-14
 */
@ApiModel("0x8105终端控制")
@Data
@Slf4j
@DownCommand(messageId = 0x8105, respMessageId = 0x0001, desc = "终端控制")
public class Command8105Param implements IDownCommandService {

    @ApiModelProperty(value = "命令字", required = true, position = 1)
    private int command;

    @ApiModelProperty(value = "命令项", position = 2)
    private String param;

    @Override
    public byte[] buildMessageBody() throws Exception {
        if (command < 1 || command > 7) {
            throw new ApplicationException("命令字必须为1-7");
        }

        byte[] msgBodyArr = null;
        if (command == 1 || command == 2) {
            if (StringUtils.isNotBlank(param)) {
                byte[] paramArr = param.getBytes(CommonConstants.DEFAULT_CHARSET_NAME);
                ByteBuf msgBody = Unpooled.buffer(paramArr.length + 1);
                msgBody.writeByte(command).writeBytes(paramArr);
                msgBodyArr = msgBody.array();
                ReferenceCountUtil.release(msgBody);
            }
        } else {
            msgBodyArr = new byte[]{(byte) command};
        }
        return msgBodyArr;
    }
}
