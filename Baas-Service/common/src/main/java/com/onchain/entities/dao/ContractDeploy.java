package com.onchain.entities.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractDeploy {
    private String userId;
    private String appName;
    private String templateType;
    private Date deployTime;
    private String contractFileUuid;
    private String contractName;
    private String contractAddress;
    private String bytecode;
    private String constructorJson;
}
