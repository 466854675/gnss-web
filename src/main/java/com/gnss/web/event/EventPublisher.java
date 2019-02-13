package com.gnss.web.event;

import com.gnss.common.proto.CommandProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 事件发布</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
@Component
public class EventPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    public void publishCommandEvent(String downCommandId, CommandProto message) {
        applicationContext.publishEvent(new CommandEvent(this, downCommandId, message));
    }

}
