package com.onchain.entities.dao;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ChainAccount extends Base {
    private String userId;
    private String userAddress;
    private String balance;
    private Date applyTime;
    private String privateKey;
    private String walletPass;
    private String walletFileUuid;
}
