package com.onchain.entities.response;

import lombok.Data;

@Data
public class ResponseTotalSummary {

    private String blockNumber;
    private Long txCount;
    private Integer nodeCount;
    private Integer activeCount;
    private Boolean netStatus;
    private Integer addressCount;
}
