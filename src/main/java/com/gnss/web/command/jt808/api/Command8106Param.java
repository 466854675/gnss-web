package com.gnss.web.command.jt808.api;

import com.gnss.web.annotations.DownCommand;
import com.gnss.web.common.service.IDownCommandService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashSet;

/**
 * <p>Description: JT808 0x8106指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-3-15
 */
@ApiModel("0x8106查询指定终端参数")
@Data
@DownCommand(messageId = 0x8106, respMessageId = 0x0104, desc = "查询指定终端参数")
public class Command8106Param implements IDownCommandService {

    @ApiModelProperty(value = "参数ID列表", required = true, position = 1)
    private LinkedHashSet<Integer> paramIdList;

    @Override
    public byte[] buildMessageBody() throws Exception {
        int paramIdCount = paramIdList.size();
        byte[] msgBodyArr = null;
        ByteBuf msgBody = Unpooled.buffer(4 * paramIdCount + 1);
        //设置参数总数
        msgBody.writeByte(paramIdCount);
        try {
            for (Integer paramId : paramIdList) {
                msgBody.writeInt(paramId);
            }
            msgBodyArr = msgBody.array();
        } catch (Exception e) {
            throw e;
        } finally {
            ReferenceCountUtil.release(msgBody);
        }
        return msgBodyArr;
    }
}
