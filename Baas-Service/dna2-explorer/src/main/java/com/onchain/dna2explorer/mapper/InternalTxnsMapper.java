package com.onchain.dna2explorer.mapper;

import com.onchain.dna2explorer.model.dao.InternalTxn;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface InternalTxnsMapper {
    String INSERT_COLS = " type, value, from_address, to_address, gas, gas_used, input, output, error, revert_reason, parent_id, tx_hash, block_time, block_number ";
    String COLS = " id, create_time, update_time, status, " + INSERT_COLS;

    @Insert("insert into tbl_internal_txns (" + INSERT_COLS + ") " +
            "values " +
            "(#{internalTxn.type}, #{internalTxn.value}, #{internalTxn.fromAddress}, #{internalTxn.toAddress}, #{internalTxn.gas}, " +
            "#{internalTxn.gasUsed}, #{internalTxn.input}, #{internalTxn.output}, #{internalTxn.error}, " +
            "#{internalTxn.revertReason}, #{internalTxn.parentId}, #{internalTxn.txHash}, #{internalTxn.blockTime}, #{internalTxn.blockNumber});")
    @Options(useGeneratedKeys = true, keyProperty = "internalTxn.id", keyColumn = "id")
    Long insert(@Param("internalTxn") InternalTxn internalTxn);


}
