package com.onchain.service;

import com.alibaba.fastjson.JSON;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.ReturnCode;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.request.RequestAddressList;
import com.onchain.entities.response.ResponseAddress;
import com.onchain.entities.response.ResponseTotalSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExplorerService {

    private final ParamsConfig paramsConfig;
    private final RestTemplate restTemplate;

    public ResponseTotalSummary getTotalSummary() {
        String summaryUrl = paramsConfig.explorerUrl + UrlConst.GET_TOTAL_SUMMARY;
        ResponseFormat<ResponseTotalSummary> resSummary = restTemplate.exchange(summaryUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseFormat<ResponseTotalSummary>>() {
                }).getBody();
        if (resSummary == null || !ReturnCode.REQUEST_SUCCESS.getValue().equals(resSummary.getReturnCode())) {
            log.error("getTotalSummary error:" + JSON.toJSONString(resSummary));
            return null;
        }
        return resSummary.getData();
    }

    public List<ResponseAddress> getAddressList(List<String> addressList) {
        RequestAddressList requestAddressList = new RequestAddressList();
        requestAddressList.setAddressList(addressList);
        String getAddressUrl = paramsConfig.explorerUrl + UrlConst.GET_ADDRESS_LIST_BY_ADDRESS;
        ResponseFormat<List<ResponseAddress>> resAddress = restTemplate.exchange(getAddressUrl,
                HttpMethod.POST,
                new HttpEntity<>(requestAddressList),
                new ParameterizedTypeReference<ResponseFormat<List<ResponseAddress>>>() {
                }).getBody();
        if (resAddress == null || ReturnCode.REQUEST_SUCCESS.getValue().equals(resAddress.getReturnCode())) {
            log.error("getTotgetAddressListalSummary error:" + JSON.toJSONString(resAddress));
            return new ArrayList<>();
        }
        return resAddress.getData();
    }
}
