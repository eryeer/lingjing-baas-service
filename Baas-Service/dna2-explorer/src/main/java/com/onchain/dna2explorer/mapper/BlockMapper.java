package com.onchain.dna2explorer.mapper;

import com.onchain.dna2explorer.model.dao.Block;
import com.onchain.dna2explorer.model.dao.Summary;
import com.onchain.dna2explorer.model.response.ResponseBlock;
import com.onchain.dna2explorer.model.response.ResponseTotalSummary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlockMapper {

    String COLS = "id, create_time, update_time, status, block_number, block_hash, block_time, miner, difficulty, total_difficulty, block_size, gas_used, gas_limit, nonce, extra_data, uncle_hash, parent_hash, state_root, receipts_root, transactions_root, tx_count ";

    @Select("select max(block_number) from tbl_block")
    Long latestNumber();

    @Select("select max(block_time) from tbl_block")
    Long latestBlockTime();

    @Select("select max(block_number) as block_number, (select count(1) from tbl_transaction) as tx_count, (select count(1) from tbl_account) as address_count from tbl_block")
    ResponseTotalSummary getTotalSummary();

    @Select("select count(1) as block_count, (select count(1) from tbl_transaction where block_time >= #{startTime} and block_time < #{endTime}) as tx_count " +
            "from tbl_block where block_time >= #{startTime} and block_time < #{endTime}")
    Summary getSummary(Long startTime, Long endTime);

    @Insert("insert into tbl_block (block_number, block_hash, block_time, miner, difficulty, " +
            "total_difficulty, block_size, gas_used, gas_limit, " +
            "nonce, extra_data, parent_hash, uncle_hash, state_root, " +
            "receipts_root, transactions_root, tx_count) " +
            "values " +
            "(#{block.blockNumber}, #{block.blockHash}, #{block.blockTime}, #{block.miner}, #{block.difficulty}, " +
            "#{block.totalDifficulty}, #{block.blockSize}, #{block.gasUsed}, #{block.gasLimit}, " +
            "#{block.nonce}, #{block.extraData}, #{block.parentHash}, #{block.uncleHash}, #{block.stateRoot}, " +
            "#{block.receiptsRoot}, #{block.transactionsRoot}, #{block.txCount})")
    Integer insert(@Param("block") Block block);

    @Select("select " + COLS + " from tbl_block order by block_number desc")
    List<ResponseBlock> getBlockList();

    @Select("select " + COLS + " from tbl_block where block_number > #{startNumber} and block_number <= #{endNumber} order by block_number desc")
    List<ResponseBlock> getBlockListByNumber(Long startNumber, Long endNumber);

    @Select("select " + COLS + " from tbl_block where block_number = #{blockNumber}")
    Block getBlock(Integer blockNumber);

}
