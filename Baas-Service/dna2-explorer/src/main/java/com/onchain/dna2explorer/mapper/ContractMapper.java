package com.onchain.dna2explorer.mapper;


import com.onchain.dna2explorer.model.dao.Contract;
import com.onchain.dna2explorer.model.response.ResponseContract;
import com.onchain.entities.response.ResponseUserContractInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
//@CacheNamespace(implementation = com.onchain.dna2explorer.config.RedisCache.class)
public interface ContractMapper {

    String INSERT_COLS = " address, abi, block_time, creator, create_tx_hash, contract_type, token_name, token_symbol, decimals ";
    String INSERT_VALS = " #{item.address}, #{item.abi}, #{item.blockTime}, #{item.creator}, #{item.createTxHash}, #{item.contractType}, #{item.tokenName}, #{item.tokenSymbol}, #{item.decimals} ";
    String COLS = " id, create_time, update_time, status, " + INSERT_COLS;

    @Insert("<script>" +
            "insert into tbl_contract( " + INSERT_COLS + " ) values " +
            "<foreach collection='list' item='item' separator=','> " +
            "( " + INSERT_VALS + " ) " +
            "</foreach> " +
            "</script>")
    void batchInsert(@Param("list") List<Contract> list);

    @Update("<script>" +
            "<foreach collection='list' item='item' separator=';'> " +
            " update tbl_contract set abi = #{item.abi}, block_time = #{item.blockTime}, creator = #{item.creator}, create_tx_hash = #{item.createTxHash}, " +
            " contract_type = #{item.contractType}, token_name = #{item.tokenName}, token_symbol = #{item.tokenSymbol}, decimals = #{item.decimals} " +
            " where address = #{item.address} " +
            "</foreach> " +
            "</script>")
    void batchUpdate(@Param("list") List<Contract> list);

    @Select("select " + COLS + " from tbl_contract where address = #{address}")
    ResponseContract getResponseContract(String address);

    @Select("select " + COLS + " from tbl_contract where address = #{address}")
    Contract getContract(String address);

    @Select("<script> " +
            "select creator as chainAccountAddress, address as contractAddress, create_time  from tbl_contract " +
            "<where> status != '0' and creator in " +
            "<foreach collection='userAddressList' item='userAddress' separator=',' open=' (' close=') ' > " +
            "#{userAddress} " +
            "</foreach> " +
            "<if test='contractAddress != null'> AND address = #{contractAddress} </if> " +
            "<if test='startTime != null and endTime != null'> AND create_time between #{startTime} and #{endTime} </if> " +
            "</where>" +
            " order by contractAddress desc limit #{offset}, #{pageSize}" +
            "</script>")
    List<ResponseUserContractInfo> getContractByCreatorAddress(List<String> userAddressList, String contractAddress, Date startTime, Date endTime, Integer offset, Integer pageSize);

    @Select("<script> " +
            "select count(address) from tbl_contract  " +
            "<where> status != '0' and creator in " +
            "<foreach collection='userAddressList' item='userAddress' separator=',' open='(' close=')' > " +
            "#{userAddress} " +
            "</foreach> " +
            "<if test='contractAddress != null'> AND address = #{contractAddress} </if> " +
            "<if test='startTime != null and endTime != null'> AND create_time between #{startTime} and #{endTime} </if> " +
            "</where>" +
            "</script>")
    Integer getContractCountByCreatorAddress(List<String> userAddressList, String contractAddress, Date startTime, Date endTime);
}
