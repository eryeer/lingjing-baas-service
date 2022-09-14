package com.onchain.entities.dao;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class SmsCode extends Base {
    private String phoneNumber;
    private String code;
    private String codeType;
    private Date sendTime;
    private Date expirationTime;

}
