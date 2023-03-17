package com.onchain.dna2explorer.service;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.exception.CommonException;
import com.onchain.dna2explorer.mapper.*;
import com.onchain.dna2explorer.model.dao.*;
import com.onchain.dna2explorer.model.response.*;
import com.onchain.dna2explorer.utils.CsvUtil;
import com.onchain.dna2explorer.utils.EthUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.AbiDefinition;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.onchain.dna2explorer.constants.CommonConst.TBL_TRANSACTION_BLOCK_HEIGHT_FOR_TRANSFER;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final TxLogMapper txLogMapper;
    private final MethodMapMapper methodMapMapper;
    private final ContractMapper contractMapper;
    private final AccountMapper accountMapper;
    private final TransferMapper transferMapper;
    private final TableHeightMapper tableHeightMapper;
    private final InternalTxnsService internalTxnsService;

    public PageInfo<ResponseTransaction> getTransactionList(Integer pageNumber, Integer pageSize, Long blockNumber) {
//        PageHelper.startPage(pageNumber, pageSize);
        List<ResponseTransaction> list = transactionMapper.getTransactionList(blockNumber, null, pageSize * (pageNumber - 1), pageSize);
        setTxMethodName(list);
        PageInfo<ResponseTransaction> result = new PageInfo<>(list);
        Integer total = transactionMapper.getTransactionCount(blockNumber, null);
        result.setTotal(total);
        return result;
    }

    public PageInfo<ResponseTransaction> getTransactionListByAddress(Integer pageNumber, Integer pageSize, String address) {
//        PageHelper.startPage(pageNumber, pageSize);
        List<ResponseTransaction> list = transactionMapper.getTransactionList(null, address, pageSize * (pageNumber - 1), pageSize);
        setTxMethodName(list);
        PageInfo<ResponseTransaction> result = new PageInfo<>(list);
        Integer total = transactionMapper.getTransactionCount(null, address);
        result.setTotal(total);
        return result;
    }

    public void getLatest5kTransactionListByAddress(String address, long startTime, long endTime, HttpServletResponse response) throws CommonException {
        List<ResponseTransaction> transactionList = transactionMapper.getLatest5KTransactionList(address, startTime, endTime);
        setTxMethodName(transactionList);
        CsvUtil.transactionsToCsvOutputStream(transactionList, response);
    }

    public void setTxMethodName(List<ResponseTransaction> list) {
        // get method name if the abi has been uploaded
        for (ResponseTransaction tx : list) {
            if (tx.getTxType() == 1) {
                tx.setMethod(Constant.CONTRACT_CREATION);
            } else if (tx.getToAddressType() == 0) {
                tx.setMethod(Constant.TRANSFER);
            } else if (tx.getData().length() > 10) {
                tx.setMethod(tx.getData().substring(0, 10));
                Contract contract = contractMapper.getContract(tx.getToAddress());
                if (contract != null) {
                    tx.setMethod(getMethodName(contract.getAbi(), tx.getMethod()));
                }
            }
        }
    }

    private String getMethodName(String abiStr, String methodId) {
        List<AbiDefinition> abiDefinitions = JSONArray.parseArray(abiStr, AbiDefinition.class);
        return abiDefinitions.stream().filter(p -> Constant.TYPE_FUNCTION.equals(p.getType()) &&
                        StringUtils.equals(methodId, EthUtil.getMethodId(EthUtil.buildMethodHash(EthUtil.buildMethodSignature(p.getName(), p.getInputs())))))
                .map(AbiDefinition::getName).findFirst().orElse(methodId);
    }

    public ResponseTransaction getTransaction(String txHash) {
        ResponseTransaction tx = transactionMapper.getTransaction(txHash);
        List<ResponseTxLog> txLogList = txLogMapper.getTxLogList(txHash);
        setFullEventName(txLogList);
        tx.setLogList(txLogList);

        // ERC20/ERC721 转账记录
        List<ResponseTransferLog> transferLogs = getTransferLogList(tx);
        tx.setErcTransferLog(transferLogs);

        // 内部交易列表
        List<ResponseInternalTx> internalTxList = internalTxnsService.getInternalTxListByTxHash(txHash);
        tx.setInternalTxns(internalTxList);
        return tx;
    }

    private void setFullEventName(List<ResponseTxLog> txLogList) {
        for (ResponseTxLog responseTxLog : txLogList) {
            ResponseContract contract = contractMapper.getResponseContract(responseTxLog.getAddress());
            responseTxLog.setTopicList(JSONArray.parseArray(responseTxLog.getTopics(), String.class));
            if (contract == null || contract.getAbi().isEmpty()) {
                continue;
            }
            MethodMap methodMapByHash = methodMapMapper.getMethodMapByHash(responseTxLog.getTopicList().get(0));
            if (methodMapByHash == null) {
                continue;
            }
            List<AbiDefinition> abiDefinitions = JSONArray.parseArray(contract.getAbi(), AbiDefinition.class);
            List<AbiDefinition> filteredAbiDefinition = abiDefinitions.stream().filter(p -> StringUtils.equals(p.getName(), methodMapByHash.getMethodName()))
                    .filter(p -> StringUtils.equals(EthUtil.buildMethodSignature(p.getName(), p.getInputs()), methodMapByHash.getMethodSignature())).collect(Collectors.toList());
            if (filteredAbiDefinition.size() == 0) {
                log.warn("method sig {} not found in log translation", methodMapByHash.getMethodSignature());
            } else if (filteredAbiDefinition.size() > 1) {
                log.warn("method sig {} not found more than once in log translation", methodMapByHash.getMethodSignature());
            } else {
                responseTxLog.setEventName(EthUtil.buildMethodFullName(filteredAbiDefinition.get(0).getName(), filteredAbiDefinition.get(0).getInputs()));
            }
        }
    }

    private List<ResponseTransferLog> getTransferLogList(ResponseTransaction tx) {
        List<ResponseTransferLog> result = new ArrayList<>();
        List<ResponseTxLog> txLogList = tx.getLogList();
        for (ResponseTxLog item : txLogList) {
            List<String> topics = item.getTopicList();
            if (StringUtils.equals(topics.get(0), Constant.ERC20_TRANSFER_HASH)) {
                if (topics.size() <= 3) { // filter only ERC721
                    continue;
                }
                ResponseTransferLog transferLog = ResponseTransferLog.builder()
                        .address(item.getAddress())
                        .fromAddress(EthUtil.getAddressFromTopic(topics.get(1)))
                        .toAddress(EthUtil.getAddressFromTopic(topics.get(2)))
                        .build();
                if (topics.size() == 3) {
                    transferLog.setAmount(EthUtil.getUIntFromTopic(item.getData()));
                    transferLog.setContractType(Constant.ERC20);
                } else {
                    transferLog.setTokenId(EthUtil.getUIntFromTopic(topics.get(3)));
                    transferLog.setContractType(Constant.ERC721);
                }

                Contract contract = contractMapper.getContract(item.getAddress());
                if (contract != null) {
                    transferLog.setTokenName(contract.getTokenName());
                    transferLog.setTokenSymbol(contract.getTokenSymbol());
                    transferLog.setDecimals(contract.getDecimals());
                }
                result.add(transferLog);
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncTransferList(Long startNumber, Long endNumber) {
        List<Transaction> txs = transactionMapper.getTransactionListByBlockNumber(startNumber, endNumber);
        for (Transaction tx : txs) {
            List<Transfer> transfers = getERC721TransferList(tx);
            List<Account> accounts = getAddresses(transfers);
            if (!transfers.isEmpty()) {
                transferMapper.batchInsertIgnore(transfers);
            }
            if (!accounts.isEmpty()) {
                accountMapper.batchInsertIgnore(accounts);
            }
        }
        tableHeightMapper.updateTableHeightByTableName(TBL_TRANSACTION_BLOCK_HEIGHT_FOR_TRANSFER, endNumber);
    }

    private List<Transfer> getERC721TransferList(Transaction tx) {
        List<Transfer> result = new ArrayList<>();
        List<ResponseTxLog> txLogList = txLogMapper.getTxLogList(tx.getTxHash());
        for (ResponseTxLog item : txLogList) {
            List<String> topics = JSONArray.parseArray(item.getTopics(), String.class);
            if (!topics.isEmpty() && StringUtils.equals(topics.get(0), Constant.ERC20_TRANSFER_HASH) && topics.size() == 4) {
                Transfer transfer = Transfer.builder()
                        .blockHash(tx.getBlockHash())
                        .blockNumber(tx.getBlockNumber())
                        .blockTime(tx.getBlockTime())
                        .fromAddress(tx.getFromAddress())
                        .toAddress(tx.getToAddress())
                        .transferFrom(EthUtil.getAddressFromTopic(topics.get(1)))
                        .transferTo(EthUtil.getAddressFromTopic(topics.get(2)))
                        .contractAddress(item.getAddress())
                        .tokenId(EthUtil.getUIntFromTopic(topics.get(3)))
                        .txHash(tx.getTxHash())
                        .logIndex(item.getLogIndex())
                        .fromType(0)
                        .toType(0)
                        .build();
                ResponseAddress transferFrom = accountMapper.getAddress(transfer.getTransferFrom());
                if (transferFrom != null && transferFrom.getType() != 0) {
                    transfer.setFromType(transferFrom.getType());
                }
                ResponseAddress transferTo = accountMapper.getAddress(transfer.getTransferTo());
                if (transferTo != null && transferTo.getType() != 0) {
                    transfer.setToType(transferTo.getType());
                }
                result.add(transfer);
            }
        }
        return result;
    }

    private List<Account> getAddresses(List<Transfer> transfers) {
        Map<String, Account> result = new HashMap<>();

        for (Transfer transfer : transfers) {
            if (null != transfer.getTransferFrom()) {
                Account item = getDefaultAccount(transfer.getTransferFrom(), 0, transfer.getBlockTime());
                result.put(item.getAddress(), item);
            }
            if (null != transfer.getTransferTo()) {
                Account item = getDefaultAccount(transfer.getTransferTo(), 0, transfer.getBlockTime());
                result.put(item.getAddress(), item);
            }
        }
        return new ArrayList<>(result.values());
    }

    private Account getDefaultAccount(String address, Integer type, Long blockTime) {
        return Account.builder()
                .address(address)
                .type(type)
                .blockTime(blockTime)
                .balance("0")
                .nonce(0)
                .txCount(0L)
                .build();
    }
}
