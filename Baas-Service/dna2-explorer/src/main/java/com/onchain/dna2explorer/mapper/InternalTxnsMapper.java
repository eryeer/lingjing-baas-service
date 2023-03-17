package com.onchain.dna2explorer.mapper;

import com.onchain.dna2explorer.model.dao.InternalTxn;
import com.onchain.dna2explorer.model.response.ResponseInternalTx;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InternalTxnsMapper {
    String INSERT_COLS = " type, value, from_address, to_address, gas, gas_used, input, output, error, parent_id, tx_hash, block_time, block_number ";
    String COLS = " id, create_time, update_time, status, " + INSERT_COLS;

    @Insert("insert into tbl_internal_txns (" + INSERT_COLS + ") " +
            "values " +
            "(#{internalTxn.type}, #{internalTxn.value}, #{internalTxn.fromAddress}, #{internalTxn.toAddress}, #{internalTxn.gas}, " +
            "#{internalTxn.gasUsed}, #{internalTxn.input}, #{internalTxn.output}, #{internalTxn.error}, " +
            " #{internalTxn.parentId}, #{internalTxn.txHash}, #{internalTxn.blockTime}, #{internalTxn.blockNumber});")
    @Options(useGeneratedKeys = true, keyProperty = "internalTxn.id", keyColumn = "id")
    Long insert(@Param("internalTxn") InternalTxn internalTxn);

    @Select("<script> " +
            "select " + COLS +
            "from tbl_internal_txns " +
            "<where> 1 = 1 " +
            "<if test='address != null'>AND (from_address = #{address} or to_address = #{address}) </if> " +
            "</where>" +
            " order by id desc " +
            "</script>")
    List<ResponseInternalTx> getInternalTxList(@Param("address") String address);

    @Select("select " + COLS +
            "from tbl_internal_txns " +
            "where tx_hash = #{txHash} " +
            " order by id ")
    List<ResponseInternalTx> getInternalTxListByTxHash(@Param("txHash") String txHash);
}
