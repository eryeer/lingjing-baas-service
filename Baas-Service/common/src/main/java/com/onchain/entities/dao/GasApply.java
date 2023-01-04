package com.onchain.entities.dao;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class GasApply extends Base {
    private String userId;
    private String userAddress;
    private String name;
    private String applyAmount;
    private Long applyTime;
    private String txHash;
    private Integer retries;
}
