package com.onchain.entities.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseContractDepoly {
    private String contractAddress;
    private String transactionHash;
    private String gasUsed;
    private String blockNumber;
}
