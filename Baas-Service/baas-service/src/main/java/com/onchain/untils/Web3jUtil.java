package com.onchain.untils;

import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;

public class Web3jUtil {

    public static Credentials getCredentials(String privateKey) {
        return Credentials.create(privateKey);
    }

    public static RawTransactionManager getTransactionManager(Web3j web3j, Credentials credentials, String chainId) {
        return new RawTransactionManager(web3j, credentials, Long.parseLong(chainId));
    }

    public static Web3j getWeb3j(String url) {
        return Web3j.build(new HttpService(url));
    }

    public static void checkReceipt(TransactionReceipt receipt) throws TransactionException {
        if (!receipt.isStatusOK()) {
            throw new TransactionException(
                    String.format(
                            "Transaction has failed with status: %s. "
                                    + "Gas used: %s. (not-enough gas?)",
                            receipt.getStatus(),
                            receipt.getGasUsedRaw() != null
                                    ? receipt.getGasUsed().toString()
                                    : "unknown"));
        }
    }

    public static TransactionReceipt executeResponse(Web3j web3j, String transactionHash) throws TransactionException, IOException {
        PollingTransactionReceiptProcessor transactionReceiptProcessor = new PollingTransactionReceiptProcessor(web3j, 15000, 40);
        TransactionReceipt receipt = transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
        checkReceipt(receipt);
        return receipt;
    }

    public static Type parseStringToBCType(String type, String content) throws RuntimeException {
        switch (type) {
            case "uint256":
                return new org.web3j.abi.datatypes.generated.Uint256(new BigInteger(content));
            case "address":
                return new org.web3j.abi.datatypes.Address(160, content);
            case "string":
                return new org.web3j.abi.datatypes.Utf8String(content);
            case "bool":
                return new org.web3j.abi.datatypes.Bool(Boolean.valueOf(content));
            default:
                throw new RuntimeException("parse Block Chain Type error");
        }
    }

}
