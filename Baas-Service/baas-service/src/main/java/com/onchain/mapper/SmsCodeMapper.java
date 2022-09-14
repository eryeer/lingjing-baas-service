package com.onchain.mapper;


import com.onchain.entities.dao.SmsCode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface SmsCodeMapper {

    String BASIC_COLS = " id, create_time, update_time, status, phone_number, code, code_type, send_time, expiration_time ";

    // 插入验证码
    @Insert("insert into tbl_sms_code (phone_number, code, code_type, send_time, expiration_time) " +
            "values (#{phoneNumber}, #{code}, #{codeType}, #{sendTime}, #{expirationTime})")
    void insertSmsCode(SmsCode smsCode);

    // 获取手机号最新且有效的验证码
    @Select("select " + BASIC_COLS + " from tbl_sms_code " +
            "where phone_number = #{phoneNumber} and code_type = #{codeType} and expiration_time > now() and status = 1 " +
            "order by id desc limit 1")
    SmsCode getLastSmsCode(String phoneNumber, String codeType);

    // 使用后使验证码失效
    @Update("update tbl_sms_code set status = 0 " +
            "where phone_number = #{phoneNumber} and code_type = #{codeType} and code = #{code} and status = 1 ")
    void disableSmsCode(String phoneNumber, String codeType, String code);

}
