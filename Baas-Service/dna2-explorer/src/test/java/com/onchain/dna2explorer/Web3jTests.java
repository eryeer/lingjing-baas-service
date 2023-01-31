package com.onchain.dna2explorer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.utils.EthUtil;
import org.junit.Test;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Web3jTests {

    //    Web3j web3j = Web3j.build(new HttpService("https://bsc-dataseed.binance.org/"));
    Web3j eth = Web3j.build(new HttpService("https://mainnet.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161"));
    //    Web3j web3j = Web3j.build(new HttpService("http://123.60.28.245:8545"));
    Web3j web3j = Web3j.build(new HttpService("https://maas-test-node.onchain.com"));
    //    Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
    String pk1 = "0x59c6995e998f97a5a0044966f0945389dc9e86dae88c7a8412f4603b6b78690d";
    String address1 = "0x70997970c51812dc3a010c7d01b50e0d17dc79c8";
    String address2 = "0x3c44cdddb6a900fa2b585dd299e03d12fa4293bc";

    //    @Test
    public void transfer() throws Exception {
        Credentials credentials = Credentials.create(pk1); //WalletUtils.loadCredentials("password", "/path/to/walletfile");
        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                        web3j, credentials, address2,
                        BigDecimal.valueOf(1.0), Convert.Unit.ETHER)
                .send();
        System.out.println(transactionReceipt.toString());
    }

    //    @Test
    public void getPK() {
        String privateKey = pk1;
        Credentials cs = Credentials.create(privateKey);

        String privateKey1 = cs.getEcKeyPair().getPrivateKey().toString(16);
        String publicKey = cs.getEcKeyPair().getPublicKey().toString(16);
        String addr = cs.getAddress();

        System.out.println("Private key: " + privateKey1);
        System.out.println("Public key: " + publicKey);
        System.out.println("Address: " + addr);
    }

    @Test
    public void getTxCount() throws IOException, ExecutionException, InterruptedException {
        // web3j methods
        System.out.println("netVersion: " + web3j.netVersion().send().getNetVersion());
        System.out.println("netPeerCount: " + web3j.netPeerCount().send().getQuantity().intValue());
        System.out.println("netListening: " + web3j.netListening().send().getResult());
        System.out.println("ethBlockNumber: " + web3j.ethBlockNumber().send().getBlockNumber().longValue());
        System.out.println("ethGetBalance: " + web3j.ethGetBalance(address1, DefaultBlockParameter.valueOf(Constant.LatestBlockNumberKey)).send().getBalance().toString());
        System.out.println("ethGetTransactionCount: " + web3j.ethGetTransactionCount(address1, DefaultBlockParameter.valueOf(Constant.LatestBlockNumberKey)).send().getTransactionCount().longValue());
        System.out.println("ethGetBlockByNumber: " + JSON.toJSONString(web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(1543)), true)
                .send().getBlock()));
        System.out.println(Integer.toHexString(1543));
//        System.out.println("ethGetTransactionReceipt: " + web3j.ethGetTransactionReceipt("0xe4841616584a31c2aaadf837f77897d7112909e3e882299567ee8c063045ed1b").send().getTransactionReceipt().get());
//
//        System.out.println("Event encoder:" + EthUtil.buildMethodHash("Transfer(address,address,uint256)"));
//
//        String res = EthUtil.callContractString(web3j, "0x04d49DE637D91A0aB3F810FC8DEEc514BeF6F75b", "name", Collections.emptyList());
//        System.out.println("callContract: " + res);
//        res = EthUtil.callContractUint(web3j, "0x04d49DE637D91A0aB3F810FC8DEEc514BeF6F75b", "decimals", Collections.emptyList());
//        System.out.println("callContract: " + res);
    }

    //@Test
    public void getAbiStr() throws IOException {
        String abiStr = new String(Files.readAllBytes(Paths.get("D://Work/eth/KC721.json")), StandardCharsets.UTF_8);
        List<AbiDefinition> abi = JSONArray.parseArray(abiStr, AbiDefinition.class);
        System.out.println(JSON.toJSONString(abi));
    }

    //    @Test
    public void getPancakeInfo() throws Exception {
        System.out.println("farm poolLength: " + EthUtil.callContractUint(web3j, "0x73feaa1ee314f8c655e354234017be2193c9e24e", "poolLength", Collections.emptyList()));
        String address = EthUtil.callContractMulti(web3j, "0x73feaa1ee314f8c655e354234017be2193c9e24e", "poolInfo", Arrays.asList(new Uint256(1)), Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
        }, new TypeReference<Uint256>() {
        }, new TypeReference<Uint256>() {
        }, new TypeReference<Uint256>() {
        })).get(0).toString();
        System.out.println("farm poolInfo: " + address);
        System.out.println("farm poolInfo token0: " + EthUtil.callContractAddress(web3j, address, "token0", Collections.emptyList()));
        System.out.println("farm poolInfo token1: " + EthUtil.callContractAddress(web3j, address, "token1", Collections.emptyList()));
    }

    //    @Test
    public void getLooksInfo() throws Exception {
        String totalStr = EthUtil.callContractUint(eth, "0xf4d2888d29d722226fafa5d9b24f9164c092421e", "totalSupply", Collections.emptyList());
        long total = new BigInteger(totalStr).divide(new BigInteger("1000000000000000000")).longValue();
        System.out.println("Looks totalSupply: " + total);
    }

}
