package com.onchain.service;

import com.alibaba.fastjson.JSON;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.User;
import com.onchain.entities.response.ResponseLogin;
import com.onchain.exception.CommonException;
import com.onchain.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
@Slf4j
public class JwtService {
    private static final long ACCESS_EXPIRE_SECONDS = 5 * 60; // 5 minutes
    private static final long REFRESH_EXPIRE_SECONDS = 7 * 24 * 60 * 60; // 7 days
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";
    @Value("${jwt.secret.code}")
    private String secretCode;

    @Autowired
    private RedisService redisService;

    // 签名私钥
    private SecretKey createSecretKey() {
        byte[] encodedKey = secretCode.getBytes();
        return new SecretKeySpec(encodedKey, ""); // 签名算法不在key中指定
    }

    public String createToken(User user, String tokenType) {
        String userId = user.getUserId();
        log.info("JwtService.createToken userId={},tokenType={}", userId, tokenType);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法
        Claims claims = Jwts.claims();//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        claims.put("tokenType", tokenType);
        SecretKey secretKey = createSecretKey();//生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        // 删除敏感信息
        user.setPassword("");
        user.setIdNumber("");
        //设置过期时间
        Long expSeconds = ACCESS_EXPIRE_SECONDS;
        if (StringUtils.equals(tokenType, TYPE_REFRESH)) {
            expSeconds = REFRESH_EXPIRE_SECONDS;
        }
        String newToken = JwtUtil.createToken(SignatureAlgorithm.HS256, secretKey, claims, user, expSeconds);
        log.info("JwtService.createToken newToken={}", newToken);
        return newToken;
    }

    public ResponseLogin login(User user) {
        log.info("JwtService.login user={}", user);
        String accessToken = createToken(user, TYPE_ACCESS);
        String refreshToken = createToken(user, TYPE_REFRESH);
        redisService.setValueEX(CommonConst.USER_REFRESH_TOKEN + user.getUserId(), refreshToken, REFRESH_EXPIRE_SECONDS);
        redisService.setValueEX(CommonConst.USER_AUTHORITY + user.getUserId(), user.getRole(), REFRESH_EXPIRE_SECONDS);
        log.info("JwtService.login accessToken={},refreshToken={}", accessToken, refreshToken);
        return new ResponseLogin(accessToken, refreshToken);
    }

    public String refresh(String refreshToken) throws CommonException {
        log.info("JwtService.refresh refreshDto={}", refreshToken);
        User userEntity = parseToken(refreshToken);
        String refreshTokenServer = redisService.getValue(CommonConst.USER_REFRESH_TOKEN + userEntity.getUserId());
        log.info("JwtService.refresh redisService.getValue refreshTokenServer={}", refreshTokenServer);
        if (!StringUtils.equals(refreshToken, refreshTokenServer)) {
            throw new CommonException(ReturnCode.REFRESH_TOKEN_FAIL);
        }
        return createToken(userEntity, TYPE_ACCESS);
    }

    public Boolean logout(String userId) {
        log.info("JwtService.logout userId={}", userId);
        redisService.delete(CommonConst.USER_REFRESH_TOKEN + userId);
        redisService.delete(CommonConst.USER_AUTHORITY + userId);
        return true;
    }

    public User parseToken(String token) throws CommonException {
        SecretKey secretKey = this.createSecretKey();
        Claims body = JwtUtil.parseToken(secretKey, token);
        return JSON.parseObject(body.getSubject(), User.class);
    }
}
