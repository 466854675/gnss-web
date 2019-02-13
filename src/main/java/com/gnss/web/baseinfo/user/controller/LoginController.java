package com.gnss.web.baseinfo.user.controller;

import com.gnss.common.constants.RoleTypeEnum;
import com.gnss.common.security.AuthTokenDetails;
import com.gnss.common.utils.JwtUtils;
import com.gnss.web.baseinfo.user.api.LoginDTO;
import com.gnss.web.common.api.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * <p>Description: 登录接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2019-01-23
 */
@Api(tags = "登录接口")
@RestController
@RequestMapping("/api/v1/baseinfo/users")
@Slf4j
public class LoginController {

    @Value("${gnss.token.timeout}")
    private long tokenTimeout;

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResultDTO<String> login(@ApiParam("登录信息") @Valid @RequestBody LoginDTO loginDTO) {
        log.info("登录,用户名:{},密码:{}", loginDTO.getAccount(), loginDTO.getPassword());
        AuthTokenDetails authTokenDetails = new AuthTokenDetails();
        authTokenDetails.setUserId(1L);
        authTokenDetails.setOrganizationId(1L);
        authTokenDetails.setRoleId(1L);
        authTokenDetails.setRoleType(RoleTypeEnum.ADMIN);
        authTokenDetails.setExpirationDate(new Date(System.currentTimeMillis() + tokenTimeout));
        String token = JwtUtils.generateToken(authTokenDetails);
        return new ResultDTO<>(token);
    }
}
