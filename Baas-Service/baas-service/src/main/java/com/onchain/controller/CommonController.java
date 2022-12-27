package com.onchain.controller;

import com.onchain.aop.RequestLimit;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.User;
import com.onchain.entities.response.ResponseFile;
import com.onchain.entities.response.ResponsePlatformInfo;
import com.onchain.entities.response.ResponseToolInfo;
import com.onchain.exception.CommonException;
import com.onchain.service.CosService;
import com.onchain.service.JwtService;
import com.onchain.service.SmsService;
import com.onchain.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class CommonController {

    private final SmsService smsService;
    private final CosService cosService;
    private final UserService userService;
    private final JwtService jwtService;
    private final ParamsConfig paramsConfig;

    @PostMapping(value = UrlConst.SEND_VERIFY_CODE)
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    @OperLogAnnotation(description = "sendVerifyCode")
    public ResponseFormat<String> sendVerifyCode(@RequestParam @Pattern(regexp = CommonConst.PHONE_REGEX) String phoneNumber,
                                                 @RequestParam @Pattern(regexp = CommonConst.CODE_TYPE_REGEX) String codeType) throws CommonException {
        if (StringUtils.equalsAny(codeType, CommonConst.SMS_LOGIN, CommonConst.SMS_RESET)) {
            userService.checkLogin(phoneNumber);
        }
        if (StringUtils.equalsAny(codeType, CommonConst.SMS_REGISTER, CommonConst.SMS_CHANGE_PHONE) && !userService.checkUserRegister(phoneNumber)) {
            return new ResponseFormat<>(ReturnCode.USER_EXIST);
        }
        String code = smsService.sendCode(phoneNumber, codeType, CommonConst.TEN_MINUTES);
        log.info("sendLoginCode: " + phoneNumber + ", " + code);
        if (!paramsConfig.smsTest) {
            code = "";
        }
        return new ResponseFormat<>(code);
    }

    @RequestLimit
    @PostMapping(value = UrlConst.UPLOAD_FILE)
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @OperLogAnnotation(description = "uploadFile")
    public ResponseFormat<ResponseFile> uploadFile(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("fileType") String fileType,
                                                   @RequestHeader(name = CommonConst.HEADER_ACCESS_TOKEN, required = false) String accessToken) throws Exception {
        String userId = "";
        if (!StringUtils.isBlank(accessToken)) {
            User user = jwtService.parseToken(accessToken);
            userId = user.getUserId();
        }
        return new ResponseFormat<>(cosService.uploadFile(file, fileType, userId));
    }

    @RequestLimit
    @PostMapping(value = UrlConst.UPDATE_FILE)
    @ApiOperation(value = "更新上传文件", notes = "更新上传文件")
    @OperLogAnnotation(description = "updateFile")
    public ResponseFormat<ResponseFile> updateFile(@RequestParam("file") MultipartFile file,
                                                   @RequestParam String uuid,
                                                   @RequestHeader(name = CommonConst.HEADER_ACCESS_TOKEN, required = false) String accessToken) throws Exception {
        String userId = "";
        if (!StringUtils.isBlank(accessToken)) {
            User user = jwtService.parseToken(accessToken);
            userId = user.getUserId();
        }
        return new ResponseFormat<>(cosService.updateFile(file, uuid, userId));
    }

    @GetMapping(value = UrlConst.GET_PLATFORM_INFO)
    @ApiOperation(value = "获取平台主体信息", notes = "获取平台主体信息")
    @OperLogAnnotation(description = "getPlatformInfo")
    public ResponseFormat<ResponsePlatformInfo> getPlatformInfo() {
        return new ResponseFormat<>(ResponsePlatformInfo.builder().platformName(CommonConst.PLATFORM_NAME).platformPubKey(CommonConst.PLATFORM_PUB_KEY).build());
    }

    @GetMapping(value = UrlConst.GET_SERVER_TIME)
    @ApiOperation(value = "获取服务器时间", notes = "获取服务器时间")
    public ResponseFormat<List<Long>> getSyncTime() {
        List<Long> items = new ArrayList<>();
        items.add(System.currentTimeMillis());
        return new ResponseFormat<>(items);
    }

    @GetMapping(value = UrlConst.GET_RETURN_CODES)
    @ApiOperation(value = "获取所有响应码", notes = "获取所有响应码")
    public ResponseFormat<List<ResponseFormat<?>>> returnCode() {
        List<ResponseFormat<?>> codes = new ArrayList<>();
        for (ReturnCode code : ReturnCode.values()) {
            codes.add(new ResponseFormat<>(code));
        }
        return new ResponseFormat<>(codes);
    }

    @GetMapping(value = UrlConst.GET_TOOLS)
    @ApiOperation(value = "获取开发组件列表", notes = "获取开发组件列表")
    public ResponseFormat<List<ResponseToolInfo>> getTools() {
        List<ResponseToolInfo> tools = new ArrayList<>();
        tools.add(ResponseToolInfo.builder()
                .toolName("DNA2.0 Java SDK")
                .version("v1.0.1")
                .toolUrl(cosService.getTempUrl(CommonConst.DNA2_JAVA_SDK, paramsConfig.cosBucketName, CommonConst.FILE_URL_VALID_TIME))
                .build());
        tools.add(ResponseToolInfo.builder()
                .toolName("DNA2.0 Golang SDK")
                .version("v1.0.1")
                .toolUrl(cosService.getTempUrl(CommonConst.DNA2_GO_SDK, paramsConfig.cosBucketName, CommonConst.FILE_URL_VALID_TIME))
                .build());
        tools.add(ResponseToolInfo.builder()
                .toolName("DNA2.0 JavaScript SDK")
                .version("v1.0.1")
                .toolUrl(cosService.getTempUrl(CommonConst.DNA2_JS_SDK, paramsConfig.cosBucketName, CommonConst.FILE_URL_VALID_TIME))
                .build());
        tools.add(ResponseToolInfo.builder()
                .toolName("Multi Language Deployer")
                .version("v1.0.1")
                .toolUrl(cosService.getTempUrl(CommonConst.MULTI_LAN_DEPLOYER, paramsConfig.cosBucketName, CommonConst.FILE_URL_VALID_TIME))
                .build());
        return new ResponseFormat<>(tools);
    }


}
