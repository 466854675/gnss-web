package com.gnss.web.command.jt808.api;

import com.gnss.common.constants.CommonConstants;
import com.gnss.common.utils.Jt808Util;
import com.gnss.web.annotations.DownCommand;
import com.gnss.web.common.service.IDownCommandService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * <p>Description: JT808 0x8103指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018/2/15
 */
@ApiModel("0x8103设置终端参数")
@Data
@Slf4j
@DownCommand(messageId = 0x8103, respMessageId = 0x0001, desc = "设置终端参数")
public class Command8103Param implements IDownCommandService {

    @ApiModelProperty(value = "参数项", required = true, position = 1)
    private Map<String, Object> items;

    @Override
    public byte[] buildMessageBody() throws Exception {
        byte[] msgBodyArr = null;
        ByteBuf msgBody = Unpooled.buffer();
        //设置参数总数，等后续所有参数项写入后再重新设置
        msgBody.writeByte(0);
        int itemCount = 0;
        try {
            for (Map.Entry<String, Object> entry : items.entrySet()) {
                boolean isWritten = writeItem(msgBody, Integer.parseInt(entry.getKey(), 16), entry.getValue());
                if (isWritten) {
                    itemCount++;
                }
            }
            //重新设置参数总数
            msgBody.setByte(0, itemCount);
            int len = msgBody.readableBytes();
            msgBodyArr = new byte[len];
            msgBody.getBytes(msgBody.readerIndex(), msgBodyArr);
        } catch (Exception e) {
            throw e;
        } finally {
            ReferenceCountUtil.release(msgBody);
        }
        return msgBodyArr;
    }

    /**
     * 设置参数项
     *
     * @param msgBody
     * @param paramId
     * @param paramValue
     * @return
     * @throws Exception
     */
    private boolean writeItem(ByteBuf msgBody, int paramId, Object paramValue) throws Exception {
        if (paramValue == null) {
            return false;
        }

        //根据参数ID获取对应的数据类型
        Class<?> itemType = Jt808Util.getParamType(paramId);
        if (itemType == Integer.class) {
            msgBody.writeInt(paramId);
            msgBody.writeByte(4);
            msgBody.writeInt(Integer.parseInt(paramValue.toString()));
        } else if (itemType == Short.class) {
            msgBody.writeInt(paramId);
            msgBody.writeByte(2);
            msgBody.writeShort(Short.parseShort(paramValue.toString()));
        } else if (itemType == Byte.class) {
            msgBody.writeInt(paramId);
            msgBody.writeByte(1);
            msgBody.writeByte(Byte.parseByte(paramValue.toString()));
        } else if (itemType == String.class) {
            msgBody.writeInt(paramId);
            byte[] paramValueArr = paramValue.toString().getBytes(CommonConstants.DEFAULT_CHARSET_NAME);
            msgBody.writeByte(paramValueArr.length);
            msgBody.writeBytes(paramValueArr);
        } else {
            log.error("未支持的终端参数,参数项ID:{},参数信息:{}", paramId, paramValue);
            return false;
        }
        return true;
    }
}
