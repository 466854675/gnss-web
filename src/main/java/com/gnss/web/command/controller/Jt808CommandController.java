package com.gnss.web.command.controller;

import com.gnss.common.constants.ProtocolEnum;
import com.gnss.common.proto.CommandProto;
import com.gnss.web.command.api.CommandDTO;
import com.gnss.web.command.jt808.api.Command8103Param;
import com.gnss.web.command.jt808.api.Command8104Param;
import com.gnss.web.command.jt808.api.Command8105Param;
import com.gnss.web.command.jt808.api.Command8106Param;
import com.gnss.web.command.jt808.api.Command8107Param;
import com.gnss.web.command.jt808.api.Command8201Param;
import com.gnss.web.command.jt808.api.Command8202Param;
import com.gnss.web.command.jt808.api.Command8300Param;
import com.gnss.web.command.jt808.api.Command8400Param;
import com.gnss.web.command.jt808.api.Command9003Param;
import com.gnss.web.command.jt808.api.Command9101Param;
import com.gnss.web.command.jt808.api.Command9102Param;
import com.gnss.web.command.service.CommandOperationService;
import com.gnss.web.common.api.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>Description: JT808指令操作接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018/2/14
 */
@Api(tags = "JT808指令操作")
@RestController
@RequestMapping("/api/v1/monitor/commands/jt808")
@Slf4j
public class Jt808CommandController {

    @Autowired
    private CommandOperationService commandOperationService;

    @ApiOperation("设置终端参数")
    @PostMapping("/sendCommand8103")
    public ResultDTO<CommandProto> sendCommand8103(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command8103Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("查询终端参数")
    @PostMapping("/sendCommand8104")
    public ResultDTO<CommandProto> sendCommand8104(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command8104Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("终端控制")
    @PostMapping("/sendCommand8105")
    public ResultDTO<CommandProto> sendCommand8105(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command8105Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("查询指定终端参数")
    @PostMapping("/sendCommand8106")
    public ResultDTO<CommandProto> sendCommand8106(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command8106Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("查询终端属性")
    @PostMapping("/sendCommand8107")
    public ResultDTO<CommandProto> sendCommand8107(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command8107Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("位置信息查询")
    @PostMapping("/sendCommand8201")
    public ResultDTO<CommandProto> sendCommand8201(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command8201Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("临时位置跟踪控制")
    @PostMapping("/sendCommand8202")
    public ResultDTO<CommandProto> sendCommand8202(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command8202Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("文本信息下发参数")
    @PostMapping("/sendCommand8300")
    public ResultDTO<CommandProto> sendCommand8300(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command8300Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("电话回拨参数")
    @PostMapping("/sendCommand8400")
    public ResultDTO<CommandProto> sendCommand8400(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command8400Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("查询终端音视频属性")
    @PostMapping("/sendCommand9003")
    public ResultDTO<CommandProto> sendCommand9003(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command9003Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("实时音视频传输请求")
    @PostMapping("/sendCommand9101")
    public ResultDTO<CommandProto> sendCommand9101(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command9101Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    @ApiOperation("音视频实时传输控制")
    @PostMapping("/sendCommand9102")
    public ResultDTO<CommandProto> sendCommand9102(@ApiParam("指令信息") @Valid @RequestBody CommandDTO<Command9102Param> commandDTO) {
        return sendJt808Command(commandDTO);
    }

    private ResultDTO<CommandProto> sendJt808Command(CommandDTO<?> commandDTO) {
        commandDTO.setProtocol(ProtocolEnum.JT808);
        CommandProto result = commandOperationService.sendCommandEntity(commandDTO);
        return new ResultDTO<>(result);
    }

}