package com.onchain.entities.dao;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)

public class LoginLog extends Base {
    private String userId;
    private Date loginTime;
}
