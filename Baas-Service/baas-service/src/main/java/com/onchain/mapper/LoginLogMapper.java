package com.onchain.mapper;


import org.apache.ibatis.annotations.Insert;

public interface LoginLogMapper {
    String BASIC_COLS = " id, create_time, update_time, status, user_id, login_time ";

    //添加登录日志
    @Insert("insert into tbl_login_log (user_id) " +
            "values (#{userId})")
    void insertLog(String userId);

}
