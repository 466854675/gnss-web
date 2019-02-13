package com.gnss.web.event;

import com.gnss.common.constants.CommonConstants;
import com.gnss.common.constants.ProtocolEnum;
import com.gnss.common.exception.ApplicationException;
import com.gnss.web.annotations.DownCommand;
import com.gnss.web.command.domain.DownCommandInfo;
import com.gnss.web.common.service.IDownCommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 注册下行指令</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
@Component
@Slf4j
@Order(value = 2)
public class DownCommandRegister implements CommandLineRunner {

    private Map<ProtocolEnum, Map<String, DownCommandInfo>> protocolMap = new HashMap<>();

    private Map<Class, DownCommandInfo> classMap = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        Reflections reflections = new Reflections("com.gnss.web.command");
        reflections.getTypesAnnotatedWith(DownCommand.class).stream()
                .filter(clazz -> IDownCommandService.class.isAssignableFrom(clazz))
                .forEach(clazz -> {
                    registerDownCommand(clazz);
                });
    }

    private void registerDownCommand(Class<?> clazz) {
        DownCommand annotation = clazz.getAnnotation(DownCommand.class);
        ProtocolEnum protocol = annotation.protocol();
        int messageId = annotation.messageId();
        String strMessageId = messageId == CommonConstants.DEFAULT_MESSAGE_ID ? annotation.strMessageId() : StringUtils.leftPad(Integer.toHexString(messageId).toUpperCase(), 4, '0');
        int respMessageId = annotation.respMessageId();
        String strRespMessageId = respMessageId == CommonConstants.DEFAULT_MESSAGE_ID ? annotation.strRespMessageId() : StringUtils.leftPad(Integer.toHexString(respMessageId).toUpperCase(), 4, '0');
        String desc = annotation.desc();
        DownCommandInfo downCommandInfo = new DownCommandInfo();
        downCommandInfo.setDownCommandId(strMessageId);
        downCommandInfo.setRespCommandId(strRespMessageId);
        downCommandInfo.setDesc(desc);
        downCommandInfo.setCommandParamClass(clazz);
        Map<String, DownCommandInfo> downCommandInfoMap = protocolMap.computeIfAbsent(protocol, k -> new HashMap<>());
        if (downCommandInfoMap.containsKey(strMessageId)) {
            String exceptionMsg = String.format("注册的下行指令已存在,协议类型:%s,指令类型:%s,指令描述:%s,响应指令:%s,指令参数:%s", protocol, strMessageId, desc, strRespMessageId, clazz.getName());
            throw new ApplicationException(exceptionMsg);
        }
        downCommandInfoMap.put(strMessageId, downCommandInfo);
        classMap.put(clazz, downCommandInfo);
        log.info("注册下行指令,协议类型:{},指令类型:{},指令描述:{},响应指令:{},指令参数:{}", protocol, strMessageId, desc, strRespMessageId, clazz.getName());
    }

    public DownCommandInfo getDownCommandInfo(ProtocolEnum protocolType, String downCommandId) {
        Map<String, DownCommandInfo> downCommandInfoMap = protocolMap.get(protocolType);
        if (downCommandInfoMap == null) {
            return null;
        }
        return downCommandInfoMap.getOrDefault(downCommandId, null);
    }

    public DownCommandInfo getDownCommandInfo(Class commandParam) {
        return classMap.get(commandParam);
    }

}