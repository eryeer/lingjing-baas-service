package com.onchain.service;

import com.alibaba.fastjson.JSON;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.constants.UrlConst;
import com.onchain.entities.dao.User;
import com.onchain.exception.CommonException;
import com.onchain.util.JwtUtil;
import com.onchain.util.SHA;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret.code}")
    private String secretCode;
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";
    private static final String TYPE = "tokenType";

    // 签名私钥
    private SecretKey createSecretKey() {
        byte[] encodedKey = SHA.SHA256(secretCode).getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(encodedKey, ""); // 签名算法不在key中指定
    }

    public User parseToken(String token) throws CommonException {
        SecretKey secretKey = this.createSecretKey();
        Claims body = JwtUtil.parseToken(secretKey, token);
        if (!StringUtils.equals(body.get(TYPE).toString(), TYPE_ACCESS)) {
            throw new CommonException(ReturnCode.ACCESS_TOKEN_FAIL);
        }
        return JSON.parseObject(body.getSubject(), User.class);
    }

    /**
     * 验证用户角色权限
     *
     * @param url  请求地址
     * @param role 用户角色
     */
    public void checkRole(String url, String role) throws CommonException {
        if (StringUtils.equalsAnyIgnoreCase(url, UrlConst.COMMON_URLS)) {
            return;
        }

        // PM
        if (StringUtils.equals(role, CommonConst.PM)) {
            if (StringUtils.equalsAnyIgnoreCase(url, UrlConst.PM_URLS)) {
                return;
            }
            throw new CommonException(ReturnCode.USER_ROLE_ERROR);
        }

        // 普通用户
        if (StringUtils.equalsAny(role, CommonConst.CU)) {
            if (StringUtils.equalsAnyIgnoreCase(url, UrlConst.CU_URLS)) {
                return;
            }
            throw new CommonException(ReturnCode.USER_ROLE_ERROR);
        }

        // 其他不支持的地址报错
        log.error("checkRole USER_URL_ERROR: " + url);
        throw new CommonException(ReturnCode.USER_URL_ERROR);
    }
}
