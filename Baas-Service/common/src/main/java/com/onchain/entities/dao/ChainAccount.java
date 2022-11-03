package com.onchain.entities.dao;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ChainAccount extends Base {
    private String userId;
    private String userAddress;
    private String name;
    private Boolean isGasTransfer;
    private Boolean isCustody;
    private String encodeKey;
}
