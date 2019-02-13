package com.gnss.web.event;

import com.gnss.common.proto.CommandProto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * <p>Description: 指令事件</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
@Getter
@Setter
public class CommandEvent extends ApplicationEvent {

    private String commandEventKey;

    private CommandProto message;

    public CommandEvent(Object source, String commandEventKey, CommandProto message) {
        super(source);
        this.commandEventKey = commandEventKey;
        this.message = message;
    }
}
