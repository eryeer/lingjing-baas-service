package com.onchain.dna2explorer.exception;

/**
 * Created by Zhaochen on 3/22/18
 */
public class FeignClientResponseException extends Exception {
    public FeignClientResponseException(String msg) {
        super(msg);
    }
}
