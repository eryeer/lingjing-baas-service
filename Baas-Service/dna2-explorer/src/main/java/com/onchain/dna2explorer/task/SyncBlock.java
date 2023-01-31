package com.onchain.dna2explorer.task;

import com.alibaba.fastjson.JSON;
import com.onchain.dna2explorer.lock.DistributedLockAnnotation;
import com.onchain.dna2explorer.mapper.BlockMapper;
import com.onchain.dna2explorer.model.dao.Account;
import com.onchain.dna2explorer.model.dao.Block;
import com.onchain.dna2explorer.model.dao.Transaction;
import com.onchain.dna2explorer.model.dao.TxLog;
import com.onchain.dna2explorer.service.AddressService;
import com.onchain.dna2explorer.service.BlockService;
import com.onchain.dna2explorer.utils.ConverterFunctionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncBlock {

    private final Web3j web3j;
    private final BlockMapper blockMapper;
    private final AddressService addressService;
    private final BlockService blockService;

    @Scheduled(fixedRate = 5000)
    @DistributedLockAnnotation("blockSync")
    public void blockSync() throws Exception {
        log.info("Staring block Sync");
        Long latest = blockMapper.latestNumber();
        long ethLatest = web3j.ethBlockNumber().send().getBlockNumber().longValue();
        if (null == latest) {
            latest = 0L;
        }

        for (long i = latest + 1; i < ethLatest + 1; i++) {
            handleBlock(i);
        }
    }

    public void handleBlock(Long blockNumber) throws Exception {
        EthBlock.Block blk = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true)
                .send().getBlock();

        Block block = ConverterFunctionUtil.toDbBlock.apply(blk);
        List<Transaction> transactions = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        List<TxLog> logs = new ArrayList<>();

        if (block.getTxCount() > 0) {
            transactions = ConverterFunctionUtil.toTransactions.apply(blk);
            Map<String, TransactionReceipt> transactionReceiptMap = updateTransactions(transactions);
            accounts = getAddresses(transactionReceiptMap, block);
            addressService.updateAccounts(accounts);
            logs = getAllLogs(block, transactionReceiptMap);
        }
        blockService.storeBlock(block, transactions, accounts, logs);
        log.info("block {} sync succeeded", blockNumber);
    }

    private Map<String, TransactionReceipt> updateTransactions(List<Transaction> transactions) throws Exception {
        Map<String, TransactionReceipt> result = new ConcurrentHashMap<>();
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Transaction tx : transactions) {
            futures.add(web3j.ethGetTransactionReceipt(tx.getTxHash()).sendAsync()
                    .thenApply(receipt -> {
                        TransactionReceipt r = receipt.getTransactionReceipt().get();
                        tx.setTxType(null == r.getContractAddress() ? 0 : 1);
                        tx.setContractAddress(r.getContractAddress());
                        tx.setGasUsed(r.getGasUsed().longValue());
                        tx.setTxStatus(r.getStatus() == null ? "" : r.getStatus());

                        result.putIfAbsent(tx.getTxHash(), r);
                        return receipt;
                    })
            );
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
        return result;
    }

    private List<Account> getAddresses(Map<String, TransactionReceipt> transactionReceiptMap, Block block) {
        Map<String, Account> result = new HashMap<>();

        if (null != block.getMiner()) {
            Account item = getDefaultAccount(block.getMiner(), 0, block.getBlockTime());
            result.put(item.getAddress(), item);
        }

        for (TransactionReceipt receipt : transactionReceiptMap.values()) {
            if (null != receipt.getFrom()) {
                Account item = getDefaultAccount(receipt.getFrom(), 0, block.getBlockTime());
                result.put(item.getAddress(), item);
            }
            if (null != receipt.getTo()) {
                Account item = getDefaultAccount(receipt.getTo(), 0, block.getBlockTime());
                result.put(item.getAddress(), item);
            }

            // 合约账户
            if (null != receipt.getContractAddress()) {
                Account item = getDefaultAccount(receipt.getContractAddress(), 1, block.getBlockTime());
                result.put(item.getAddress(), item);
            }
        }

        for (Account item : result.values()) {
            // calculate tx count with from and to address
            item.setTxCount(transactionReceiptMap.values().stream()
                    .filter(p -> item.getAddress().equals(p.getFrom()) || item.getAddress().equals(p.getTo()) || item.getAddress().equals(p.getContractAddress()))
                    .count());
        }

        return new ArrayList<>(result.values());
    }

    private Account getDefaultAccount(String address, Integer type, Long blockTime) {
        return Account.builder()
                .address(address)
                .type(type)
                .blockTime(blockTime)
                .nonce(0)
                .build();
    }

    private List<TxLog> getAllLogs(Block block, Map<String, TransactionReceipt> transactionReceiptMap) {
        Set<TxLog> result = new HashSet<>();

        for (TransactionReceipt receipt : transactionReceiptMap.values()) {
            if (receipt.getLogs() != null && !receipt.getLogs().isEmpty()) {
                for (Log item : receipt.getLogs()) {
                    result.add(TxLog.builder()
                            .txHash(item.getTransactionHash())
                            .txType(null == receipt.getContractAddress() ? 0 : 1)
                            .blockTime(block.getBlockTime())
                            .blockHash(item.getBlockHash())
                            .blockNumber(item.getBlockNumber().longValue())
                            .logIndex(item.getLogIndex().intValue())
                            .txIndex(receipt.getTransactionIndex().intValue())
                            .address(item.getAddress())
                            .data(item.getData())
                            .type(item.getType() == null ? "" : item.getType())
                            .topics(JSON.toJSONString(item.getTopics()))
                            .build());
                }
            }
        }
        return new ArrayList<>(result);
    }

}
