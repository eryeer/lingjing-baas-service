package com.onchain.mapper;


import com.onchain.entities.dao.ChainAccount;
import com.onchain.entities.response.ResponseChainAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface ChainAccountMapper {

    String INSERT_COLS = " user_id, user_address, is_gas_transfer, is_custody, encode_key, name";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{userAddress}, #{isGasTransfer}, #{isCustody}, #{encodeKey}, #{name}";

    //根据用户id查询链账户作为响应对象
    @Select("select " + BASIC_COLS + " from tbl_chain_account where user_id= #{userId} and status != 0")
    List<ResponseChainAccount> getChainAccountByUserId(String userId);

    //添加链账户
    @Insert("insert into tbl_chain_account (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertChainAccount(ChainAccount account);

    //根据链户Id更新链账户
    @Update("update tbl_chain_account set user_address = #{userAddress}, user_id = #{userId}, encode_key = #{encodeKey} , name = #{name}, is_gas_transfer = #{isGasTransfer} " +
            "where id = #{id}")
    void updateChainAccount(ChainAccount account);

    //根据链户Id查询链户
    @Select("select " + BASIC_COLS + " from tbl_chain_account where id= #{id} and status != 0 limit 1")
    ChainAccount getChainAccountById(Long id);

    //根据链账户地址查询链户
    @Select("select " + BASIC_COLS + " from tbl_chain_account where user_address= #{address} and status != 0 limit 1")
    ResponseChainAccount getChainAccountByAddress(String address);

    //根据链户id更新加密后的私钥
    @Update("update tbl_chain_account set encode_key = #{encodeKey} " +
            "where id = #{id}")
    void updateEncodeKey(Long id, String encodeKey);

    @Select("<script> " +
            "select " + BASIC_COLS + " from tbl_chain_account " +
            "<where> status != '0' and user_id = #{userId} " +
            "<if test='name != null'>AND name = #{name} </if> " +
            "<if test='userAddress != null'> AND user_Address = #{userAddress} </if> " +
            "<if test='isGasTransfer != null'> AND is_Gas_Transfer = #{isGasTransfer} </if> " +
            "<if test='isCustody != null'> AND is_custody = #{isCustody} </if> " +
            "<if test='startTime != null and endTime != null'> AND create_time between #{startTime} and #{endTime} </if> " +
            "</where>" +
            " order by update_time desc " +
            "</script>")
    List<ResponseChainAccount> getChainAccount(String userId, String name, String userAddress, Boolean isGasTransfer, Boolean isCustody, Date startTime, Date endTime);

    // 根据用户id和链户id查询地址列表
    @Select("<script> " +
            "select " + BASIC_COLS + " from tbl_chain_account where user_Id = #{userId} and status != 0 and id in " +
            "<foreach collection='ids' index='index' item='item' open='(' separator=',' close=')'> " +
            "#{item} " +
            "</foreach> " +
            "</script>")
    List<ChainAccount> getUserAccountListById(String userId, List<Long> ids);

    // 根据用户id和链户id更新转账和删除状态
    @Update("<script> " +
            "update tbl_chain_account set status = #{status}, is_gas_transfer = #{isGasTransfer} " +
            "where user_id = #{userId} and id in " +
            "<foreach collection='ids' index='index' item='item' open='(' separator=',' close=')'> " +
            "#{item} " +
            "</foreach> " +
            "</script>")
    void updateAccountStatusById(String userId, List<Long> ids, String status, Boolean isGasTransfer);
}
