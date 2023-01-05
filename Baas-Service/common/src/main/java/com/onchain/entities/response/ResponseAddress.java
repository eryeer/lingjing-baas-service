package com.onchain.entities.response;

import lombok.Data;

@Data
public class ResponseAddress {
    private String address;
    private Integer type;
    private String balance;
    private Long blockTime;
    private Integer txCount;
    private Integer deployCount;
}
