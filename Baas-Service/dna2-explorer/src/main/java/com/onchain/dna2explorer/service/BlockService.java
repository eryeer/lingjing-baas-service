package com.onchain.dna2explorer.service;

import com.github.pagehelper.PageInfo;
import com.onchain.dna2explorer.mapper.AccountMapper;
import com.onchain.dna2explorer.mapper.BlockMapper;
import com.onchain.dna2explorer.mapper.TransactionMapper;
import com.onchain.dna2explorer.mapper.TxLogMapper;
import com.onchain.dna2explorer.model.dao.Account;
import com.onchain.dna2explorer.model.dao.Block;
import com.onchain.dna2explorer.model.dao.Transaction;
import com.onchain.dna2explorer.model.dao.TxLog;
import com.onchain.dna2explorer.model.response.ResponseBlock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BlockService {

    private final BlockMapper blockMapper;
    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;
    private final TxLogMapper txLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void storeBlock(Block block, List<Transaction> transactions, List<Account> accounts, List<TxLog> logs) {
        log.info("####### storeBlock start.");
        blockMapper.insert(block);

        if (!transactions.isEmpty()) {
            transactionMapper.insert(transactions);
        }
        if (!logs.isEmpty()) {
            txLogMapper.insert(logs);
        }
        if (!accounts.isEmpty()) {
            accountMapper.batchMerge(accounts);
        }
    }

    public PageInfo<ResponseBlock> getBlockList(Integer pageNumber, Integer pageSize) {
        Long total = blockMapper.latestNumber();
        Long endNumber = total - (long) (pageNumber - 1) * pageSize;
        List<ResponseBlock> list = blockMapper.getBlockListByNumber(endNumber - pageSize, endNumber);
        PageInfo<ResponseBlock> result = new PageInfo<>(list);
        result.setTotal(total);
        return result;
    }

    public Block getBlock(Integer blockNumber) {
        return blockMapper.getBlock(blockNumber);
    }
}
