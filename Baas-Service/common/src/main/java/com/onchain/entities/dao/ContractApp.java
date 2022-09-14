package com.onchain.entities.dao;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ContractApp extends Base {
    private String userId;
    private String appName;
    private String contractStatus;
    private String templateType;
    private Date deployTime;
    private String deployHistory;
    private String contractFileUuids;
}
