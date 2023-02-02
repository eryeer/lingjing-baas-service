package com.onchain.dna2explorer.mapper;


import com.onchain.dna2explorer.model.dao.TxLog;
import com.onchain.dna2explorer.model.response.ResponseTxLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TxLogMapper {

    String INSERT = " tx_hash, tx_type, block_time, block_hash, block_number, log_index, tx_index, address, data, type, topics ";
    String INSERT_ITEM = " #{item.txHash}, #{item.txType}, #{item.blockTime}, #{item.blockHash}, #{item.blockNumber}, #{item.logIndex}, #{item.txIndex}, #{item.address}, #{item.data}, #{item.type}, #{item.topics} ";

    @Insert("<script>" +
            "insert into tbl_transaction_log (" + INSERT + ") " +
            "values " +
            "<foreach collection='list' item='item' separator=','> " +
            "(" + INSERT_ITEM + ") " +
            "</foreach>" +
            "</script>")
    Integer insert(@Param("list") List<TxLog> list);

    @Select("select " + INSERT + " from tbl_transaction_log where tx_hash = #{txHash}")
    List<ResponseTxLog> getTxLogList(String txHash);
}
