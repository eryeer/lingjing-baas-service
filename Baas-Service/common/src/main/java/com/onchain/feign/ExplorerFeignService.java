package com.onchain.feign;

import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.request.RequestAddressList;
import com.onchain.entities.response.ResponseAddress;
import com.onchain.entities.response.ResponseContractHolderInfo;
import com.onchain.entities.response.ResponseTotalSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Component
@FeignClient(value = UrlConst.EXPLORER_SERVICE)
public interface ExplorerFeignService {
    @GetMapping(value = UrlConst.GET_TOTAL_SUMMARY)
    ResponseFormat<ResponseTotalSummary> getTotalSummary();

    @PostMapping(value = UrlConst.GET_ADDRESS_LIST_BY_ADDRESS)
    ResponseFormat<List<ResponseAddress>> getAddressListByAddress(RequestAddressList request);

    @GetMapping(value = UrlConst.GET_CONTRACT_BY_CREATOR_ADDRESS)
    ResponseFormat<ResponseContractHolderInfo> getContractByCreatorAddress(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, @RequestParam List<String> userAddressList, @RequestParam String contractAddress, @RequestParam Long startTime, @RequestParam Long endTime);

}
