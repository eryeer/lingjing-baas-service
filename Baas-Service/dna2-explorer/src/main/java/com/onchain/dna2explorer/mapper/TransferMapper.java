package com.onchain.dna2explorer.mapper;

import com.onchain.dna2explorer.model.dao.Transfer;
import com.onchain.dna2explorer.model.response.ResponseTransfer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferMapper {

    String COLS = " id, create_time, update_time, status, tx_hash, block_hash, block_number, from_address, to_address, transfer_from, transfer_to, contract_address, from_type, to_type, token_id, block_time, log_index ";

    @Insert("<script>" +
            "insert ignore into tbl_transfer (tx_hash, block_hash, block_number, from_address, to_address, transfer_from, transfer_to, contract_address, from_type, to_type, token_id, block_time, log_index) " +
            "values " +
            "<foreach collection='list' item='item' separator=','> " +
            "( #{item.txHash}, #{item.blockHash}, #{item.blockNumber}, #{item.fromAddress}, #{item.toAddress}, #{item.transferFrom}, #{item.transferTo}, #{item.contractAddress}, #{item.fromType}, #{item.toType}, #{item.tokenId}, #{item.blockTime}, #{item.logIndex}) " +
            "</foreach>" +
            "</script>")
    Integer batchInsertIgnore(@Param("list") List<Transfer> transactions);

    @Select("<script> " +
            "select a.*, b.token_name, b.token_symbol " +
            "from tbl_transfer a " +
            "left join tbl_contract b on a.contract_address = b.address " +
            "<where> 1 = 1 " +
            "<if test='address != null'>AND (a.transfer_from = #{address} or a.transfer_to = #{address}) </if> " +
            "</where>" +
            " order by block_number desc limit #{offset}, #{pageSize} " +
            "</script>")
    List<ResponseTransfer> getTransferList(@Param("address") String address, Integer offset, Integer pageSize);

    @Select("<script> " +
            "select count(a.transfer_from) " +
            "from tbl_transfer a " +
            "left join tbl_contract b on a.contract_address = b.address " +
            "<where> " +
            "<if test='address != null'>a.transfer_from = #{address}  </if> " +
            "</where>" +
            "</script>")
    Integer getTransferListCountByTransferFrom(@Param("address") String address);

    @Select("<script> " +
            "select count(a.transfer_from) " +
            "from tbl_transfer a " +
            "left join tbl_contract b on a.contract_address = b.address " +
            "<where> " +
            "<if test='address != null'> a.transfer_to = #{address} </if> " +
            "</where>" +
            "</script>")
    Integer getTransferListCountByTransferTo(@Param("address") String address);

    @Select("select max(block_number) " +
            "from tbl_transfer ")
    Long getMaxBlockNumber();


    @Select("<script> " +
            "select a.*, b.token_name, b.token_symbol " +
            "from tbl_transfer a " +
            "left join tbl_contract b on a.contract_address = b.address " +
            "<where> 1 = 1 " +
            "<if test='address != null'>AND (a.transfer_from = #{address} or a.transfer_to = #{address}) </if> "+
            "<if test='startTime != null'>AND (a.block_time &gt;= #{startTime}) </if> " +
            "<if test='endTime != null'>AND (a.block_time &lt;= #{endTime}) </if> " +
            "</where>" +
            " order by block_number desc limit 5000" +
            "</script>")
    List<ResponseTransfer> getLatest5KTransferList(@Param("address") String address, @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    @Select("<script> " +
            "select * " +
            "from tbl_transfer  " +
            "<where> id > #{id}</where>" +
            " limit #{limit}" +
            "</script>")
    List<Transfer> getTransferListByGTId(@Param("id") Long id, @Param("limit") Long limit);
}
