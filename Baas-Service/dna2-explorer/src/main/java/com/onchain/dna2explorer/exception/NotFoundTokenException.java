package com.onchain.dna2explorer.exception;

import com.onchain.dna2explorer.constants.ReturnCode;

public class NotFoundTokenException extends RuntimeException {
    private ReturnCode returnCode;

    public NotFoundTokenException(ReturnCode returnCode, String msg) {
        super(msg);
        this.returnCode = returnCode;
    }

    public NotFoundTokenException(ReturnCode returnCode) {
        super(returnCode.getDesc());
        this.returnCode = returnCode;
    }

    public ReturnCode getReturnCode() {
        return returnCode;
    }
}
