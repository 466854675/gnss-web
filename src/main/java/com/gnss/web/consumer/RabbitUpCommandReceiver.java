package com.gnss.web.consumer;

import com.gnss.common.constants.CommandRequestTypeEnum;
import com.gnss.common.proto.CommandProto;
import com.gnss.web.common.constant.RabbitConstants;
import com.gnss.web.common.utils.GnssUtils;
import com.gnss.web.event.EventPublisher;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Description: rabbitmq上行指令订阅</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
@Component
@RabbitListener(queues = RabbitConstants.UP_COMMAND_QUEUE)
@Slf4j
public class RabbitUpCommandReceiver {

    @Autowired
    private EventPublisher eventPublisher;

    @RabbitHandler
    public void handleUpCommand(CommandProto upCommand, Channel channel, Message message) throws Exception {
        log.info("收到上行指令：{}", upCommand);
        try {
            //异步发送的响应结果
            if (upCommand.getRequestType() == CommandRequestTypeEnum.ASYNC) {
                return;
            }

            //同步发送的响应结果
            long terminalId = upCommand.getTerminalId();
            String downCommandId = upCommand.getDownCommandId();
            String commandEventKey = GnssUtils.buildCommandEventKey(terminalId, downCommandId);
            eventPublisher.publishCommandEvent(commandEventKey, upCommand);
        } catch (Exception e) {
            log.error("更新指令操作日志异常{}", upCommand, e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}