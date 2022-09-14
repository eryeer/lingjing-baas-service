package com.onchain.exception;

public class DistributedLockException extends Exception {
    public DistributedLockException(String message) {
        super(message);
    }
}
