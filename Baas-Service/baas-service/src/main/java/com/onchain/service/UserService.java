package com.onchain.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.User;
import com.onchain.entities.request.RequestLogin;
import com.onchain.entities.request.RequestRegister;
import com.onchain.entities.response.ResponseLogin;
import com.onchain.entities.response.ResponseUser;
import com.onchain.exception.CommonException;
import com.onchain.mapper.CosFileMapper;
import com.onchain.mapper.LoginLogMapper;
import com.onchain.mapper.UserMapper;
import com.onchain.util.CommonUtil;
import com.onchain.util.ECCUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final SmsService smsService;
    private final JwtService jwtService;
    private final LoginLogMapper loginLogMapper;
    private final CosFileMapper cosFileMapper;
    private final CosService cosService;

    public User getUserByPhoneNumber(String phoneNumber) {
        return userMapper.getUserByPhoneNumber(phoneNumber);
    }

    // 检查用户手机可用于登录
    public User checkLogin(String phoneNumber) throws CommonException {
        User user = userMapper.getUserByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new CommonException(ReturnCode.USER_UNREGISTER);
        }

        if (StringUtils.equals(CommonConst.INACTIVE, user.getStatus())) {
            throw new CommonException(ReturnCode.USER_INACTIVE);
        }

        // 审批通过用户才能登录
        if (!StringUtils.equals(CommonConst.APPROVED, user.getApproveStatus())) {
            throw new CommonException(ReturnCode.USER_PENDING);
        }

        // 手机号已注册并处于启用状态
        return user;
    }

    // 注册用户
    @Transactional(rollbackFor = Exception.class)
    public String Register(RequestRegister requestRegister) throws CommonException {
        // 验证注册条件
        if (!checkUserRegister(requestRegister.getPhoneNumber())) {
            throw new CommonException(ReturnCode.USER_REGISTER_ERROR);
        }
        // 验证注册验证码
        if (!smsService.verifyCode(requestRegister.getPhoneNumber(), CommonConst.SMS_REGISTER, requestRegister.getRegisterCode())) {
            throw new CommonException(ReturnCode.REGISTER_CODE_ERROR);
        }
        // 失效验证码
        smsService.disableCode(requestRegister.getPhoneNumber(), CommonConst.SMS_REGISTER, requestRegister.getRegisterCode());
        // 加密用户密码
        String encodedPassword = encodePassword(requestRegister.getPassword());

        User user = createDefaultUser();
        BeanUtils.copyProperties(requestRegister, user);
        user.setPassword(encodedPassword);
        userMapper.insertUser(user);
        cosFileMapper.markFileUsed(Arrays.asList(user.getIdaFileUuid(), user.getIdbFileUuid(), user.getLegalPersonIdaFileUuid(), user.getLegalPersonIdbFileUuid(),
                user.getBusinessLicenseCopyFileUuid(), user.getBusinessLicenseFileUuid()));
        return user.getUserId();
    }

    private String encodePassword(String password) {
        return ECCUtils.SHA256(ECCUtils.SHA256(password));
    }

    private Boolean checkPassword(String userId, String password) {
        return userMapper.checkPassword(userId, encodePassword(password));
    }

    // 校验注册手机号和角色, 已拒绝的手机号可重新注册
    public Boolean checkUserRegister(String phoneNumber) {
        User user = userMapper.getUserByPhoneNumber(phoneNumber);
        return user == null;
    }

    private User createDefaultUser() {
        return User.builder()
                .userId(CommonUtil.getUUID())
                .userName("")
                .phoneNumber("")
                .password("")
                .role(CommonConst.CU)
                .idNumber("")
                .companyName("")
                .uniSocialCreditCode("")
                .legalPersonName("")
                .legalPersonIdn("")
                .applyTime(new Date())
                .approveStatus(CommonConst.PENDING)
                .approveFeedback("")
                .businessLicenseFileUuid("")
                .businessLicenseCopyFileUuid("")
                .legalPersonIdaFileUuid("")
                .legalPersonIdbFileUuid("")
                .idaFileUuid("")
                .idbFileUuid("")
                .build();
    }

    // 用户登录
    @Transactional(rollbackFor = Exception.class)
    public ResponseLogin login(RequestLogin request) throws CommonException {
        User user = checkLogin(request.getPhoneNumber());
        // 验证登录验证码
        if (!smsService.verifyCode(request.getPhoneNumber(), CommonConst.SMS_LOGIN, request.getLoginCode())) {
            throw new CommonException(ReturnCode.LOGIN_CODE_ERROR);
        }
        // 验证密码
        if (!checkPassword(user.getUserId(), request.getPassword())) {
            throw new CommonException(ReturnCode.USER_PASSWORD_ERROR);
        }
        // 失效验证码
        smsService.disableCode(request.getPhoneNumber(), CommonConst.SMS_LOGIN, request.getLoginCode());
        //登录日志
        loginLogMapper.insertLog(user.getUserId());

        return jwtService.login(user);
    }

    public String refreshToken(String refreshToken) throws CommonException {
        User user = jwtService.parseToken(refreshToken);
        // 校验当前用户状态，停用或删除不能获取token
        User record = userMapper.getUserByPhoneNumber(user.getPhoneNumber());
        if (record == null) {
            throw new CommonException(ReturnCode.USER_NOT_EXIST);
        }
        if (!StringUtils.equals(CommonConst.ACTIVE, record.getStatus())) {
            throw new CommonException(ReturnCode.USER_INACTIVE);
        }
        if (!StringUtils.equals(CommonConst.APPROVED, record.getApproveStatus())) {
            throw new CommonException(ReturnCode.USER_PENDING);
        }
        return jwtService.refresh(refreshToken);
    }

    public void logout(String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        jwtService.logout(user.getUserId());
    }

    public PageInfo<ResponseUser> getUserList(Integer pageNumber, Integer pageSize, Boolean isPending, String userId, String userName, String companyName, String phoneNumber, Date start, Date end) {
        PageHelper.startPage(pageNumber, pageSize);
        List<ResponseUser> users = userMapper.getUserList(isPending, userId, userName, companyName, phoneNumber, start, end);
        for (ResponseUser user : users) {
            user.setIdaFile(cosService.getCosFile(user.getIdaFileUuid()));
            user.setIdbFile(cosService.getCosFile(user.getIdbFileUuid()));
            user.setBusinessLicenseFile(cosService.getCosFile(user.getBusinessLicenseFileUuid()));
            user.setBusinessLicenseCopyFile(cosService.getCosFile(user.getBusinessLicenseCopyFileUuid()));
            user.setLegalPersonIdaFile(cosService.getCosFile(user.getLegalPersonIdaFileUuid()));
            user.setLegalPersonIdbFile(cosService.getCosFile(user.getLegalPersonIdbFileUuid()));
        }
        return new PageInfo<>(users);
    }

    public ResponseUser getUserById(String userId) throws CommonException {
        ResponseUser user = userMapper.getResponseUserById(userId);
        if (user == null) {
            throw new CommonException(ReturnCode.USER_NOT_EXIST);
        }

        user.setIdaFile(cosService.getCosFile(user.getIdaFileUuid()));
        user.setIdbFile(cosService.getCosFile(user.getIdbFileUuid()));
        user.setBusinessLicenseFile(cosService.getCosFile(user.getBusinessLicenseFileUuid()));
        user.setBusinessLicenseCopyFile(cosService.getCosFile(user.getBusinessLicenseCopyFileUuid()));
        user.setLegalPersonIdaFile(cosService.getCosFile(user.getLegalPersonIdaFileUuid()));
        user.setLegalPersonIdbFile(cosService.getCosFile(user.getLegalPersonIdbFileUuid()));
        return user;
    }

    public void changePassword(String userId, String oldPassword, String newPassword) throws CommonException {
        if (!checkPassword(userId, oldPassword)) {
            throw new CommonException(ReturnCode.USER_ORIGINAL_PASSWORD_ERROR);
        }
        userMapper.updatePassword(userId, encodePassword(newPassword));
    }

    public void approveUser(String userId, Boolean isApproved, String feedback) throws CommonException {
        String approveStatus = isApproved ? CommonConst.APPROVED : CommonConst.REJECTED;
        User user = User.builder().userId(userId).approveStatus(approveStatus).approveFeedback(feedback).build();
        userMapper.approveUser(user);
    }

}
