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
import java.util.List;

/**
 * <p>Description: JT808 0x8300指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8300文本信息下发参数")
@Data
@DownCommand(messageId = 0x8300, respMessageId = 0x0001, desc = "文本信息下发")
public class Command8300Param implements IDownCommandService {

    @NotNull
    @ApiModelProperty(value = "标志位", required = true, position = 1)
    private List<Integer> flags;

    @NotBlank
    @ApiModelProperty(value = "文本信息", required = true, position = 2)
    private String textMsg;

    @Override
    public byte[] buildMessageBody() throws Exception {
        byte[] msgBodyArr = null;
        ByteBuf msgBody = null;
        try {
            char[] chars = new char[8];
            for (int i = 0; i < 8; i++) {
                char value = flags.contains(i) ? '1' : '0';
                chars[7 - i] = value;
            }
            int flag = Integer.parseInt(new String(chars), 2);
            byte[] textMsgArr = textMsg.getBytes(CommonConstants.DEFAULT_CHARSET_NAME);
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