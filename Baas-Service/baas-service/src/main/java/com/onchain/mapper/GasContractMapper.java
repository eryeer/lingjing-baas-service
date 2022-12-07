package com.onchain.mapper;

import com.onchain.entities.dao.GasContract;
import com.onchain.entities.response.ResponseAdminGasContract;
import com.onchain.entities.response.ResponseGasContract;
import com.onchain.entities.response.ResponseGasContractStatistic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface GasContractMapper {

    String INSERT_COLS = " user_id, contract_file_uuid, flow_id, upload_time, approved_time, agreement_amount, feedback ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{contractFileUUID}, #{flowId}, #{uploadTime},  #{approvedTime} ,  #{agreementAmount},  #{feedback}  ";

    // 创建gas合同
    @Insert("insert into tbl_gas_contract (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void createGasContract(GasContract gasContract);

    // 审批gas合同
    @Update("update tbl_gas_contract set status = #{status}, approved_time = #{approvedTime}, agreement_amount = #{agreementAmount}, feedback = #{feedback} where flow_id = #{flowId}")
    void approveGasContract(GasContract gasContract);

    @Select("<script> " +
            "select " + BASIC_COLS + " from tbl_gas_contract " +
            "<where>  user_id = #{userId} " +
            "<if test='status != null'>AND status = #{status} </if> " +
            "<if test='flowId != null'> AND flow_id = #{flowId} </if> " +
            "<if test='uploadStartTime != null and uploadEndTime != null'> AND upload_time between #{uploadStartTime} and #{uploadEndTime} </if> " +
            "<if test='approvedStartTime != null and approvedEndTime != null'> AND approved_time between #{approvedStartTime} and #{approvedEndTime} </if> " +
            "</where>" +
            " order by upload_time desc, approved_time desc " +
            "</script>")
    List<ResponseGasContract> getGasContractList(String userId, Integer status, String flowId, Long uploadStartTime, Long uploadEndTime, Long approvedStartTime, Long approvedEndTime);

    //通过userid 查询审核通过的gas合同
    @Select("select " + BASIC_COLS + " from tbl_gas_contract  where  user_id = #{userId} and status = 1 order by upload_time, approved_time desc ")
    List<ResponseGasContract> getSuccessGasContractListByUserId(String userId);

    //根据流水号查询gas签约的合同
    @Select("select " + BASIC_COLS + " from tbl_gas_contract where flow_id= #{flowId} limit 1")
    ResponseGasContract getGasContractByFlowId(String flowId);

    @Select("<script> " +
            "select c.*, u.phone_number, u.company_name from tbl_gas_contract c, tbl_user u " +
            "<where> c.user_id = u.user_id and u.status = 1 " +
            "<if test='phoneNumber != null'>AND u.phone_number = #{phoneNumber} </if> " +
            "<if test='companyName != null'>AND u.company_name = #{companyName} </if> " +
            "<if test='agreementAmount != null'>AND c.agreement_Amount = #{agreementAmount} </if> " +
            "<if test='status != null'>AND c.status = #{status} </if> " +
            "<if test='isApproving != null and isApproving'>AND c.status = 0 </if> " +
            "<if test='isApproving != null and !isApproving'>AND c.status != 0 </if> " +
            "<if test='flowId != null'> AND c.flow_id = #{flowId} </if> " +
            "<if test='uploadStartTime != null and uploadEndTime != null'> AND c.upload_time between #{uploadStartTime} and #{uploadEndTime} </if> " +
            "<if test='approvedStartTime != null and approvedEndTime != null'> AND c.approved_time between #{approvedStartTime} and #{approvedEndTime} </if> " +
            "</where>" +
            " order by upload_time desc, approved_time desc " +
            "</script>")
    List<ResponseAdminGasContract> getAdminGasContractList(String phoneNumber, String companyName, String agreementAmount, Integer status, Boolean isApproving, String flowId, Long uploadStartTime, Long uploadEndTime, Long approvedStartTime, Long approvedEndTime);

    @Select("<script> " +
            "select u.phone_number, u.company_name, u.user_id, sum(cast(agreement_amount as decimal(60))) as total_amount, max(approved_time) as last_approved_time  " +
            "from tbl_gas_contract c, tbl_user u " +
            "<where> c.user_id = u.user_id and u.status = 1 and c.status = 1 " +
            "<if test='phoneNumber != null'>AND u.phone_number = #{phoneNumber} </if> " +
            "<if test='companyName != null'>AND u.company_name = #{companyName} </if> " +
            "</where>" +
            "group by u.phone_number, u.company_name, u.user_id " +
            "<if test='approvedStartTime != null and approvedEndTime != null'>having last_approved_time between #{approvedStartTime} and #{approvedEndTime} </if> " +
            "order by last_approved_time desc " +
            "</script>")
    List<ResponseGasContractStatistic> getGasContactStatisticList(String phoneNumber, String companyName, Long approvedStartTime, Long approvedEndTime);
}
