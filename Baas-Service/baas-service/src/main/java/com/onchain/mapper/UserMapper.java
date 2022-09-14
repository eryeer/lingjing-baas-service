package com.onchain.mapper;


import com.onchain.constants.CommonConst;
import com.onchain.entities.dao.User;
import com.onchain.entities.response.ResponseUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface UserMapper {
    // password not in BASIC_COLS
    String INSERT_COLS = " user_id, user_name, phone_number, password, role, id_number, company_name, uni_social_credit_code, legal_person_name, legal_person_idn, apply_time, approve_status, approve_feedback, approve_time, business_license_file_uuid, business_license_copy_file_uuid, ida_file_uuid, idb_file_uuid, legal_person_ida_file_uuid, legal_person_idb_file_uuid ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{userName}, #{phoneNumber}, #{password}, #{role}, #{idNumber}, #{companyName}, #{uniSocialCreditCode}, #{legalPersonName}, #{legalPersonIdn}, #{applyTime}, #{approveStatus}, #{approveFeedback}, #{approveTime}, #{businessLicenseFileUuid}, #{businessLicenseCopyFileUuid}, #{idaFileUuid}, #{idbFileUuid}, #{legalPersonIdaFileUuid}, #{legalPersonIdbFileUuid} ";

    //根据手机号查询未删除用户
    @Select("select " + BASIC_COLS + " from tbl_user where phone_number= #{phoneNumber} and status <> 0 and approve_status <> 'Rejected' limit 1")
    User getUserByPhoneNumber(String phoneNumber);

    //根据会员号查询用户作为响应对象
    @Select("select " + BASIC_COLS + " from tbl_user where user_id =#{userId} and status <> 0 and approve_status <> 'Rejected' ")
    ResponseUser getResponseUserById(String userId);

    //根据会员号和密码查询用户(验证密码)
    @Select("select count(1) > 0 as valid from tbl_user where user_id = #{userId} and password = #{password} and status = " + CommonConst.ACTIVE + " and approve_status = 'Approved'")
    Boolean checkPassword(String userId, String password);

    //添加用户
    @Insert("insert into tbl_user (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertUser(User user);
//
//    //根据会员号更新会员信息, 不包含密码更新
//    @Update("update tbl_user set user_name = #{userName}, phone_number = #{phoneNumber}, " +
//            "role = #{role}, id_number = #{idNumber}, email = #{email}, ida_file_uuid = #{idaFileUuid}, idb_file_uuid = #{idbFileUuid}, " +
//            "company_id = #{companyId}, company_name = #{companyName}, is_registered = #{isRegistered}, is_policy_signed = #{isPolicySigned} " +
//            "where user_id = #{userId}")
//    void updateUser(User user);

    //更新用户密码
    @Update("update tbl_user set password = #{password} " +
            "where user_id = #{userId}")
    void updatePassword(String userId, String password);

    // 审批用户
    @Update("update tbl_user set approve_status = #{approveStatus}, approve_feedback = #{approveFeedback}, approve_time = now() " +
            "where user_id = #{userId} ")
    void approveUser(User user);

    //根据用户Id更新用户状态
    @Update("update tbl_user set status = #{status} where user_id = #{userId}")
    void updateStatusByUserId(String userId, String status);

    //更新用户状态
    @Update("update tbl_user set status = #{status} where company_id = #{companyId} and status!='0'")
    void updateUserStatus(String companyId, String status);

    @Select("<script> " +
            "select " + BASIC_COLS + " from tbl_user " +
            "<where> status != '0' and role !='PM' " +
            "<if test='isPending == true'>AND approve_status = 'Pending' </if> " +
            "<if test='isPending != true'>AND approve_status in ('Approved', 'Rejected') </if> " +
            "<if test='userId != null'>AND user_Id = #{userId} </if> " +
            "<if test='userName != null'> AND user_name = #{userName} </if> " +
            "<if test='companyName != null'> AND company_name = #{companyName} </if> " +
            "<if test='phoneNumber != null'> AND phone_number = #{phoneNumber} </if> " +
            "<if test='start != null'> AND apply_time between #{start} and #{end} </if> " +
            "</where>" +
            " order by update_time desc " +
            "</script>")
    List<ResponseUser> getUserList(Boolean isPending, String userId, String userName, String companyName, String phoneNumber, Date start, Date end);

    //检查手机号是否重复
    @Select("select case  when   count(*)>0  then  1  else 0 end from tbl_user where  phone_number= #{phoneNumber} and status<>0")
    boolean getCompanyChkPhone(String phoneNumber);

    //根据公司编号查询公司下的所有人员
    @Select("select " + BASIC_COLS + " from tbl_user where company_id =#{centerId} and status <> " + CommonConst.DELETED)
    List<User> getUserByCompanyId(String centerId);

    //根据公司id批量更新
    @Update("update tbl_user set  company_name = #{companyName} " +
            "where company_id = #{companyId}")
    void updateUserList(User user);


}
