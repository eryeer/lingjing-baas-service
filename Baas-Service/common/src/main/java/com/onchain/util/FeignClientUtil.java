package com.onchain.util;

import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.ResponseFormat;
import com.onchain.exception.FeignClientResponseException;
import com.onchain.exception.HystrixFallBackException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Zhaochen on 3/22/18
 */
@Slf4j
public class FeignClientUtil {

    public static void checkResponse(ResponseFormat responseFormat) throws HystrixFallBackException, FeignClientResponseException {
        if (responseFormat == null) {
            throw new FeignClientResponseException(CommonConst.FEIGN_RESPONSE_NULL_DESC);
        }
        log.info(responseFormat.toString());
        if (responseFormat.getReturnCode() == null) {
            throw new FeignClientResponseException(CommonConst.FEIGN_RETURNCODE_NULL_DESC);
        }
        if (responseFormat.getReturnCode() == ReturnCode.HYSTRIX_FALLBACK.getValue()) {
            throw new HystrixFallBackException(CommonConst.HYSTRIX_FALLBACK_DESC);
        }

        if (responseFormat.getReturnCode() % 1000 != 0) {
            throw new FeignClientResponseException(CommonConst.FEIGN_RESPONSE_FAIL);
        }
    }

    public static <T> ResponseFormat<T> generateFallbackResponse(Class<T> clazz) {
        ResponseFormat<T> responseFormat = new ResponseFormat<>();
        responseFormat.setReturnCode(ReturnCode.HYSTRIX_FALLBACK.getValue());
        return responseFormat;
    }

}
