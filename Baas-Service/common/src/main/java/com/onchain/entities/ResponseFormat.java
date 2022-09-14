package com.onchain.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onchain.constants.ReturnCode;
import com.onchain.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFormat<T> {
    private Integer returnCode;
    private String returnDesc;
    private T data;
    private String version;

    public ResponseFormat() {
        setReturnCode(ReturnCode.REQUEST_SUCCESS.getValue());
        setReturnDesc(ReturnCode.REQUEST_SUCCESS.getDesc());
        setVersion(CommonUtil.getVersion());
    }

    public ResponseFormat(ReturnCode returnCode) {
        setReturnCode(returnCode.getValue());
        setReturnDesc(returnCode.getDesc());
        setVersion(CommonUtil.getVersion());
    }

    public ResponseFormat(Integer returnCode, String returnDesc, T data) {
        setReturnCode(returnCode);
        setReturnDesc(returnDesc);
        setVersion(CommonUtil.getVersion());
        setData(data);
    }

    public ResponseFormat(ReturnCode returnCode, String msg) {
        setReturnCode(returnCode.getValue());
        String desc = returnCode.getDesc();
        if (!StringUtils.equals(desc, msg)) {
            desc = desc + " - " + msg;
        }
        setReturnDesc(desc);
        setVersion(CommonUtil.getVersion());
    }

    public ResponseFormat(T obj) {
        setReturnCode(ReturnCode.REQUEST_SUCCESS.getValue());
        setReturnDesc(ReturnCode.REQUEST_SUCCESS.getDesc());
        setVersion(CommonUtil.getVersion());
        setData(obj);
    }
}

