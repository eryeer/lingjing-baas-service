package com.onchain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.ChainAccount;
import com.onchain.entities.dao.GasApply;
import com.onchain.entities.response.ResponseChainAccount;
import com.onchain.entities.response.ResponseFile;
import com.onchain.exception.CommonException;
import com.onchain.mapper.ChainAccountMapper;
import com.onchain.mapper.GasApplyMapper;
import com.onchain.untils.Web3jUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChainService {

    private final ChainAccountMapper chainAccountMapper;
    private final ParamsConfig paramsConfig;
    private final ObjectMapper objectMapper;
    private final Web3j web3j;
    private final CosService cosService;
    private final GasApplyMapper gasApplyMapper;

    @Transactional
    public ResponseChainAccount accountCreate(String userId, String walletPass) {
        ResponseChainAccount account = chainAccountMapper.getChainAccountByUserId(userId);
        if (account != null) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_EXIST);
        }

        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
//        Credentials cs = Credentials.create(ecKeyPair);
            String privateKey = ecKeyPair.getPrivateKey().toString(16);
            WalletFile walletFile = Wallet.createStandard(walletPass, ecKeyPair);
            String address = walletFile.getAddress();
            String fileName = address + ".dat";
            // create folder if not exists
            new File(paramsConfig.localWalletDir).mkdirs();
            File destination = new File(paramsConfig.localWalletDir, fileName);
            objectMapper.writeValue(destination, walletFile);
            ResponseFile cosFile = cosService.uploadLocalFile(destination.getAbsolutePath(), CommonConst.FILE_WALLET, userId);
            log.info("Wallet file local path: " + destination.getAbsolutePath());
            ChainAccount chainAccount = ChainAccount.builder()
                    .applyTime(null)
                    .balance("0")
                    .privateKey(privateKey)
                    .userAddress("0x" + address)
                    .userId(userId)
                    .walletFileUuid(cosFile.getUuid())
                    .walletPass(walletPass)
                    .build();
            chainAccountMapper.insertChainAccount(chainAccount);
        } catch (Exception ex) {
            log.error("Wallet file create failed!", ex);
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_CREATE_ERROR);
        }

        return getChainAccount(userId);
    }

    public ResponseChainAccount getChainAccount(String userId) {
        ResponseChainAccount account = chainAccountMapper.getChainAccountByUserId(userId);
        if (account == null) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
        }
        account.setWalletFile(cosService.getCosFile(account.getWalletFileUuid()));
        return account;
    }

    public String applyGas(String userId) throws Exception {
        ChainAccount account = chainAccountMapper.getChainAccount(userId);
        if (account == null) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
        }
        Date now = new Date();
        if (account.getApplyTime() != null && DateUtils.addDays(account.getApplyTime(), 1).after(now)) {
            throw new CommonException(ReturnCode.GAS_APPLY_LIMIT);
        }
        Credentials credentials = Credentials.create(paramsConfig.adminAccount);
        String netVersion = web3j.netVersion().send().getNetVersion();
        RawTransactionManager rawTransactionManager = Web3jUtil.getTransactionManager(web3j, credentials, netVersion);
        Transfer transfer = new Transfer(web3j, rawTransactionManager);
        TransactionReceipt transactionReceipt = transfer.sendFunds(account.getUserAddress(), BigDecimal.valueOf(0.1), Convert.Unit.ETHER).send();
        account.setApplyTime(now);
        account.setBalance(web3j.ethGetBalance(account.getUserAddress(), DefaultBlockParameter.valueOf(CommonConst.LatestBlockNumberKey)).send().getBalance().toString());
        chainAccountMapper.updateChainAccount(account);
        gasApplyMapper.insertGasApply(GasApply.builder().userAddress(account.getUserAddress()).applyAmount("100000000")
                .userId(account.getUserId()).applyTime(account.getApplyTime()).build());
        return transactionReceipt.getTransactionHash();
    }

    public BigInteger getNonce(Web3j web3j, String address) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
        return ethGetTransactionCount.getTransactionCount();
    }

    public void updateBalance(String userId) throws IOException {
        ChainAccount account = chainAccountMapper.getChainAccount(userId);
        String balance = web3j.ethGetBalance(account.getUserAddress(), DefaultBlockParameter.valueOf(CommonConst.LatestBlockNumberKey)).send().getBalance().toString();
        if (!StringUtils.equals(balance, account.getBalance())) {
            account.setBalance(balance);
            chainAccountMapper.updateChainAccount(account);
        }
    }

    public List<GasApply> getApplyList(String userId) {
        return gasApplyMapper.getApplyList(userId);
    }
}
