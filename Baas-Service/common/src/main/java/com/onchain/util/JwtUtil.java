package com.onchain.util;

import com.alibaba.fastjson.JSON;
import com.onchain.constants.ReturnCode;
import com.onchain.exception.CommonException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtUtil {

    public static String createToken(SignatureAlgorithm algorithm, Key secretKey, Claims claims, Object subject, Long expireSeconds) {
        Date now = new Date();//生成JWT的时间
        String jwtId = UUID.randomUUID().toString();
        JwtBuilder builder = Jwts.builder() //JwtBuilder，设置jwt的body
                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(jwtId)               //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setSubject(JSON.toJSONString(subject))        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setExpiration(DateUtils.addSeconds(now, Math.toIntExact(expireSeconds))) //设置过期时间
                .signWith(algorithm, secretKey); //设置签名使用的签名算法和签名使用的秘钥
        return "jwt_" + builder.compact();
    }

    public static Claims parseToken(Key secretKey, String token) throws CommonException {
        try {
            return Jwts.parser()   //得到DefaultJwtParser
                    .setSigningKey(secretKey)  //设置签名的秘钥
                    .parseClaimsJws(token.replace("jwt_", ""))
                    .getBody();
            // Jwt 会自动校验token是否过期
        } catch (ExpiredJwtException expiredJwtException) {
            //log.error("parseToken expiredJwtException: " + expiredJwtException.toString());
            throw new CommonException(ReturnCode.TOKEN_EXPIRED);
        } catch (Exception ex) {
            log.error("parseToken error: " + ex.toString());
            throw new CommonException(ReturnCode.TOKEN_NOT_MATCH);
        }
    }

}
