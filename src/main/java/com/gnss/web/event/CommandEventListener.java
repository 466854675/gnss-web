package com.gnss.web.event;

import com.gnss.common.proto.CommandProto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>Description: 指令事件监听</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
@Component
@Slf4j
public class CommandEventListener {
    private ConcurrentHashMap<String, CopyOnWriteArrayList<CompletableFuture<CommandProto>>> subscriberMap = new ConcurrentHashMap<>();

    @EventListener
    public void onApplicationEvent(CommandEvent event) {
        String commandEventKey = event.getCommandEventKey();
        CommandProto message = event.getMessage();
        subscriberMap.computeIfPresent(commandEventKey, (k, v) -> {
            v.forEach(future -> {
                future.complete(message);
            });
            return null;
        });
        log.info("广播指令事件,commandEventKey:{},message:{}", commandEventKey, message);
    }

    /**
     * 注册指令事件监听
     *
     * @param commandEventKey
     * @param future
     */
    public void register(String commandEventKey, CompletableFuture<CommandProto> future) {
        subscriberMap.computeIfAbsent(commandEventKey, k -> new CopyOnWriteArrayList<>()).add(future);
        log.info("注册指令事件监听,commandEventKey:{},size:{}", commandEventKey, subscriberMap.get(commandEventKey).size());
    }

    /**
     * 注销指令事件监听
     *
     * @param commandEventKey
     * @param future
     */
    public void unregister(String commandEventKey, CompletableFuture<CommandProto> future) {
        subscriberMap.computeIfPresent(commandEventKey, (k, v) -> {
            v.remove(future);
            if (v.isEmpty()) {
                log.info("删除指令事件监听,commandEventKey:{}", commandEventKey);
                return null;
            }
            log.info("注销指令事件监听,commandEventKey:{},size:{}", commandEventKey, v.size());
            return v;
        });
    }
}
