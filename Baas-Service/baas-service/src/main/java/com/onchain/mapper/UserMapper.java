package com.onchain.mapper;


import com.onchain.entities.dao.User;
import com.onchain.entities.response.ResponseUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper {
    // password not in BASIC_COLS
    String INSERT_COLS = " user_id, user_name, user_type, phone_number, password, role, id_number, company_name, uni_social_credit_code, legal_person_name, legal_person_idn, apply_time, approve_status, approve_feedback, approve_time, business_license_file_uuid, ida_file_uuid, idb_file_uuid, legal_person_ida_file_uuid, legal_person_idb_file_uuid ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{userName}, #{userType}, #{phoneNumber}, #{password}, #{role}, #{idNumber}, #{companyName}, #{uniSocialCreditCode}, #{legalPersonName}, #{legalPersonIdn}, #{applyTime}, #{approveStatus}, #{approveFeedback}, #{approveTime}, #{businessLicenseFileUuid}, #{idaFileUuid}, #{idbFileUuid}, #{legalPersonIdaFileUuid}, #{legalPersonIdbFileUuid} ";

    //根据手机号查询未删除用户
    @Select("select " + BASIC_COLS + ",has_kyc_notify from tbl_user where phone_number= #{phoneNumber} and status <> 0 limit 1")
    User getUserByPhoneNumber(String phoneNumber);

    //根据用户id查询未删除用户
    @Select("select " + BASIC_COLS + ",has_kyc_notify from tbl_user where user_id =#{userId} and status <> 0 and approve_status <> 'Rejected' ")
    User getUserById(String userId);

    //根据会员号查询用户作为响应对象
    @Select("select " + BASIC_COLS + ",has_kyc_notify from tbl_user where user_id =#{userId} and status <> 0 and approve_status <> 'Rejected' ")
    ResponseUser getResponseUserById(String userId);

    //根据会员号和密码查询用户(验证密码)
    @Select("select count(1) > 0 as valid from tbl_user where user_id = #{userId} and password = #{password} ")
    Boolean checkPassword(String userId, String password);

    //添加用户
    @Insert("insert into tbl_user (user_id, phone_number, password, role) " +
            "values ( #{userId}, #{phoneNumber}, #{password}, #{role} )")
    void insertUser(User user);

    //添加审批记录
    @Insert("insert into tbl_approve_history ( kyc_type, " + INSERT_COLS + ") " +
            "select #{kycType}, " + INSERT_COLS + " from tbl_user where user_id = #{userId}")
    void insertApproveHistory(String userId, String kycType);

    //根据用户id更新用户认证信息
    @Update("update tbl_user set approve_status = #{approveStatus}, user_type = #{userType}, user_name = #{userName}, company_name = #{companyName}, " +
            "id_number = #{idNumber}, uni_social_credit_code = #{uniSocialCreditCode}, legal_person_name = #{legalPersonName}, legal_person_idn = #{legalPersonIdn}, " +
            "apply_time = #{applyTime},  ida_file_uuid = #{idaFileUuid}, idb_file_uuid = #{idbFileUuid}, " +
            "business_license_file_uuid = #{businessLicenseFileUuid}, legal_person_ida_file_uuid = #{legalPersonIdaFileUuid}, legal_person_idb_file_uuid = #{legalPersonIdbFileUuid} " +
            "where user_id = #{userId}")
    void updateUser(User user);

    // 更新用户密码
    @Update("update tbl_user set password = #{password} " +
            "where user_id = #{userId}")
    void updatePassword(String userId, String password);

    // 更新用户手机号
    @Update("update tbl_user set phone_number = #{phoneNumber} " +
            "where user_id = #{userId}")
    void updatePhoneNumber(String userId, String phoneNumber);

    //根据用户id查询用户kyc变更信息
    @Select("select " + BASIC_COLS + " from tbl_kyc_update where user_id =#{userId}")
    User getKycUpdateById(String userId);

    //根据用户id查询用户kyc变更信息
    @Select("select " + BASIC_COLS + " from tbl_kyc_update where user_id =#{userId}")
    ResponseUser getResKycUpdateById(String userId);

    //添加变更记录
    @Insert("insert into tbl_kyc_update ( " + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertKycUpdate(User user);

    //根据用户id更新用户变更信息
    @Update("update tbl_kyc_update set approve_status = #{approveStatus}, user_type = #{userType}, user_name = #{userName}, company_name = #{companyName}, " +
            "id_number = #{idNumber}, uni_social_credit_code = #{uniSocialCreditCode}, legal_person_name = #{legalPersonName}, legal_person_idn = #{legalPersonIdn}, " +
            "apply_time = #{applyTime},  ida_file_uuid = #{idaFileUuid}, idb_file_uuid = #{idbFileUuid}, " +
            "business_license_file_uuid = #{businessLicenseFileUuid}, legal_person_ida_file_uuid = #{legalPersonIdaFileUuid}, legal_person_idb_file_uuid = #{legalPersonIdbFileUuid} " +
            "where user_id = #{userId}")
    void updateKyc(User user);

    // 标记KYC变更反馈信息是否提示
    @Update("update tbl_user set has_kyc_notify = #{hasKycNotify} " +
            "where user_id = #{userId}")
    void markKycNotify(String userId, Boolean hasKycNotify);

    // 添加变更审批记录
    @Insert("insert into tbl_approve_history ( kyc_type, " + INSERT_COLS + ") " +
            "select #{kycType}, " + INSERT_COLS + " from tbl_kyc_update where user_id = #{userId}")
    void insertKycUpdateHistory(String userId, String kycType);

    // 审批用户
    @Update("update tbl_user set approve_status = #{approveStatus}, approve_feedback = #{approveFeedback}, approve_time = #{approveTime} " +
            "where user_id = #{userId} ")
    void approveUser(User user);

    // 审批用户KYC变更
    @Update("update tbl_kyc_update set approve_status = #{approveStatus}, approve_feedback = #{approveFeedback}, approve_time = #{approveTime} " +
            "where user_id = #{userId} ")
    void approveKycUpdate(User user);

    @Select("<script> " +
            "select " + BASIC_COLS + " from tbl_user " +
            "<where> status != '0' and role !='PM' and approve_status = #{approveStatus} " +
            "<if test='userType != null'>AND user_Type = #{userType} </if> " +
            "<if test='userName != null'> AND user_name = #{userName} </if> " +
            "<if test='companyName != null'> AND company_name = #{companyName} </if> " +
            "<if test='phoneNumber != null'> AND phone_number = #{phoneNumber} </if> " +
            "<if test='idNumber != null'> AND id_Number = #{idNumber} </if> " +
            "<if test='uniSocialCreditCode != null'> AND uni_Social_Credit_Code = #{uniSocialCreditCode} </if> " +
            "<if test='startApplyTime != null'> AND apply_time between #{startApplyTime} and #{endApplyTime} </if> " +
            "<if test='startApproveTime != null'> AND approve_time between #{startApproveTime} and #{endApproveTime} </if> " +
            "</where>" +
            " order by update_time desc " +
            "</script>")
    List<ResponseUser> getUserList(String approveStatus, String userType, String userName, String companyName, String phoneNumber,
                                   String idNumber, String uniSocialCreditCode, Long startApplyTime, Long endApplyTime, Long startApproveTime, Long endApproveTime);

    @Select("<script> " +
            "select " + BASIC_COLS + " from tbl_kyc_update " +
            "<where> status != '0' and role !='PM' and approve_status = #{approveStatus} " +
            "<if test='userType != null'>AND user_Type = #{userType} </if> " +
            "<if test='userName != null'> AND user_name = #{userName} </if> " +
            "<if test='companyName != null'> AND company_name = #{companyName} </if> " +
            "<if test='phoneNumber != null'> AND phone_number = #{phoneNumber} </if> " +
            "<if test='idNumber != null'> AND id_Number = #{idNumber} </if> " +
            "<if test='uniSocialCreditCode != null'> AND uni_Social_Credit_Code = #{uniSocialCreditCode} </if> " +
            "<if test='startApplyTime != null'> AND apply_time between #{startApplyTime} and #{endApplyTime} </if> " +
            "<if test='startApproveTime != null'> AND approve_time between #{startApproveTime} and #{endApproveTime} </if> " +
            "</where>" +
            " order by update_time desc " +
            "</script>")
    List<ResponseUser> getKycUpdateList(String approveStatus, String userType, String userName, String companyName, String phoneNumber,
                                        String idNumber, String uniSocialCreditCode, Long startApplyTime, Long endApplyTime, Long startApproveTime, Long endApproveTime);

    @Select("<script> " +
            "select " + BASIC_COLS + " from tbl_approve_history " +
            "<where> status != '0' and role !='PM' " +
            "<if test='approveStatus != null'>AND approve_Status = #{approveStatus} </if> " +
            "<if test='userType != null'>AND user_Type = #{userType} </if> " +
            "<if test='kycType != null'>AND kyc_Type = #{kycType} </if> " +
            "<if test='userName != null'> AND user_name = #{userName} </if> " +
            "<if test='companyName != null'> AND company_name = #{companyName} </if> " +
            "<if test='phoneNumber != null'> AND phone_number = #{phoneNumber} </if> " +
            "<if test='idNumber != null'> AND id_Number = #{idNumber} </if> " +
            "<if test='uniSocialCreditCode != null'> AND uni_Social_Credit_Code = #{uniSocialCreditCode} </if> " +
            "<if test='startApplyTime != null'> AND apply_time between #{startApplyTime} and #{endApplyTime} </if> " +
            "<if test='startApproveTime != null'> AND approve_time between #{startApproveTime} and #{endApproveTime} </if> " +
            "</where>" +
            " order by update_time desc " +
            "</script>")
    List<ResponseUser> getUserKycRecordList(String approveStatus, String userType, String kycType, String userName, String companyName, String phoneNumber,
                                            String idNumber, String uniSocialCreditCode, Long startApplyTime, Long endApplyTime, Long startApproveTime, Long endApproveTime);
}
