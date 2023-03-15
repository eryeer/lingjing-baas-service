package com.onchain.dna2explorer.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.onchain.dna2explorer.config.Web3jConfig;
import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.mapper.InternalTxnsMapper;
import com.onchain.dna2explorer.mapper.TableHeightMapper;
import com.onchain.dna2explorer.mapper.TransactionMapper;
import com.onchain.dna2explorer.model.dao.InternalTxn;
import com.onchain.dna2explorer.model.dao.Transaction;
import com.onchain.dna2explorer.model.response.ResponseDebugTraceTransaction;
import com.onchain.dna2explorer.model.response.ResponseRpc;
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
import org.web3j.utils.Numeric;
import sun.reflect.generics.factory.GenericsFactory;

import java.io.IOException;
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


    @Transactional(rollbackFor = Exception.class)
    public void syncInternalTransaction(Long startNumber, Long endNumber) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<Transaction> txs = transactionMapper.getTransactionListByBlockNumber(startNumber, endNumber);
        HttpPost httpPost = new HttpPost(web3jConfig.getSyncNodeUrl());
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        for (Transaction tx : txs) {
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
            if (!StringUtils.isEmpty(object.getError())){
                log.error("transaction hash: "+ txHash + ", error is " + object.getError());
                throw new RuntimeException(object.getError());
            }
            ResponseDebugTraceTransaction result = object.getResult();
            if (null != result && null != result.getCalls()) {
                for (ResponseDebugTraceTransaction subCall : result.getCalls()) {
                    parseInsert(tx, subCall, 0l);
                }
            }

        }
        tableHeightMapper.updateTableHeightByTableName(TBL_TRANSACTION_BLOCK_HEIGHT_FOR_INTERNAL_TXNS, endNumber);
    }



    private void parseInsert(Transaction tx, ResponseDebugTraceTransaction result, Long parentId) {
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
                .revertReason(result.getRevertReason())
                .build();
        if (StringUtils.isEmpty(result.getError())) {
            internalTxn.setError("");
        }
        if (StringUtils.isEmpty(result.getRevertReason())) {
            internalTxn.setRevertReason("");
        }
        String value = result.getValue();
        if (StringUtils.isEmpty(value)){
            log.warn(String.format("txhash: %s ,value is null", tx.getTxHash()));
            internalTxn.setValue("0x0");
        }else if (value.equals("0x")){
            log.warn(String.format("txhash: %s ,value is 0x", tx.getTxHash()));
            internalTxn.setValue("0x0");
        }else {
            internalTxn.setValue(Numeric.decodeQuantity(result.getValue()).divide(Constant.GWeiFactor).toString());
        }
        String gas = result.getGas();
        if (StringUtils.isEmpty(gas)){
            log.warn(String.format("txhash: %s ,gas is null", tx.getTxHash()));
            internalTxn.setGas(0l);
        }else if (gas.equals("0x")){
            log.warn(String.format("txhash: %s ,gas is 0x", tx.getTxHash()));
            internalTxn.setGas(0l);
        }else {
            internalTxn.setGas(Numeric.decodeQuantity(result.getGas()).longValue());
        }
        String gasUsed = result.getGasUsed();
        if (StringUtils.isEmpty(gasUsed)){
            log.warn(String.format("txhash: %s ,gasUsed is null", tx.getTxHash()));
            internalTxn.setGasUsed(0l);
        }else if (gasUsed.equals("0x")){
            log.warn(String.format("txhash: %s ,gasUsed is 0x", tx.getTxHash()));
            internalTxn.setGasUsed(0l);
        }else {
            internalTxn.setGasUsed(Numeric.decodeQuantity(result.getGasUsed()).longValue());
        }
        if (StringUtils.isEmpty(result.getOutput())) {
            log.warn(String.format("txhash: %s ,output is null", tx.getTxHash()));
            internalTxn.setOutput("0x");
        }
        if (StringUtils.isEmpty(result.getInput())) {
            log.warn(String.format("txhash: %s ,input is null", tx.getTxHash()));
            internalTxn.setInput("0x");
        }
        internalTxnsMapper.insert(internalTxn);
        Long id = internalTxn.getId();
        if (null != result.getCalls()) {
            for (ResponseDebugTraceTransaction subCall : result.getCalls()) {
                parseInsert(tx, subCall, id);
            }
        }
    }

}
