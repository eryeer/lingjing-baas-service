package com.onchain.entities.dao;

import lombok.*;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Base {
    private String userId;
    private String userName;
    private String phoneNumber;
    private String password;
    private String role;
    private String idNumber;

    private String companyName;
    private String uniSocialCreditCode;
    private String legalPersonName;
    private String legalPersonIdn;
    private Date applyTime;
    private String approveStatus;
    private String approveFeedback;
    private Date approveTime;

    private String businessLicenseFileUuid;
    private String businessLicenseCopyFileUuid;
    private String idaFileUuid;
    private String idbFileUuid;
    private String legalPersonIdaFileUuid;
    private String legalPersonIdbFileUuid;
}
