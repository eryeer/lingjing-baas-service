package com.onchain.entities.dao;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Base {
    private String userId;
    private String userType;
    private String userName;
    private String phoneNumber;
    private String password;
    private String role;
    private String idNumber;

    private String companyName;
    private String uniSocialCreditCode;
    private String legalPersonName;
    private String legalPersonIdn;
    private Long applyTime;
    private String approveStatus;
    private String approveFeedback;
    private Long approveTime;

    private String businessLicenseFileUuid;
    private String businessLicenseCopyFileUuid;
    private String idaFileUuid;
    private String idbFileUuid;
    private String legalPersonIdaFileUuid;
    private String legalPersonIdbFileUuid;
}
