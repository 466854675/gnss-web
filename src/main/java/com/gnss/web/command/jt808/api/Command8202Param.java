package com.gnss.web.command.jt808.api;

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

/**
 * <p>Description: JT808 0x8202指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-12-14
 */
@ApiModel("0x8202临时位置跟踪控制")
@Data
@Slf4j
@DownCommand(messageId = 0x8202, respMessageId = 0x0001, desc = "临时位置跟踪控制")
public class Command8202Param implements IDownCommandService {

    @ApiModelProperty(value = "时间间隔", required = true, position = 1)
    private int interval;

    @ApiModelProperty(value = "位置跟踪有效期", position = 2)
    private Integer duration;

    @Override
    public byte[] buildMessageBody() throws Exception {
        byte[] msgBodyArr = null;
        if (interval == 0) {
            msgBodyArr = new byte[]{0, 0};
        } else {
            if (duration == null) {
                throw new ApplicationException("位置跟踪有效期不能为空");
            }
            ByteBuf msgBody = Unpooled.buffer(6);
            msgBody.writeShort(interval).writeInt(duration);
            msgBodyArr = msgBody.array();
            ReferenceCountUtil.release(msgBody);
        }
        return msgBodyArr;
    }
}
