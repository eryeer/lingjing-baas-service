package com.onchain.dna2explorer.utils;

import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.model.dao.Block;
import com.onchain.dna2explorer.model.dao.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ConverterFunctionUtil {
    Function<EthBlock.Block, Block> toDbBlock
            = blk -> Block.builder()
            .blockNumber(blk.getNumber().longValue())
            .blockHash(blk.getHash())
            .blockTime(blk.getTimestamp().longValue() * Constant.TimestampFactor)
            .miner(blk.getMiner())
            .difficulty(blk.getDifficulty().longValue())
            .totalDifficulty(blk.getTotalDifficulty().longValue())
            .blockSize(blk.getSize().intValue())
            .gasUsed(blk.getGasUsed().longValue())
            .gasLimit(blk.getGasLimit().longValue())
            .nonce(blk.getNonceRaw())
            .extraData(blk.getExtraData())
            .uncleHash(blk.getSha3Uncles())
            .parentHash(blk.getParentHash())
            .stateRoot(blk.getStateRoot())
            .receiptsRoot(blk.getReceiptsRoot())
            .transactionsRoot(blk.getTransactionsRoot())
            .txCount(blk.getTransactions().size())
            .build();

    Function<EthBlock.Block, List<Transaction>> toTransactions
            = blk -> blk.getTransactions().stream()
            .map(txResult -> {
                EthBlock.TransactionObject tx = (EthBlock.TransactionObject) txResult;
                return Transaction.builder()
                        .txHash(tx.getHash())
                        .blockHash(tx.getBlockHash())
                        .blockNumber(tx.getBlockNumber().longValue())
                        .fromAddress(tx.getFrom())
                        .toAddress(tx.getTo() == null ? "" : tx.getTo())
                        .txValue(tx.getValue().divide(Constant.GWeiFactor).toString())
                        .blockTime(blk.getTimestamp().longValue() * Constant.TimestampFactor)
                        .nonce(tx.getNonce().intValue())
                        .txIndex(tx.getTransactionIndex().intValue())
                        .data(tx.getInput())
                        .gasPrice(tx.getGasPrice().longValue())
                        .gasLimit(tx.getGas().longValue())
                        .build();
            })
            .collect(Collectors.toList());

//    BiFunction
//
//    Function<EthBlock.Block, List<Account>> toAccounts
//            = blk -> {
//        Set<Account> accountSet = new HashSet<>();
//        accountSet.add(Account.builder().address(blk.getMiner()).type(0).blockTime(new Date(blk.getTimestamp().longValue())).build());
//        for (EthBlock.TransactionResult txResult : blk.getTransactions()) {
//            EthBlock.TransactionObject tx = (EthBlock.TransactionObject) txResult;
//            if (null != tx.getTo()) {
//                accountSet.add(Account.builder().address(tx.getTo()).type(0).blockTime(new Date(blk.getTimestamp().longValue())).build());
//            }
//            if (null != tx.getFrom()) {
//                accountSet.add(Account.builder().address(tx.getFrom()).type(0).blockTime(new Date(blk.getTimestamp().longValue())).build());
//            }
//        }
//        return new ArrayList<>(accountSet);
//    };

    Function<List<org.web3j.protocol.core.methods.response.Transaction>, List<Transaction>> toPendingTransaction
            = txs -> txs.stream()
            .map(tx -> Transaction.builder()
                    .txHash(tx.getHash())
                    .blockHash(tx.getBlockHash())
                    .blockNumber(tx.getBlockNumber().longValue())
                    .fromAddress(tx.getFrom())
                    .toAddress(tx.getTo())
                    .txValue(tx.getValue().divide(Constant.GWeiFactor).toString())
                    .nonce(tx.getNonce().intValue())
                    .txIndex(tx.getTransactionIndex().intValue())
                    .data(tx.getInput())
                    .gasPrice(tx.getGasPrice().longValue())
                    .gasLimit(tx.getGas().longValue())
                    .build())
            .collect(Collectors.toList());
}
