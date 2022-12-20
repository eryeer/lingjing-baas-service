package com.onchain.mapper;


import com.onchain.entities.dao.GasApply;
import com.onchain.entities.response.ResponseChainAccountGasClaimSummary;
import com.onchain.entities.response.ResponseChainAccountGasSummary;
import com.onchain.entities.response.ResponseGasClaimHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GasApplyMapper {

    String INSERT_COLS = " user_id, user_address, name, apply_amount, apply_time, tx_hash ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{userAddress}, #{name}, #{applyAmount}, #{applyTime}, #{txHash} ";

    //添加申领记录并获取主键
    @Insert("insert into tbl_gas_apply (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertGasApply(GasApply gasApply);

    @Select("select tca.name as account_name, tca.user_address as account_address, tga.apply_amount\n" +
            "from (\n" +
            "select user_address, sum(cast(apply_amount as decimal(60) )) as apply_amount\n" +
            "from tbl_gas_apply\n" +
            "where user_id = #{userId}  \n" +
            "group by user_address ) tga\n" +
            "right join tbl_chain_account tca on tga.user_address = tca.user_address " +
            "where tca.status = 1 and tca.user_id = #{userId}")
    List<ResponseChainAccountGasSummary> getChainAccountApplySummary(String userId);

    @Select("<script>" +
            "select tca.user_address as account_address, tca.name, info.applied_gas, info.recently_apply_time\n" +
            "from (\n" +
            "select tga.user_address,  sum(cast(tga.apply_amount as decimal(60))) as applied_gas, max(tga.apply_time) as recently_apply_time\n" +
            "from tbl_gas_apply tga\n" +
            "right join tbl_chain_account tca on tga.user_address = tca.user_address\n" +
            "where tca.status = 1 group by tga.user_address ) as info\n" +
            "right join tbl_chain_account tca on tca.user_address = info.user_address" +
            "<where> tca.user_id = #{userId} and tca.status = 1 " +
            "<if test='name != null'>AND tca.name = #{name} </if> " +
            "<if test='userAddress != null'>AND tca.user_address = #{userAddress} </if> " +
            "<if test='applyStartTime != null and applyEndTime != null'> AND info.recently_apply_time between #{applyStartTime} and #{applyEndTime} </if> " +
            "</where>" +
            "order by info.recently_apply_time desc, tca.create_time desc" +
            "</script>")
    List<ResponseChainAccountGasClaimSummary> getChainAccountGasInfoList(String userId, String userAddress, Long applyStartTime, Long applyEndTime, String name);

    @Select("<script> " +
            "select a.*, u.phone_number, u.company_name  " +
            "FROM tbl_gas_apply a, tbl_user u " +
            "<where> a.user_id = u.user_id " +
            "<if test='userId != null'>AND a.user_id = #{userId} </if> " +
            "<if test='userAddress != null'>AND a.user_Address = #{userAddress} </if> " +
            "<if test='name != null'>AND a.name = #{name} </if> " +
            "<if test='phoneNumber != null'> AND u.phone_Number = #{phoneNumber} </if> " +
            "<if test='companyName != null'> AND u.company_Name = #{companyName} </if> " +
            "<if test='applyStartTime != null and applyEndTime != null'> AND a.apply_time between #{applyStartTime} and #{applyEndTime} </if> " +
            "</where>" +
            " order by apply_time desc " +
            "</script>")
    List<ResponseGasClaimHistory> getGasClaimHistory(String userId, String userAddress, String name, String phoneNumber, String companyName, Long applyStartTime, Long applyEndTime);
}
