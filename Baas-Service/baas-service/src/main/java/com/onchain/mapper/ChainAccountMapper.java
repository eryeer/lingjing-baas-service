package com.onchain.mapper;


import com.onchain.entities.dao.ChainAccount;
import com.onchain.entities.dao.User;
import com.onchain.entities.response.ResponseChainAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ChainAccountMapper {

    String INSERT_COLS = " user_id, user_address, is_gas_transfer, encode_key, name";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{userAddress}, #{isGasTransfer}, #{encodeKey}, #{name}";

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

}
