package com.gnss.web.command.controller;

import com.gnss.common.proto.CommandProto;
import com.gnss.web.command.api.CommandDTO;
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
 * <p>Description: 指令操作接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018/2/14
 */
@Api(tags = "指令操作")
@RestController
@RequestMapping("/api/v1/monitor/commands")
@Slf4j
public class DownCommandController {

    @Autowired
    private CommandOperationService commandOperationService;

    @ApiOperation("发送下行指令")
    @PostMapping("/sendCommand")
    public ResultDTO<CommandProto> sendCommand(@ApiParam("指令信息") @Valid @RequestBody CommandDTO commandDTO) {
        CommandProto result = commandOperationService.sendCommand(commandDTO);
        return new ResultDTO<>(result);
    }

}
