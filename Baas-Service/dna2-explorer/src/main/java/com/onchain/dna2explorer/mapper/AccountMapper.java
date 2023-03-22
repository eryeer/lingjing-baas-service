package com.onchain.dna2explorer.mapper;


import com.onchain.dna2explorer.model.dao.Account;
import com.onchain.dna2explorer.model.response.ResponseAddress;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AccountMapper {

    String COLS = " id, create_time, update_time, status, address, type, balance, tx_count, nonce, block_time ";

    @Insert("<script>" +
            "insert into tbl_account(address, type, balance, tx_count, nonce, block_time) values " +
            "<foreach collection='addresses' item='item' separator=','> " +
            "( #{item.address},  #{item.type},  #{item.balance}, #{item.txCount}, #{item.nonce}, #{item.blockTime} ) " +
            "</foreach> " +
            "on duplicate key update " +
            "  balance = VALUES(balance), tx_count= tx_count + VALUES(tx_count), nonce = VALUES(nonce) " +
            "</script>")
    void batchMerge(@Param("addresses") List<Account> addresses);

    @Insert("<script>" +
            "insert ignore into tbl_account(address, type, balance, tx_count, nonce, block_time) values " +
            "<foreach collection='addresses' item='item' separator=','> " +
            "( #{item.address},  #{item.type},  #{item.balance}, #{item.txCount}, #{item.nonce}, #{item.blockTime} ) " +
            "</foreach> " +
            "</script>")
    void batchInsertIgnore(@Param("addresses") List<Account> addresses);

    @Select("select " + COLS + " from tbl_account order by tx_count desc")
    List<ResponseAddress> getAddressList();

    @Select({"<script>",
            "SELECT ", COLS, ", (select count(1) from tbl_transaction where from_address = address and tx_type =1) as deployCount ",
            " FROM tbl_account",
            " WHERE address IN",
            "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<ResponseAddress> getAddressListByAddress(List<String> list);

    @Select("select " + COLS + " from tbl_account")
    List<Account> getAccountList();

    @Select("select " + COLS + " from tbl_account where address = #{address}")
    ResponseAddress getAddress(String address);

    @Insert("<script>" +
            "insert into tbl_account(address, type, balance, tx_count, nonce, block_time) values " +
            "( #{item.address},  #{item.type},  #{item.balance}, #{item.txCount}, #{item.nonce}, #{item.blockTime} ) " +
            "on duplicate key update " +
            "  balance = VALUES(balance), nonce = VALUES(nonce) " +
            "</script>")
    void merge(@Param("addresses") Account item);
}
