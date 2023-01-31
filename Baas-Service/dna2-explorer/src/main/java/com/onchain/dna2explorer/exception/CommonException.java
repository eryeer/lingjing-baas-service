package com.onchain.dna2explorer.exception;


import com.onchain.dna2explorer.constants.ReturnCode;

public class CommonException extends RuntimeException {
    private ReturnCode returnCode;

    public CommonException(ReturnCode returnCode, String msg) {
        super(msg);
        this.returnCode = returnCode;
    }

    public CommonException(ReturnCode returnCode) {
        super(returnCode.getDesc());
        this.returnCode = returnCode;
    }

    public ReturnCode getReturnCode() {
        return returnCode;
    }
}
