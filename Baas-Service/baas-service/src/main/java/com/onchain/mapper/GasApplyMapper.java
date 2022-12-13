package com.onchain.mapper;


import com.onchain.entities.dao.GasApply;
import com.onchain.entities.dao.GasSummary;
import com.onchain.entities.response.ReponseChainAccountGasApplySummary;
import com.onchain.entities.response.ResponseChainAccountGasSummary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GasApplyMapper {

    String INSERT_COLS = " user_id, user_address, apply_amount, apply_time, tx_hash ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{userAddress}, #{applyAmount}, #{applyTime}, #{txHash} ";

    //添加申领记录
    @Insert("insert into tbl_gas_apply (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertGasApply(GasApply gasApply);

    @Select("select tca.name, tca.user_address, tga.apply_time, tga.apply_amount, tga.tx_hash\n" +
            "from tbl_gas_apply tga\n" +
            "right join tbl_chain_account tca on tga.user_id = tca.user_id and tga.user_address = tca.user_address\n" +
            "where tca.user_id = #{userId} ")
    List<ResponseChainAccountGasSummary> getChainAccountApplyList(String userId);

    @Select("<script>" +
            "select tca.user_address, tca.name, info.applied_gas, info.recently_apply_time\n" +
            "from (\n" +
            "select tga.user_address,  sum(cast(tga.apply_amount as decimal(60))) as applied_gas, max(tga.apply_time) as recently_apply_time\n" +
            "from tbl_gas_apply tga\n" +
            "right join tbl_chain_account tca on tga.user_address = tca.user_address\n" +
            "group by tga.user_address ) as info\n" +
            "right join tbl_chain_account tca on tca.user_address = info.user_address" +
            "<where> tca.user_id = #{userId} and tca.status = 1 " +
            "<if test='name != null'>AND tca.name = #{name} </if> " +
            "<if test='userAddress != null'>AND tca.user_address = #{userAddress} </if> " +
            "<if test='applyStartTime != null and applyEndTime != null'> AND info.recently_apply_time between #{applyStartTime} and #{applyEndTime} </if> " +
            "</where>" +
            "order by info.recently_apply_time desc" +
            "</script>")
    List<ReponseChainAccountGasApplySummary> getChainAccountGasInfoList(String userId, String userAddress, Long applyStartTime, Long applyEndTime, String name);

    //获取userid剩余可申请的gas
    @Select("select cast(agreement_amount as decimal(60)) - cast(apply_amount as decimal(60))\n" +
            "from tbl_gas_summary where user_id = #{userId};")
    String getRemainAmountByUserId(String userId);

    @Insert("insert into tbl_gas_summary (user_id, apply_amount, agreement_amount, apply_time, agreement_time) \n" +
            "values (#{userId}, #{applyAmount}， #{agreementAmount}, #{applyTime}, #{agreementTime})\n" +
            "on duplicate key update user_id = VALUES(user_id) , apply_amount = VALUES(apply_amount), agreement_amount = VALUES(agreement_amount), apply_time = VALUES(apply_time), agreement_time = VALUES(agreement_time)")
    void updateGasSummaryInfo(GasSummary gasSummary);
}
