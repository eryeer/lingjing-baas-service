package com.onchain.dna2explorer.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.dna2explorer.config.Web3jConfig;
import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.mapper.AccountMapper;
import com.onchain.dna2explorer.mapper.InternalTxnsMapper;
import com.onchain.dna2explorer.mapper.TableHeightMapper;
import com.onchain.dna2explorer.mapper.TransactionMapper;
import com.onchain.dna2explorer.model.dao.Account;
import com.onchain.dna2explorer.model.dao.InternalTxn;
import com.onchain.dna2explorer.model.dao.Transaction;
import com.onchain.dna2explorer.model.response.ResponseDebugTraceTransaction;
import com.onchain.dna2explorer.model.response.ResponseInternalTx;
import com.onchain.dna2explorer.model.response.ResponseRpc;
import com.onchain.dna2explorer.utils.EthUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.List;

import static com.onchain.dna2explorer.constant.Constant.DEBUG_TRACETRANSACTION_PARAMS;
import static com.onchain.dna2explorer.constants.CommonConst.TBL_TRANSACTION_BLOCK_HEIGHT_FOR_INTERNAL_TXNS;

@Slf4j
@Service
@AllArgsConstructor
public class InternalTxnsService {
    private final InternalTxnsMapper internalTxnsMapper;
    private final TransactionMapper transactionMapper;
    private final TableHeightMapper tableHeightMapper;
    private final Web3jConfig web3jConfig;

    private final AccountMapper accountMapper;

    private final Web3j web3j;


    @Transactional(rollbackFor = Exception.class)
    public void syncInternalTransaction(Long startNumber, Long endNumber) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<Transaction> txs = transactionMapper.getTransactionListByBlockNumber(startNumber, endNumber);
        HttpPost httpPost = new HttpPost(web3jConfig.getSyncNodeUrl());
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        for (Transaction tx : txs) {
            if (StringUtils.isEmpty(tx.getData()) || tx.getData().equals("0x")) {
                continue;
            }
            String txHash = tx.getTxHash();
            String params = String.format(DEBUG_TRACETRANSACTION_PARAMS, txHash);
            StringEntity entity = new StringEntity(params, ContentType.create("text/json", "UTF-8"));
            httpPost.setEntity(entity);
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("network error or request error");
            }

            String output = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            ResponseRpc<ResponseDebugTraceTransaction> object = JSONObject.parseObject(output, new TypeReference<ResponseRpc<ResponseDebugTraceTransaction>>() {
            });
            if (!StringUtils.isEmpty(object.getError())) {
                log.error("transaction hash: " + txHash + ", error is " + object.getError());
                throw new RuntimeException(object.getError());
            }
            ResponseDebugTraceTransaction result = object.getResult();
            if (null != result && null != result.getCalls()) {
                for (ResponseDebugTraceTransaction subCall : result.getCalls()) {
                    parseInsert(tx, subCall, 0L);
                }
            }

        }
        tableHeightMapper.updateTableHeightByTableName(TBL_TRANSACTION_BLOCK_HEIGHT_FOR_INTERNAL_TXNS, endNumber);
    }


    private void parseInsert(Transaction tx, ResponseDebugTraceTransaction result, Long parentId) throws Exception {
        InternalTxn internalTxn = InternalTxn.builder().blockNumber(tx.getBlockNumber())
                .blockTime(tx.getBlockTime())
                .fromAddress(result.getFrom())
                .toAddress(result.getTo())
                .input(result.getInput())
                .output(result.getOutput())
                .type(result.getType())
                .parentId(parentId)
                .txHash(tx.getTxHash())
                .error(result.getError())
                .build();
        if (StringUtils.isEmpty(result.getError())) {
            internalTxn.setError("");
        }
        String value = result.getValue();
        if (StringUtils.isEmpty(value) || value.equals("0x")) {
            internalTxn.setValue("0");
        } else {
            internalTxn.setValue(Numeric.decodeQuantity(result.getValue()).divide(Constant.GWeiFactor).toString());
        }
        String gas = result.getGas();
        if (StringUtils.isEmpty(gas) || gas.equals("0x")) {
            internalTxn.setGas(0L);
        } else {
            internalTxn.setGas(Numeric.decodeQuantity(result.getGas()).longValue());
        }
        String gasUsed = result.getGasUsed();
        if (StringUtils.isEmpty(gasUsed) || gasUsed.equals("0x")) {
            internalTxn.setGasUsed(0L);
        } else {
            internalTxn.setGasUsed(Numeric.decodeQuantity(result.getGasUsed()).longValue());
        }
        if (StringUtils.isEmpty(result.getOutput())) {
            internalTxn.setOutput("0x");
        }
        if (StringUtils.isEmpty(result.getInput())) {
            internalTxn.setInput("0x");
        }
        internalTxnsMapper.insert(internalTxn);
        if (internalTxn.getType().equals("CREATE") || internalTxn.getType().equals("CREATE2")) {
            Account account = Account.builder()
                    .address(internalTxn.getToAddress())
                    .type(1)
                    .blockTime(tx.getBlockTime())
                    .nonce(0)
                    .txCount(0L)
                    .build();
            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(account);
            EthUtil.updateAccounts(web3j, accounts);
            accountMapper.merge(account);
        }
        Long id = internalTxn.getId();
        if (null != result.getCalls()) {
            for (ResponseDebugTraceTransaction subCall : result.getCalls()) {
                parseInsert(tx, subCall, id);
            }
        }
    }

    public PageInfo<ResponseInternalTx> getInternalTxListByAddress(Integer pageNumber, Integer pageSize, String address) {
        PageHelper.startPage(pageNumber, pageSize);
        List<ResponseInternalTx> list = internalTxnsMapper.getInternalTxList(address);
        return new PageInfo<>(list);
    }

    // tree list for internal txs
    public List<ResponseInternalTx> getInternalTxListByTxHash(String txHash) {
        List<ResponseInternalTx> result = new ArrayList<>();
        List<ResponseInternalTx> list = internalTxnsMapper.getInternalTxListByTxHash(txHash);
        for (ResponseInternalTx item : list) {
            if (item.getParentId() == 0) {
                result.add(item);
                continue;
            }
            ResponseInternalTx parent = list.stream().filter(p -> p.getId().equals(item.getParentId())).findFirst().orElse(null);
            if (parent != null) {
                if (parent.getCalls() == null) {
                    parent.setCalls(new ArrayList<>());
                }
                parent.getCalls().add(item);
            }
        }
        return result;
    }

}
