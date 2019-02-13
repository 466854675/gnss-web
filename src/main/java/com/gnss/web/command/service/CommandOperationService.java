package com.gnss.web.command.service;

import com.alibaba.fastjson.JSON;
import com.gnss.common.constants.CommandRequestTypeEnum;
import com.gnss.common.constants.CommandSendResultEnum;
import com.gnss.common.constants.ProtocolEnum;
import com.gnss.common.proto.CommandProto;
import com.gnss.common.proto.TerminalProto;
import com.gnss.common.service.RedisService;
import com.gnss.mqutil.producer.RabbitMessageSender;
import com.gnss.web.command.api.CommandDTO;
import com.gnss.web.command.domain.DownCommandInfo;
import com.gnss.web.common.service.IDownCommandService;
import com.gnss.web.common.utils.GnssUtils;
import com.gnss.web.event.CommandEventListener;
import com.gnss.web.event.DownCommandRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>Description: 指令操作服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-3-28
 */
@Service
@Slf4j
public class CommandOperationService {

    /**
     * 节点名称,通过rabbitmq传输时接收方可以知道消息是哪个节点发送的
     */
    private static final String NODE_NAME = "web";

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommandEventListener eventListener;

    @Autowired
    private RabbitMessageSender messageSender;

    @Autowired
    private DownCommandRegister downCommandRegister;

    /**
     * 发送指令
     *
     * @param commandDTO
     * @return
     */
    public CommandProto sendCommand(CommandDTO commandDTO) {
        ProtocolEnum protocol = commandDTO.getProtocol();
        String downCommandId = commandDTO.getDownCommandId();
        String paramsJson = JSON.toJSONString(commandDTO.getParams());
        //查询指令有无注册
        DownCommandInfo downCommandInfo = downCommandRegister.getDownCommandInfo(protocol, downCommandId);
        if (downCommandInfo == null) {
            log.error("指令发送失败,不支持的指令,终端ID:{},协议类型:{},指令类型:{},指令参数:{}", commandDTO.getTerminalId(), protocol, downCommandId, paramsJson);
            return buildCommandResponse(commandDTO, downCommandInfo, paramsJson, CommandSendResultEnum.NOT_SUPPORT);
        }
        return sendCommand(commandDTO, downCommandInfo, paramsJson);
    }

    /**
     * 发送带泛型的指令
     *
     * @param commandDTO
     * @param <T>
     * @return
     */
    public <T> CommandProto sendCommandEntity(CommandDTO<T> commandDTO) {
        ProtocolEnum protocol = commandDTO.getProtocol();
        String paramsJson = JSON.toJSONString(commandDTO.getParamsEntity());
        Class paramsEntityClass = commandDTO.getParamsEntityClass();
        //查询指令有无注册
        DownCommandInfo downCommandInfo = downCommandRegister.getDownCommandInfo(paramsEntityClass);
        if (downCommandInfo == null) {
            log.error("指令发送失败,不支持的指令,终端ID:{},协议类型:{},参数实体:{},指令参数:{}", commandDTO.getTerminalId(), protocol, paramsEntityClass, paramsJson);
            return buildCommandResponse(commandDTO, downCommandInfo, paramsJson, CommandSendResultEnum.NOT_SUPPORT);
        }
        commandDTO.setDownCommandId(downCommandInfo.getDownCommandId());
        return sendCommand(commandDTO, downCommandInfo, paramsJson);
    }

    /**
     * 发送指令
     *
     * @param commandDTO
     * @param downCommandInfo
     * @param paramsJson
     * @return
     */
    private CommandProto sendCommand(CommandDTO commandDTO, DownCommandInfo downCommandInfo, String paramsJson) {
        Long terminalId = commandDTO.getTerminalId();
        ProtocolEnum protocol = commandDTO.getProtocol();
        String downCommandId = downCommandInfo.getDownCommandId();

        //查询终端是否在线
        TerminalProto cacheTerminal = redisService.getOnlineTerminal(terminalId);
        if (cacheTerminal == null) {
            log.error("指令发送失败,终端不在线,终端ID:{},协议类型:{},指令类型:{},指令参数:{}", protocol, terminalId, downCommandId, paramsJson);
            return buildCommandResponse(commandDTO, downCommandInfo, paramsJson, CommandSendResultEnum.TERMINAL_OFFLINE);
        }

        CommandProto result = null;
        try {
            //获取透传的消息体,发送指令
            byte[] msgBody = ((IDownCommandService) (JSON.parseObject(paramsJson, downCommandInfo.getCommandParamClass()))).buildMessageBody();
            CommandProto commandProto = buildCommandResponse(commandDTO, downCommandInfo, paramsJson, CommandSendResultEnum.FAILED);
            commandProto.setMessageBody(msgBody);
            commandProto.setFromNode(NODE_NAME);
            commandProto.setToNode(cacheTerminal.getNodeName());
            commandProto.setStartTime(System.currentTimeMillis());
            messageSender.sendDownCommand(commandProto);

            //同步方式需要等待指令应答结果,同步方式直接返回成功给客户端
            if (commandDTO.getRequestType() == CommandRequestTypeEnum.SYNC) {
                log.info("同步指令发送成功到MQ,终端ID:{},协议类型:{},指令类型:{},指令参数:{}", terminalId, protocol, downCommandId, paramsJson);
                result = waitForResult(commandDTO, downCommandInfo, paramsJson);
            } else {
                log.info("异步指令发送成功到MQ,终端ID:{},协议类型:{},指令类型:{},指令参数:{}", terminalId, protocol, downCommandId, paramsJson);
                result = buildCommandResponse(commandDTO, downCommandInfo, paramsJson, CommandSendResultEnum.SUCCESS);
            }
        } catch (Exception e) {
            log.error("指令发送失败,内部服务错误,终端ID:{},协议类型:{},指令类型:{},指令参数:{}", terminalId, protocol, downCommandId, paramsJson, e);
            CommandSendResultEnum commandSendResultEnum = CommandSendResultEnum.INTERNAL_SERVER_ERROR;
            result = buildCommandResponse(commandDTO, downCommandInfo, paramsJson, commandSendResultEnum);
        }
        return result;
    }

    /**
     * 同步方式需要等待终端应答
     *
     * @param commandDTO
     * @param downCommandInfo
     * @param paramsJson
     * @return
     */
    private CommandProto waitForResult(CommandDTO commandDTO, DownCommandInfo downCommandInfo, String paramsJson) {
        //注册指令监听事件
        CompletableFuture<CommandProto> future = new CompletableFuture<>();
        long terminalId = commandDTO.getTerminalId();
        int timeout = commandDTO.getResponseTimeout();
        String downCommandId = commandDTO.getDownCommandId();
        String commandEventKey = GnssUtils.buildCommandEventKey(terminalId, downCommandId);
        eventListener.register(commandEventKey, future);

        CommandSendResultEnum commandSendResultEnum = CommandSendResultEnum.FAILED;
        try {
            //等待应答结果
            CommandProto result = future.get(timeout, TimeUnit.MILLISECONDS);
            log.info("收到指令应答,终端ID:{},指令类型:{},指令参数:{},应答结果:{}", terminalId, downCommandId, paramsJson, result);
            return result;
        } catch (TimeoutException e) {
            commandSendResultEnum = CommandSendResultEnum.TIMEOUT;
            log.error("等待指令应答超时,终端ID:{},指令类型:{},指令参数:{},等待时间:{}", terminalId, downCommandId, paramsJson, timeout, e);
        } catch (Exception e) {
            commandSendResultEnum = CommandSendResultEnum.INTERNAL_SERVER_ERROR;
            log.error("等待指令应答异常,终端ID:{},指令类型:{},指令参数:{}", terminalId, downCommandId, paramsJson, e);
        }

        //注销指令监听事件
        eventListener.unregister(commandEventKey, future);
        CommandProto result = buildCommandResponse(commandDTO, downCommandInfo, paramsJson, commandSendResultEnum);
        return result;
    }

    /**
     * 构建响应指令
     *
     * @param commandDTO
     * @param downCommandInfo
     * @param paramsJson
     * @param sendResult
     * @return
     */
    private CommandProto buildCommandResponse(CommandDTO commandDTO, DownCommandInfo downCommandInfo, String paramsJson, CommandSendResultEnum sendResult) {
        CommandProto result = new CommandProto();
        result.setTerminalId(commandDTO.getTerminalId());
        result.setRequestType(commandDTO.getRequestType());
        if (downCommandInfo != null) {
            result.setDownCommandId(downCommandInfo.getDownCommandId());
            result.setRespCommandId(downCommandInfo.getRespCommandId());
            result.setDownCommandDesc(downCommandInfo.getDesc());
        }
        result.setParams(paramsJson);
        result.setSendResult(sendResult);
        result.setTimeout(commandDTO.getResponseTimeout());
        return result;
    }

}