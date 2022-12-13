package com.onchain.entities.dao;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class GasSummary extends Base {
    String userId;
    String applyAmount;
    Long applyTime;
    String agreementAmount;
    Long agreementTime;
}
