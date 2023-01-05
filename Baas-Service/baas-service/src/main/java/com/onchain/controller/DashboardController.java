package com.onchain.controller;

import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.CommonConst;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.User;
import com.onchain.entities.response.ResponseDashboardSummary;
import com.onchain.service.ChainService;
import com.onchain.service.JwtService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class DashboardController {

    private final ChainService chainService;
    private final JwtService jwtService;

    @GetMapping(value = UrlConst.GET_DASHBOARD_SUMMARY)
    @ApiOperation(value = "获取燃料签约记录列表", notes = "获取燃料签约记录列表")
    @OperLogAnnotation(description = "getDashboardSummary")
    public ResponseFormat<ResponseDashboardSummary> getDashboardSummary(@RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        ResponseDashboardSummary gasContractList = chainService.getDashboardSummary(user.getUserId());
        return new ResponseFormat<>(gasContractList);
    }
}
