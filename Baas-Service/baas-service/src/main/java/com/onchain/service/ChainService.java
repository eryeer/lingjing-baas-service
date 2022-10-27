package com.onchain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.ChainAccount;
import com.onchain.entities.dao.GasApply;
import com.onchain.entities.request.RequestAccountCreate;
import com.onchain.entities.response.ResponseChainAccount;
import com.onchain.entities.response.ResponseFile;
import com.onchain.exception.CommonException;
import com.onchain.mapper.ChainAccountMapper;
import com.onchain.mapper.GasApplyMapper;
import com.onchain.untils.Web3jUtil;
import com.onchain.util.ECCUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;
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
    public ResponseChainAccount accountCreate(String userId, RequestAccountCreate request) {
        String userAddress = request.getChainAddress();
        String chainUserName = request.getChainUserName();
        if (!Web3jUtil.isSignatureValid(request.getSignedMessage(), request.getMessage(), request.getChainAddress())){
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_SIGNATURE_ERROR);
        }
        ResponseChainAccount account = chainAccountMapper.getChainAccountByAddress(userAddress);
        if (account != null) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_EXIST);
        }
        if (!userAddress.startsWith("0x")){
            userAddress = "0x" + userAddress;
        }

        try {
            ChainAccount chainAccount = ChainAccount.builder()
                    .isGasTransfer(false)
                    .userAddress(userAddress)
                    .userId(userId)
                    .name(chainUserName)
                    .build();
            chainAccountMapper.insertChainAccount(chainAccount);
        } catch (Exception ex) {
            log.error("chain account create failed!", ex);
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_CREATE_ERROR);
        }

        return chainAccountMapper.getChainAccountByAddress(userAddress);
    }

    @Transactional
    public void custodPrivateKey(Long chainAccountId, String privateKey) {
        ChainAccount account = chainAccountMapper.getChainAccountById(chainAccountId);
        if (account == null) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
        }
        if (!account.getUserAddress().equals(Web3jUtil.getAddressFromETHPrivateKey(privateKey))){
            throw new CommonException(ReturnCode.PRIVATE_KEY_CHAIN_ACCOUNT_UN_MATCH);
        }

        try {
            String encodeKey = ECCUtils.encryptBySecret(privateKey, paramsConfig.privateEncodeKey, paramsConfig.privateEncodeOffset);
            chainAccountMapper.updateEncodeKey(chainAccountId, encodeKey);
        } catch (Exception ex) {
            log.error("custod private key failed!", ex);
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_CREATE_ERROR);
        }
    }

    public ResponseChainAccount getCustodPrivateKey(Long chainAccountId) {
        ChainAccount account = chainAccountMapper.getChainAccountById(chainAccountId);
        if (account == null) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
        }
        if (StringUtils.isEmpty(account.getEncodeKey())){
            throw new CommonException(ReturnCode.PRIVATE_KEY_UN_CUSTOD);
        }
        ResponseChainAccount responseChainAccount = ResponseChainAccount.builder().Id(account.getId()).
                createTime(account.getCreateTime())
                .isGasTransfer(account.getIsGasTransfer())
                .name(account.getName())
                .userAddress(account.getUserAddress())
                .userId(account.getUserId()).build();

        try {
            String privateKey = ECCUtils.decryptBySecret(account.getEncodeKey(), paramsConfig.privateEncodeKey, paramsConfig.privateEncodeOffset);
            responseChainAccount.setPrivateKey(privateKey);
            return responseChainAccount;
        } catch (Exception ex) {
            log.error("download custoded private key failed!", ex);
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_CREATE_ERROR);
        }
    }

//    public ResponseChainAccount getChainAccount(String userId) {
//        ResponseChainAccount account = chainAccountMapper.getChainAccountByUserId(userId);
//        if (account == null) {
//            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
//        }
//        account.setWalletFile(cosService.getCosFile(account.getWalletFileUuid()));
//        return account;
//    }

//    public String applyGas(String userId) throws Exception {
//        ChainAccount account = chainAccountMapper.getChainAccount(userId);
//        if (account == null) {
//            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
//        }
//        Date now = new Date();
//        if (account.getApplyTime() != null && DateUtils.addDays(account.getApplyTime(), 1).after(now)) {
//            throw new CommonException(ReturnCode.GAS_APPLY_LIMIT);
//        }
//        Credentials credentials = Credentials.create(paramsConfig.adminAccount);
//        String netVersion = web3j.netVersion().send().getNetVersion();
//        RawTransactionManager rawTransactionManager = Web3jUtil.getTransactionManager(web3j, credentials, netVersion);
//        Transfer transfer = new Transfer(web3j, rawTransactionManager);
//        TransactionReceipt transactionReceipt = transfer.sendFunds(account.getUserAddress(), BigDecimal.valueOf(0.1), Convert.Unit.ETHER).send();
//        account.setApplyTime(now);
//        account.setBalance(web3j.ethGetBalance(account.getUserAddress(), DefaultBlockParameter.valueOf(CommonConst.LatestBlockNumberKey)).send().getBalance().toString());
//        chainAccountMapper.updateChainAccount(account);
//        gasApplyMapper.insertGasApply(GasApply.builder().userAddress(account.getUserAddress()).applyAmount("100000000")
//                .userId(account.getUserId()).applyTime(account.getApplyTime()).build());
//        return transactionReceipt.getTransactionHash();
//    }

    public BigInteger getNonce(Web3j web3j, String address) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
        return ethGetTransactionCount.getTransactionCount();
    }

//    public void updateBalance(String userId) throws IOException {
//        ChainAccount account = chainAccountMapper.getChainAccount(userId);
//        String balance = web3j.ethGetBalance(account.getUserAddress(), DefaultBlockParameter.valueOf(CommonConst.LatestBlockNumberKey)).send().getBalance().toString();
//        if (!StringUtils.equals(balance, account.getBalance())) {
//            account.setBalance(balance);
//            chainAccountMapper.updateChainAccount(account);
//        }
//    }

    public List<GasApply> getApplyList(String userId) {
        return gasApplyMapper.getApplyList(userId);
    }
}
