package com.onchain.dna2explorer.exception;

/**
 * Created by Zhaochen on 3/22/18
 */
public class HystrixFallBackException extends Exception {
    public HystrixFallBackException(String msg) {
        super(msg);
    }
}
