package com.onchain.feign;

import com.onchain.entities.ResponseFormat;
import com.onchain.entities.request.RequestAddressList;
import com.onchain.entities.response.ResponseAddress;
import com.onchain.entities.response.ResponseContractHolderInfo;
import com.onchain.entities.response.ResponseTotalSummary;
import com.onchain.util.FeignClientUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExplorerHystrix implements ExplorerFeignService {
    @Override
    public ResponseFormat<ResponseTotalSummary> getTotalSummary() {
        return FeignClientUtil.generateFallbackResponse(null);
    }

    @Override
    public ResponseFormat<List<ResponseAddress>> getAddressListByAddress(RequestAddressList request) {
        return FeignClientUtil.generateFallbackResponse(null);
    }

    @Override
    public ResponseFormat<ResponseContractHolderInfo> getContractByCreatorAddress(Integer pageNumber, Integer pageSize, List<String> userAddressList, String contractAddress, Long startTime, Long endTime) {
        return FeignClientUtil.generateFallbackResponse(null);
    }

}
