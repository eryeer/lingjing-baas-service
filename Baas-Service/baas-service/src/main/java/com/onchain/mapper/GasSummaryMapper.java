package com.onchain.mapper;


import com.onchain.entities.dao.GasSummary;
import com.onchain.entities.response.ResponseUserGasClaimSummary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GasSummaryMapper {

    String INSERT_COLS = " user_id, user_address, name, apply_amount, apply_time, tx_hash ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{userAddress}, #{name}, #{applyAmount}, #{applyTime}, #{txHash} ";

    //获取userid剩余可申请的gas
    @Select("select cast(agreement_amount as decimal(60)) - cast(apply_amount as decimal(60))\n" +
            "from tbl_gas_summary where user_id = #{userId} for update;")
    String getRemainAmountByUserId(String userId);

//    @Insert("insert into tbl_gas_summary(user_id, apply_amount, apply_time, agreement_amount, agreement_time)\n" +
//            "select user_id, sum(cast(apply_amount as decimal(60))), max(apply_time),0,0\n" +
//            "from tbl_gas_apply\n" +
//            "where status != 0 and user_id = #{userId}\n" +
//            "on duplicate key update \n" +
//            "apply_amount = VALUES(apply_amount),\n" +
//            "apply_time = VALUES(apply_time) ")
//    void updateGasApplySummary(String userId);

    @Insert("<script>" +
            "insert into tbl_gas_summary(user_id, apply_amount, apply_time, agreement_amount, agreement_time)\n" +
            "select user_id, sum(cast(apply_amount as decimal(60))), max(apply_time),0,0\n" +
            "from tbl_gas_apply\n" +
            "where status != 0 and user_id in " +
            "<foreach collection='userIds' item='userId' open='(' close=')'  separator=','  >" +
            " #{userId}" +
            "</foreach>  " +
            "on duplicate key update \n" +
            "apply_amount = VALUES(apply_amount),\n" +
            "apply_time = VALUES(apply_time) " +
            "</script>")
    void updateGasApplySummary(@Param("userIds") List<String> userIds);

    @Insert("insert into tbl_gas_summary (user_id, agreement_amount, agreement_time, apply_amount, apply_time) " +
            "select user_id, sum(cast(agreement_amount as decimal(60))) as agreement_amount, max(approved_time) as agreement_time, 0, 0 " +
            "from tbl_gas_contract " +
            "where status =1 and user_id = #{userId} " +
            "on duplicate key update " +
            "agreement_amount = VALUES(agreement_amount), " +
            "agreement_time = VALUES(agreement_time) ")
    void updateGasContractSummary(String userId);

    @Select("select user_id, apply_amount, agreement_amount, apply_time, agreement_time from tbl_gas_summary where user_id = #{userId} ")
    GasSummary getGasSummaryInfoByUserId(String userId);

    @Select("<script> " +
            "select s.*, u.phone_number, u.company_name  " +
            "FROM tbl_gas_summary s, tbl_user u " +
            "<where> s.user_id = u.user_id " +
            "<if test='phoneNumber != null'> AND u.phone_Number = #{phoneNumber} </if> " +
            "<if test='companyName != null'> AND u.company_Name = #{companyName} </if> " +
            "<if test='applyStartTime != null and applyEndTime != null'> AND s.apply_time between #{applyStartTime} and #{applyEndTime} </if> " +
            "</where>" +
            " order by apply_time desc " +
            "</script>")
    List<ResponseUserGasClaimSummary> getUserGasClaimSummary(String phoneNumber, String companyName, Long applyStartTime, Long applyEndTime);
}
