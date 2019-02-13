package com.gnss.web.command.jt808.api;

import com.gnss.common.exception.ApplicationException;
import com.gnss.web.annotations.DownCommand;
import com.gnss.web.command.api.MediaConfigDTO;
import com.gnss.web.common.constant.GnssConstants;
import com.gnss.web.common.service.IDownCommandService;
import com.gnss.web.common.service.SpringBeanService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import javax.validation.constraints.NotNull;

/**
 * <p>Description: JT808 0x9101指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-3-15
 */
@ApiModel("0x9101实时音视频传输请求")
@Data
@DownCommand(messageId = 0x9101, respMessageId = 0x0001, desc = "实时音视频传输请求")
public class Command9101Param implements IDownCommandService {

    @NotNull(message = "逻辑通道号不能为空")
    @ApiModelProperty(value = "逻辑通道号", required = true, position = 1)
    private Integer channelId;

    @NotNull(message = "数据类型不能为空")
    @ApiModelProperty(value = "数据类型", required = true, position = 2)
    private Integer avItemType;

    @NotNull(message = "码流类型不能为空")
    @ApiModelProperty(value = "码流类型", required = true, position = 3)
    private Integer streamType;

    @Override
    public byte[] buildMessageBody() throws Exception {
        byte[] msgBodyArr = null;
        ByteBuf msgBody = null;
        try {
            MediaConfigDTO mediaConfigDTO = findMediaConfig();
            byte[] serverIpArr = mediaConfigDTO.getServerIp().getBytes();
            msgBody = Unpooled.buffer(serverIpArr.length + 8);
            msgBody.writeByte(serverIpArr.length).writeBytes(serverIpArr)
                    .writeShort(mediaConfigDTO.getTcpPort()).writeShort(mediaConfigDTO.getUdpPort())
                    .writeByte(channelId).writeByte(avItemType).writeByte(streamType);
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

    //从ehcache缓存中查询流媒体配置
    private MediaConfigDTO findMediaConfig() {
        CacheManager cacheManager = SpringBeanService.getBean(CacheManager.class);
        Cache.ValueWrapper valueWrapper = cacheManager.getCache(GnssConstants.GLOBAL_CONFIG_CACHE_NAME).get(GnssConstants.GLOBAL_CONFIG_MEDIA_SERVER);
        if (valueWrapper == null) {
            throw new ApplicationException("流媒体未配置");
        }
        return (MediaConfigDTO) valueWrapper.get();
    }
}