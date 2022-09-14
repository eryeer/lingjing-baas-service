package com.onchain.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.User;
import com.onchain.entities.request.RequestChangePassword;
import com.onchain.entities.request.RequestLogin;
import com.onchain.entities.request.RequestRegister;
import com.onchain.entities.request.RequestUserApprove;
import com.onchain.entities.response.ResponseLogin;
import com.onchain.entities.response.ResponseUser;
import com.onchain.exception.CommonException;
import com.onchain.service.JwtService;
import com.onchain.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping(value = UrlConst.REGISTER_USER)
    @ApiOperation(value = "注册用户", notes = "注册用户")
    @OperLogAnnotation(description = "registerUser")
    public ResponseFormat<?> registerUser(@Valid @RequestBody RequestRegister requestRegister) throws CommonException {
        return new ResponseFormat<>(userService.Register(requestRegister));
    }

    @GetMapping(value = UrlConst.CHECK_USER_REGISTER)
    @ApiOperation(value = "注册校验", notes = "注册校验")
    @OperLogAnnotation(description = "checkUserRegister")
    public ResponseFormat<Boolean> checkUserRegister(@RequestParam @Pattern(regexp = CommonConst.PHONE_REGEX) String phoneNumber) {
        Boolean result = userService.checkUserRegister(phoneNumber);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.LOGIN)
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @OperLogAnnotation(description = "login")
    public ResponseFormat<ResponseLogin> login(@Valid @RequestBody RequestLogin request) throws CommonException {
        return new ResponseFormat<>(userService.login(request));
    }

    @GetMapping(value = UrlConst.REFRESH_TOKEN)
    @ApiOperation(value = "用户token刷新", notes = "用户token刷新")
    @OperLogAnnotation(description = "refreshToken")
    public ResponseFormat<String> refreshToken(@RequestParam String refreshToken) throws CommonException {
        String accessToken = userService.refreshToken(refreshToken);
        return new ResponseFormat<>(accessToken);
    }

    @GetMapping(value = UrlConst.LOGOUT)
    @ApiOperation(value = "用户登出", notes = "用户登出")
    @OperLogAnnotation(description = "logout")
    public ResponseFormat<?> logout(@RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        userService.logout(accessToken);
        return new ResponseFormat<>();
    }

    @GetMapping(value = UrlConst.GET_USER_BY_ID)
    @ApiOperation(value = "获取用户详细信息", notes = "获取用户详细信息")
    @OperLogAnnotation(description = "getUserById")
    public ResponseFormat<ResponseUser> getUserById(@RequestParam String userId,
                                                    @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(userId, user.getUserId()) && !StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR, "Wrong userId or Role.");
        }

        ResponseUser result = userService.getUserById(userId);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.CHANGE_PASSWORD)
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @OperLogAnnotation(description = "changePassword")
    public ResponseFormat<?> changePassword(@Valid @RequestBody RequestChangePassword request) throws CommonException {
        if (!StringUtils.equals(request.getNewPassword(), request.getNewPasswordConfirm())) {
            return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED);
        }

        userService.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
        return new ResponseFormat<>();
    }

    @PostMapping(value = UrlConst.APPROVE_USER)
    @ApiOperation(value = "审批用户", notes = "审批用户")
    @OperLogAnnotation(description = "approveUser")
    public ResponseFormat<?> approveUser(@Valid @RequestBody RequestUserApprove request,
                                         @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        if ((!request.getIsApproved()) && StringUtils.isBlank(request.getApproveFeedback())) {
            return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED, "approveFeedback should not be blank when reject.");
        }

        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }

        userService.approveUser(request.getUserId(), request.getIsApproved(), request.getApproveFeedback());
        return new ResponseFormat<>();
    }

    @GetMapping(value = UrlConst.GET_USER_LIST)
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @OperLogAnnotation(description = "getUserList")
    public ResponseFormat<PageInfo<ResponseUser>> getUserList(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                              @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                              @RequestParam(required = true) Boolean isPending,
                                                              @RequestParam(required = false) String userId,
                                                              @RequestParam(required = false) String userName,
                                                              @RequestParam(required = false) String companyName,
                                                              @RequestParam(required = false) String phoneNumber,
                                                              @RequestParam(required = false) Long startTime,
                                                              @RequestParam(required = false) Long endTime,
                                                              @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        if (isPending == null) {
            return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED, "isPending shouldn't be null");
        }

        Date start = null;
        Date end = null;
        if (startTime != null) {
            if (!(startTime <= endTime)) {
                return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED, "startTime should before endTime");
            }
            start = new Date(startTime);
            end = new Date(endTime);
        }

        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }

        PageInfo<ResponseUser> result = userService.getUserList(pageNumber, pageSize, isPending, userId, userName, companyName, phoneNumber, start, end);
        return new ResponseFormat<>(result);
    }
}
