package com.onchain.dna2explorer.mapper;

import com.onchain.dna2explorer.model.dao.NFTHolder;
import com.onchain.dna2explorer.model.response.ResponseAccountNFTHolder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TokenMapper {
    String INSERT_COLS = " contract_address, account_address , count ";
    String BASIC_COLS = " id, create_time, update_time, status , " + INSERT_COLS;
    String INSERT_VALS = " #{contractAddress}, #{accountAddress}, #{count}";

    // 获取响应对象列表
    @Select("select " + BASIC_COLS + " from tbl_nft_holder order by count desc")
    List<ResponseAccountNFTHolder> getResponseNFTHolderList();

    // 获取列表
    @Select("select " + BASIC_COLS + " from tbl_nft_holder")
    List<NFTHolder> getNFTHolderList();

    //通过合约地址获取合约下每个账户的持有详情的响应对象列表
    @Select("  select " + BASIC_COLS + " from tbl_nft_holder where contract_address = #{contractAddress} and count > 0 order by count desc, id desc")
    List<ResponseAccountNFTHolder> getNFTHolderByContractAddress(String contractAddress);

    //通过合约地址获取合约下每个账户总数
    @Select("  select count(*) from tbl_nft_holder where contract_address = #{contractAddress} and count > 0 order by count desc, id desc")
    Long getNFTHolderSumByContractAddress(String contractAddress);

    //通过合约地址获取合约下每个账户id顺序列表
    @Select(" select id from tbl_nft_holder where contract_address = #{contractAddress} and count > 0 order by count desc, id desc")
    List<Long> getNFTHolderIdByContractAddress(String contractAddress);

    //通过合约地址获取合约的token总数
    @Select("select sum(count) from tbl_nft_holder where contract_address = #{contractAddress}")
    Long getNFTTokenSumByContractAddress(String contractAddress);

    //通过合约地址， 账户地址获取合约下账户的持有详情
    @Select("select " + BASIC_COLS + " from tbl_nft_holder where contract_address = #{contractAddress} and account_address = #{accountAddress} limit 1")
    NFTHolder getNFTHolderByContractAddressAndAccountAddress(String contractAddress, String accountAddress);

    @Insert("insert into tbl_nft_holder (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertNFTHolder(NFTHolder nftHolder);

    @Update("update tbl_nft_holder set count = #{count} where contract_address = #{contractAddress} and  account_address = #{accountAddress}")
    void updateNFTHolder(NFTHolder nftHolder);
}
