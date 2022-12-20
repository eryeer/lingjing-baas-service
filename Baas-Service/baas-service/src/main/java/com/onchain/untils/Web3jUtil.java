package com.onchain.untils;

import com.onchain.constants.ReturnCode;
import com.onchain.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@Slf4j
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

    public static String getAddressFromETHPrivateKey(String privateKey) {
        ECKeyPair publicKey = ECKeyPair.create(Numeric.toBigInt(privateKey));
        return Keys.toChecksumAddress(Keys.getAddress(publicKey));
    }

    public static boolean isSignatureValid(String signature, String message, String address) {
        if (address == null || address.length() != 42 || signature == null || signature.length() != 132 || message == null || message.equals("")) {
            return false;
        }
        address = address.toLowerCase(Locale.ROOT);
        byte[] sigBytes = Numeric.hexStringToByteArray(signature);
        Sign.SignatureData signatureData = sigFromByteArray(sigBytes);
        String addressRecoverd = null;
        byte[] digest = message.getBytes(StandardCharsets.UTF_8);
        byte[] data = getEthereumMessageHash(digest);
        for (int i = 0; i < 4; i++) {
            BigInteger pubkey = Sign.recoverFromSignature((byte) i,
                    new ECDSASignature(new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())),
                    data);
            if (pubkey != null) {
                addressRecoverd = "0x" + Keys.getAddress(pubkey);
                if (addressRecoverd.equals(address)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Sign.SignatureData sigFromByteArray(byte[] sig) {
        if (sig.length < 64 || sig.length > 65) return null;

        byte subv = sig[64];
        if (subv < 27) subv += 27;
        byte[] rBytes = Arrays.copyOfRange(sig, 0, 32);
        byte[] sBytes = Arrays.copyOfRange(sig, 32, 64);

        return new Sign.SignatureData(subv, rBytes, sBytes);
    }

    public static byte[] getEthereumMessageHash(byte[] message) {
        String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";
        byte[] prefix = MESSAGE_PREFIX.concat(String.valueOf(message.length)).getBytes();

        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);

        return Hash.sha3(result);
    }

    public static BigInteger getBalanceByAddress(Web3j web3j, String address) {
        try {
            return web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        } catch (Exception e) {
            log.error("getBalanceByAddress error: ", e);
            throw new CommonException(ReturnCode.GET_BALANCE_ERROR);
        }
    }

    public static String transfer(Web3j web3j, String signedRawTransaction) throws InterruptedException, ExecutionException, IOException {
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedRawTransaction).send();
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }

    public static String getSignedRawTransaction(Web3j web3j, String privateKey, String toAddress, String amount) throws InterruptedException, ExecutionException, IOException {
        Credentials credentials = Credentials.create(privateKey);
        String fromAddress = Web3jUtil.getAddressFromETHPrivateKey(privateKey);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        String netVersion = web3j.netVersion().send().getNetVersion();
        RawTransactionManager rawTransactionManager = Web3jUtil.getTransactionManager(web3j, credentials, netVersion);
        RawTransaction etherTransaction = RawTransaction.createEtherTransaction(
                nonce,
                Convert.toWei("1", Convert.Unit.GWEI).toBigInteger(),
                BigInteger.valueOf(21000L),
                toAddress,
                new BigInteger(amount)
        );
        String signedMessage = rawTransactionManager.sign(etherTransaction);
        return signedMessage;
    }

}
