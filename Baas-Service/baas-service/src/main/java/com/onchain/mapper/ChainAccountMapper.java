package com.onchain.mapper;


import com.onchain.entities.dao.ChainAccount;
import com.onchain.entities.response.ResponseChainAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ChainAccountMapper {

    String INSERT_COLS = " user_id, user_address, balance, apply_time, private_key, wallet_pass, wallet_file_uuid ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{userAddress}, #{balance}, #{applyTime}, #{privateKey}, #{walletPass}, #{walletFileUuid} ";

    //根据用户id查询链账户
    @Select("select " + BASIC_COLS + " from tbl_chain_account where user_id= #{userId} limit 1")
    ResponseChainAccount getChainAccountByUserId(String userId);

    @Select("select " + BASIC_COLS + " from tbl_chain_account where user_id= #{userId} limit 1")
    ChainAccount getChainAccount(String userId);

    //添加链账户
    @Insert("insert into tbl_chain_account (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertChainAccount(ChainAccount account);

    //根据用户Id更新链账户
    @Update("update tbl_chain_account set user_address = #{userAddress}, balance = #{balance}, apply_time = #{applyTime}, private_key = #{privateKey}, wallet_pass = #{walletPass}, wallet_file_uuid = #{walletFileUuid} " +
            "where user_id = #{userId}")
    void updateChainAccount(ChainAccount account);

}
