package com.onchain.mapper;


import com.onchain.entities.dao.ContractApp;
import com.onchain.entities.response.ResponseContractApp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ContractAppMapper {

    String INSERT_COLS = " user_id, app_name, contract_status, template_type, deploy_time, deploy_history, contract_file_uuids ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{appName}, #{contractStatus}, #{templateType}, #{deployTime}, #{deployHistory}, #{contractFileUuids} ";

    //根据用户id查询应用列表
    @Select("select " + BASIC_COLS + " from tbl_contract_app where user_id= #{userId} and status = 1")
    List<ContractApp> getContractAppListByUserId(String userId);

    @Select("select " + BASIC_COLS + " from tbl_contract_app where user_id= #{userId} and status = 1")
    List<ResponseContractApp> getContractAppList(String userId);

    @Select("select " + BASIC_COLS + " from tbl_contract_app where user_id= #{userId} and app_name = #{appName} and status = 1")
    ContractApp getContractApp(String userId, String appName);

    @Select("select " + BASIC_COLS + " from tbl_contract_app where user_id= #{userId} and app_name = #{appName} and status = 1")
    ResponseContractApp getResponseContractApp(String userId, String appName);

    @Update("update tbl_contract_app set status = 0 where user_id= #{userId} and app_name = #{appName}")
    void removeContractApp(String userId, String appName);

    //添加链账户
    @Insert("insert into tbl_contract_app (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertContractApp(ContractApp account);

    //根据用户Id更新链账户
    @Update("update tbl_contract_app set contract_status = #{contractStatus}, deploy_time = #{deployTime}, deploy_history = #{deployHistory}, contract_file_uuids = #{contractFileUuids} " +
            "where user_id = #{userId} and app_name = #{appName} and status = 1 ")
    void updateContractApp(ContractApp account);
}
