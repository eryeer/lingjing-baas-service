package com.onchain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.contracts.Maas;
import com.onchain.entities.dao.ChainAccount;
import com.onchain.entities.dao.GasApply;
import com.onchain.entities.request.RequestAccountCreate;
import com.onchain.entities.response.ResponseChainAccount;
import com.onchain.exception.CommonException;
import com.onchain.mapper.ChainAccountMapper;
import com.onchain.mapper.GasApplyMapper;
import com.onchain.untils.Web3jUtil;
import com.onchain.util.ECCUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        if (!Web3jUtil.isSignatureValid(request.getSignedMessage(), request.getMessage(), request.getChainAddress())) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_SIGNATURE_ERROR);
        }
        ResponseChainAccount account = chainAccountMapper.getChainAccountByAddress(userAddress);
        if (account != null) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_EXIST);
        }
        if (!userAddress.startsWith("0x")) {
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
        if (!account.getUserAddress().equals(Web3jUtil.getAddressFromETHPrivateKey(privateKey))) {
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
        if (StringUtils.isEmpty(account.getEncodeKey())) {
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

//    public List<ResponseChainAccount> getChainAccount(String userId) {
//        List<ResponseChainAccount> account = chainAccountMapper.getChainAccountByUserId(userId);
//        if (account == null) {
//            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
//        }
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

    public PageInfo<ResponseChainAccount> getChainAccount(Integer pageNumber, Integer pageSize, String userId, String name, String userAddress, Boolean isGasTransfer, Boolean isCustody, Long startTime, Long endTime) {
        PageHelper.startPage(pageNumber, pageSize);
        Date start = null, end = null;
        if (startTime != null && endTime != null) {
            start = new Date(startTime);
            end = new Date(endTime);
        }
        List<ResponseChainAccount> users = chainAccountMapper.getChainAccount(userId, name, userAddress, isGasTransfer, isCustody, start, end);
        return new PageInfo<>(users);
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeGasTransferStatus(String userId, List<Long> ids, Boolean isGasTransfer) throws Exception {
        List<ChainAccount> accounts = chainAccountMapper.getUserAccountListById(userId, ids);
        if (accounts.isEmpty()) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
        }
        List<String> addresses = accounts.stream().filter(p -> p.getIsGasTransfer() != isGasTransfer).map(ChainAccount::getUserAddress).collect(Collectors.toList());
        if (addresses.isEmpty()) {
            return;
        }
        setGasUsers(addresses, isGasTransfer);
        chainAccountMapper.updateAccountStatusById(userId, ids, CommonConst.ACTIVE, isGasTransfer);
    }

    @Transactional(rollbackFor = Exception.class)
    private void setGasUsers(List<String> addresses, Boolean isGasTransfer) throws Exception {
        ContractGasProvider gasProvider = new StaticGasProvider(BigInteger.valueOf(100_000_000L), BigInteger.valueOf(30_000_000L));
        Credentials credentials = Credentials.create(paramsConfig.maasAdminAccount);
        Maas maasConfig = Maas.load(paramsConfig.maasConfigAddress, web3j, credentials, gasProvider);
        maasConfig.setGasUsers(addresses, isGasTransfer).send();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteChainAccount(String userId, List<Long> ids) throws Exception {
        List<ChainAccount> accounts = chainAccountMapper.getUserAccountListById(userId, ids);
        if (accounts.isEmpty()) {
            throw new CommonException(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
        }
        List<String> addresses = accounts.stream().filter(ChainAccount::getIsGasTransfer).map(ChainAccount::getUserAddress).collect(Collectors.toList());
        if (!addresses.isEmpty()) {
            setGasUsers(addresses, false);
        }
        chainAccountMapper.updateAccountStatusById(userId, ids, CommonConst.DELETED, false);
    }
}
