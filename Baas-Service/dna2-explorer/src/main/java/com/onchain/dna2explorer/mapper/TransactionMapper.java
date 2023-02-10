package com.onchain.dna2explorer.mapper;

import com.onchain.dna2explorer.model.dao.Transaction;
import com.onchain.dna2explorer.model.response.ResponseTransaction;
import com.onchain.entities.response.ResponseUserContractInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@CacheNamespace(implementation = com.onchain.dna2explorer.config.RedisCache.class)
public interface TransactionMapper {

    String COLS = " id, create_time, update_time, status, tx_hash, block_hash, block_number, from_address, to_address, contract_address, tx_value, tx_status, block_time, nonce, tx_index, tx_type, data, gas_price, gas_limit, gas_used ";

    @Insert("<script>" +
            "insert into tbl_transaction (tx_hash, block_hash, block_number, `from_address`, `to_address`, contract_address, tx_value, tx_status, block_time, " +
            "nonce, tx_index, tx_type, data, gas_price, gas_limit, gas_used) " +
            "values " +
            "<foreach collection='list' item='item' separator=','> " +
            "(#{item.txHash}, #{item.blockHash}, #{item.blockNumber}, #{item.fromAddress}, #{item.toAddress}, #{item.contractAddress}, #{item.txValue}, #{item.txStatus}, #{item.blockTime},  " +
            "#{item.nonce}, #{item.txIndex}, #{item.txType}, #{item.data}, #{item.gasPrice}, #{item.gasLimit}, #{item.gasUsed}) " +
            "</foreach>" +
            "</script>")
    Integer insert(@Param("list") List<Transaction> transactions);

    @Select("<script> " +
            "select t.*, a.type as to_address_type from " +
            "(select id from tbl_transaction " +
            "<where> 1 = 1 " +
            "<if test='blockNumber != null'>AND block_number = #{blockNumber} </if> " +
            "<if test='address != null'>AND (to_address = #{address} or from_address = #{address} or contract_address = #{address}) </if> " +
            "</where>" +
            "order by block_number desc, tx_index limit #{offset},#{pageSize}) l " +
            "join tbl_transaction t on l.id = t.id " +
            "left join tbl_account a on t.to_address = a.address " +
            "</script>")
    @Options(useCache = false)
    List<ResponseTransaction> getTransactionList(Long blockNumber, String address, Integer offset, Integer pageSize);

    @Select("<script> " +
            "select count(1) from tbl_transaction " +
            "<where> 1 = 1 " +
            "<if test='blockNumber != null'>AND block_number = #{blockNumber} </if> " +
            "<if test='address != null'>AND (to_address = #{address} or from_address = #{address} or contract_address = #{address}) </if> " +
            "</where>" +
            "</script>")
    @Options(useCache = false)
    Integer getTransactionCount(Long blockNumber, String address);

    @Select("select " + COLS + " from tbl_transaction where tx_hash = #{txHash}")
    ResponseTransaction getTransaction(String txHash);

    @Select("(select distinct `from_address` as address from tbl_transaction " +
            "where block_time >= #{startTime} and block_time < #{endTime} and `from_address` != '') " +
            "union all " +
            "(select distinct `to_address` as address from tbl_transaction " +
            "where block_time >= #{startTime} and block_time < #{endTime} and `to_address` != '') "
    )
    @Options(useCache = false)
    List<String> getAddressListByBlockTime(Long startTime, Long endTime);

    @Select("select from_address, tx_hash from tbl_transaction " +
            "where tx_type = 1 and contract_address = #{address}")
    Transaction getContractCreateTx(String address);

    @Select("select max(block_number) from tbl_transaction")
    @Options(useCache = false)
    Long getMaxBlockNumber();

    @Select("select " + COLS + " from tbl_transaction where block_number > #{startNumber} and block_number <= #{endNumber} " +
            "order by block_number")
    @Options(useCache = false)
    List<Transaction> getTransactionListByBlockNumber(Long startNumber, Long endNumber);

    @Select("<script> " +
            "select a.*, b.type as to_address_type from tbl_transaction a " +
            "left join tbl_account b on a.to_address = b.address " +
            "<where> 1 = 1 " +
            "<if test='address != null'>AND (a.to_address = #{address} or a.from_address = #{address} or a.contract_address = #{address}) </if> " +
            "<if test='startTime != null'>AND (a.block_time &gt;= #{startTime}) </if> " +
            "<if test='endTime != null'>AND (a.block_time &lt;= #{endTime}) </if> " +
            "</where>" +
            " order by block_number desc, tx_index limit 5000" +
            "</script>")
    @Options(useCache = false)
    List<ResponseTransaction> getLatest5KTransactionList(@Param("address") String address, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    @Select("<script> " +
            "select from_address as chainAccountAddress, contract_address as contractAddress, create_time  from tbl_transaction " +
            "<where>  from_address in " +
            "<foreach collection='userAddressList' item='userAddress' separator=',' open=' (' close=') ' > " +
            "#{userAddress} " +
            "</foreach> " +
            "<if test='contractAddress != null'> AND contract_address = #{contractAddress} </if> " +
            "<if test='startTime != null and endTime != null'> AND create_time between #{startTime} and #{endTime} </if> " +
            "</where>" +
            " order by contractAddress desc limit #{offset}, #{pageSize}" +
            "</script>")
    List<ResponseUserContractInfo> getContractByCreatorAddress(List<String> userAddressList, String contractAddress, Date startTime, Date endTime, Integer offset, Integer pageSize);

    @Select("<script> " +
            "select count(contract_address) from tbl_transaction  " +
            "<where>  from_address in " +
            "<foreach collection='userAddressList' item='userAddress' separator=',' open='(' close=')' > " +
            "#{userAddress} " +
            "</foreach> " +
            "<if test='contractAddress != null'> AND contract_address = #{contractAddress} </if> " +
            "<if test='startTime != null and endTime != null'> AND create_time between #{startTime} and #{endTime} </if> " +
            "</where>" +
            "</script>")
    Integer getContractCountByCreatorAddress(List<String> userAddressList, String contractAddress, Date startTime, Date endTime);
}
