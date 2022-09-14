package com.onchain.entities.dao;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserPolicy extends Base {
    private String policyName;
    private String traceId;
    private String policyType;
    private String policyContent;
    private Integer version;

}
