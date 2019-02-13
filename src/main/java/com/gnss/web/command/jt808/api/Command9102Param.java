package com.gnss.web.command.jt808.api;

import com.gnss.web.annotations.DownCommand;
import com.gnss.web.common.service.IDownCommandService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>Description: JT808 0x9102指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9102音视频实时传输控制")
@Data
@DownCommand(messageId = 0x9102, respMessageId = 0x0001, desc = "音视频实时传输控制")
public class Command9102Param implements IDownCommandService {

    @NotNull
    @ApiModelProperty(value = "逻辑通道号", required = true, position = 1)
    private Integer channelId;

    @NotNull
    @ApiModelProperty(value = "控制指令", required = true, position = 2)
    private Integer ctrlCmd;

    @NotNull
    @ApiModelProperty(value = "音视频类型", required = true, position = 3)
    private Integer avItemType;

    @NotNull
    @ApiModelProperty(value = "码流类型", required = true, position = 4)
    private Integer streamType;

    @Override
    public byte[] buildMessageBody() throws Exception {
        byte[] msgBodyArr = null;
        ByteBuf msgBody = null;
        try {
            msgBody = Unpooled.buffer(4);
            msgBody.writeByte(channelId)
                    .writeByte(ctrlCmd)
                    .writeByte(avItemType)
                    .writeByte(streamType);
            msgBodyArr = msgBody.array();
        } catch (Exception e) {
            throw e;
        } finally {
            if (msgBody != null) {
                ReferenceCountUtil.release(msgBody);
            }
        }
        return msgBodyArr;
    }
}