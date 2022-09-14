package com.onchain.filter;

import com.alibaba.fastjson.JSON;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.User;
import com.onchain.exception.CommonException;
import com.onchain.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(0)
public class AuthFilter implements GlobalFilter {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("****** come in GlobalFilter: " + new Date());

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        String uri = request.getURI().getPath();

        //  检查白名单, 不需要accessToken
        if (StringUtils.equalsAny(uri, UrlConst.NOT_LOGIN_URLS)) {
            return chain.filter(exchange);
        }

        // 检查Jwt Token
        String accessToken = request.getHeaders().getFirst(CommonConst.HEADER_ACCESS_TOKEN);

        if (StringUtils.isBlank(accessToken)) {
            log.info("JwtController.checkAccess req.getHeader.accessToken is empty");
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return getVoidMono(serverHttpResponse, ReturnCode.ACCESS_TOKEN_FAIL);
        }

        try {
            User user = jwtService.parseToken(accessToken);
            jwtService.checkRole(uri, user.getRole());
        } catch (CommonException e) {
            return getVoidMono(serverHttpResponse, e.getReturnCode());
        }

        return chain.filter(exchange);
    }

    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, ReturnCode responseCodeEnum) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        ResponseFormat responseResult = new ResponseFormat(responseCodeEnum);
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONString(responseResult).getBytes());
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }
}
