package com.gnss.web.command.jt808.api;

import com.gnss.common.constants.CommonConstants;
import com.gnss.web.annotations.DownCommand;
import com.gnss.web.common.service.IDownCommandService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>Description: JT808 0x8400指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-5-16
 */
@ApiModel("0x8400电话回拨参数")
@Data
@DownCommand(messageId = 0x8400, respMessageId = 0x0001, desc = "电话回拨")
public class Command8400Param implements IDownCommandService {

    @NotNull
    @ApiModelProperty(value = "标志", required = true, position = 1)
    private Integer flag;

    @NotBlank
    @ApiModelProperty(value = "电话号码", required = true, position = 2)
    private String phoneNum;

    @Override
    public byte[] buildMessageBody() throws Exception {
        byte[] msgBodyArr = null;
        ByteBuf msgBody = null;
        try {
            byte[] textMsgArr = phoneNum.getBytes(CommonConstants.DEFAULT_CHARSET_NAME);
            msgBody = Unpooled.buffer(textMsgArr.length + 1);
            msgBody.writeByte(flag).writeBytes(textMsgArr);
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
