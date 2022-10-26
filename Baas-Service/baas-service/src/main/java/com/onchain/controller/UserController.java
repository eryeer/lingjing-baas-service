package com.onchain.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.User;
import com.onchain.entities.request.*;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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

    @PostMapping(value = UrlConst.CHANGE_PHONE_NUMBER)
    @ApiOperation(value = "修改用户手机号", notes = "修改用户手机号")
    @OperLogAnnotation(description = "changePhoneNumber")
    public ResponseFormat<?> changePhoneNumber(@Valid @RequestBody RequestChangePhoneNumber request,
                                               @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        userService.changePhoneNumber(user.getUserId(), request.getPhoneNumber(), request.getCode());
        return new ResponseFormat<>();
    }

    @PostMapping(value = UrlConst.RESET_PASSWORD)
    @ApiOperation(value = "忘记密码", notes = "忘记密码")
    @OperLogAnnotation(description = "resetPassword")
    public ResponseFormat<?> resetPassword(@Valid @RequestBody RequestResetPassword request) throws CommonException {
        userService.resetPassword(request.getPhoneNumber(), request.getNewPassword(), request.getCode());
        return new ResponseFormat<>();
    }

    @PostMapping(value = UrlConst.SUBMIT_USER_KYC)
    @ApiOperation(value = "提交用户KYC", notes = "提交用户KYC")
    @OperLogAnnotation(description = "submitUserKyc")
    public ResponseFormat<?> submitUserKyc(@Valid @RequestBody RequestSubmitUserKyc request,
                                           @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        userService.submitUserKyc(request, user.getUserId());
        return new ResponseFormat<>();
    }

    @PostMapping(value = UrlConst.APPROVE_USER_KYC)
    @ApiOperation(value = "审批用户KYC", notes = "审批用户KYC")
    @OperLogAnnotation(description = "approveUser")
    public ResponseFormat<?> approveUser(@Valid @RequestBody RequestUserApprove request,
                                         @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        if (!StringUtils.equals(CommonConst.APPROVED, request.getApproveStatus()) && StringUtils.isBlank(request.getApproveFeedback())) {
            return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED, "approveFeedback should not be blank when not approve.");
        }

        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }

        userService.approveUser(request.getUserId(), request.getApproveStatus(), request.getApproveFeedback(), request.getKycType());
        return new ResponseFormat<>();
    }

    @GetMapping(value = UrlConst.GET_USER_LIST)
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @OperLogAnnotation(description = "getUserList")
    public ResponseFormat<PageInfo<ResponseUser>> getUserList(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                              @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                              @RequestParam(required = true) @NotBlank String approveStatus,
                                                              @RequestParam(required = false) String userType,
                                                              @RequestParam(required = false) String phoneNumber,
                                                              @RequestParam(required = false) String userName,
                                                              @RequestParam(required = false) String companyName,
                                                              @RequestParam(required = false) String idNumber,
                                                              @RequestParam(required = false) String uniSocialCreditCode,
                                                              @RequestParam(required = false) Long startApplyTime,
                                                              @RequestParam(required = false) Long endApplyTime,
                                                              @RequestParam(required = false) Long startApproveTime,
                                                              @RequestParam(required = false) Long endApproveTime,
                                                              @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }

        PageInfo<ResponseUser> result = userService.getUserList(pageNumber, pageSize, approveStatus, userType, userName, companyName, phoneNumber, idNumber, uniSocialCreditCode, startApplyTime, endApplyTime, startApproveTime, endApproveTime);
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.GET_USER_KYC_RECORD_LIST)
    @ApiOperation(value = "获取用户KYC记录列表", notes = "获取用户KYC记录列表")
    @OperLogAnnotation(description = "getUserKycRecordList")
    public ResponseFormat<PageInfo<ResponseUser>> getUserKycRecordList(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                                       @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                                       @RequestParam(required = false) String approveStatus,
                                                                       @RequestParam(required = false) String userType,
                                                                       @RequestParam(required = false) String kycType,
                                                                       @RequestParam(required = false) String userName,
                                                                       @RequestParam(required = false) String companyName,
                                                                       @RequestParam(required = false) String phoneNumber,
                                                                       @RequestParam(required = false) String idNumber,
                                                                       @RequestParam(required = false) String uniSocialCreditCode,
                                                                       @RequestParam(required = false) Long startApplyTime,
                                                                       @RequestParam(required = false) Long endApplyTime,
                                                                       @RequestParam(required = false) Long startApproveTime,
                                                                       @RequestParam(required = false) Long endApproveTime,
                                                                       @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {

        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }

        PageInfo<ResponseUser> result = userService.getUserKycRecordList(pageNumber, pageSize, approveStatus, userType, kycType, userName, companyName, phoneNumber, idNumber, uniSocialCreditCode, startApplyTime, endApplyTime, startApproveTime, endApproveTime);
        return new ResponseFormat<>(result);
    }
}
