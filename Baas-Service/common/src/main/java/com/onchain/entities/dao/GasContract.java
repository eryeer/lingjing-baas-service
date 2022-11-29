package com.onchain.entities.dao;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class GasContract extends Base {
    private String userId;
    private String flowId;
    private String contractFileUUID;
    private String agreementAmount;
    private Long uploadTime;
    private Long approvedTime;
    private String feedback;
}