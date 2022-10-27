package com.onchain.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.ChainAccount;
import com.onchain.entities.dao.ContractApp;
import com.onchain.entities.dao.ContractDeploy;
import com.onchain.entities.request.RequestContractDeploy;
import com.onchain.entities.request.RequestContractParameter;
import com.onchain.entities.request.RequestContractUpdate;
import com.onchain.entities.response.ResponseContractApp;
import com.onchain.entities.response.ResponseContractDepoly;
import com.onchain.entities.response.ResponseContractTemplate;
import com.onchain.exception.CommonException;
import com.onchain.mapper.ChainAccountMapper;
import com.onchain.mapper.ContractAppMapper;
import com.onchain.mapper.CosFileMapper;
import com.onchain.mapper.LoginLogMapper;
import com.onchain.untils.Web3jUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractService {

    private final ContractAppMapper contractAppMapper;
    private final ChainAccountMapper chainAccountMapper;
    private final ParamsConfig paramsConfig;
    private final ObjectMapper objectMapper;
    private final Web3j web3j;
    private final ChainService chainService;
    private final JwtService jwtService;
    private final LoginLogMapper loginLogMapper;
    private final CosFileMapper cosFileMapper;
    private final CosService cosService;

    public void appCreate(String userId, String appName, String templateType) {
        if (!StringUtils.equalsAny(templateType, CommonConst.PROOF, CommonConst.VOTE, CommonConst.CUSTOM)) {
            throw new CommonException(ReturnCode.PARAMETER_FAILED);
        }

        ContractApp contractApp = contractAppMapper.getContractApp(userId, appName);
        if (contractApp != null) {
            throw new CommonException(ReturnCode.APP_EXIST);
        }

        contractApp = ContractApp.builder()
                .appName(appName)
                .userId(userId)
                .contractStatus("0")
                .deployTime(null)
                .contractFileUuids("[]")
                .deployHistory("[]")
                .templateType(templateType)
                .build();
        contractAppMapper.insertContractApp(contractApp);
    }

    public void appRemove(String userId, String appName) {
        ContractApp contractApp = contractAppMapper.getContractApp(userId, appName);
        if (contractApp == null) {
            throw new CommonException(ReturnCode.APP_NOT_EXIST);
        }

        contractAppMapper.removeContractApp(userId, appName);
    }

    public PageInfo<ResponseContractApp> getAppList(Integer pageNumber, Integer pageSize, String userId) {
        PageHelper.startPage(pageNumber, pageSize);
        List<ResponseContractApp> appList = contractAppMapper.getContractAppList(userId);
        for (ResponseContractApp item : appList) {
            List<String> contractFileIds = JSON.parseArray(item.getContractFileUuids(), String.class);
            if (contractFileIds.size() > 0) {
                item.setContractFileList(cosService.getCosFiles(contractFileIds));
            } else {
                item.setContractFileList(new ArrayList<>());
            }
        }
        return new PageInfo<>(appList);
    }

    public ResponseContractApp getApp(String userId, String appName) {
        ResponseContractApp app = contractAppMapper.getResponseContractApp(userId, appName);
        List<String> contractFileIds = JSON.parseArray(app.getContractFileUuids(), String.class);
        if (contractFileIds.size() > 0) {
            app.setContractFileList(cosService.getCosFiles(contractFileIds));
        } else {
            app.setContractFileList(new ArrayList<>());
        }
        return app;
    }

//    public ResponseContractDepoly deploy(String userId, RequestContractDeploy request) throws Exception {
//        ContractApp app = contractAppMapper.getContractApp(userId, request.getAppName());
//        if (app == null) {
//            throw new CommonException(ReturnCode.APP_NOT_EXIST);
//        }
//
//        ChainAccount chainAccount = chainAccountMapper.getChainAccount(userId);
//        List<Type> params;
//        if (StringUtils.equals(CommonConst.PROOF, app.getTemplateType())) {
//            if (request.getConstructorParameterList().size() != 1 || !"string".equals(request.getConstructorParameterList().get(0).getType())) {
//                throw new CommonException(ReturnCode.PARAMETER_FAILED, "存证合约参数错误");
//            }
//            request.setBytecode(CommonConst.PROOF_BIN);
//            request.setContractName(CommonConst.PROOF);
//            request.getConstructorParameterList().add(RequestContractParameter.builder().type("address").content(chainAccount.getUserAddress()).build());
//            params = request.getConstructorParameterList().stream()
//                    .map(p -> Web3jUtil.parseStringToBCType(p.getType(), p.getContent())).collect(Collectors.toList());
//        } else if (StringUtils.equals(CommonConst.VOTE, app.getTemplateType())) {
//            if (request.getConstructorParameterList().size() <= 1) {
//                throw new CommonException(ReturnCode.PARAMETER_FAILED);
//            }
//            request.setBytecode(CommonConst.VOTE_BIN);
//            request.setContractName(CommonConst.VOTE);
//            List<String> candidateNames = request.getConstructorParameterList().stream().map(RequestContractParameter::getContent).collect(Collectors.toList());
//            params = Collections.singletonList(new DynamicArray<>(
//                    Utf8String.class,
//                    Utils.typeMap(candidateNames, Utf8String.class)));
//        } else {
//            if (StringUtils.isBlank(request.getBytecode())) {
//                throw new CommonException(ReturnCode.PARAMETER_FAILED, "字节码不能为空");
//            }
//            params = request.getConstructorParameterList().stream()
//                    .map(p -> Web3jUtil.parseStringToBCType(p.getType(), p.getContent())).collect(Collectors.toList());
//        }
//
//        String encodedConstructor = FunctionEncoder.encodeConstructor(params);
//        log.info("encoded constructor param: " + encodedConstructor);
//        ResponseContractDepoly result = deployWithEncodedConstructorParam(request.getBytecode(), encodedConstructor, chainAccount.getPrivateKey());
//
//        Date now = new Date();
//        ContractDeploy contractDeploy = ContractDeploy.builder()
//                .userId(userId)
//                .appName(request.getAppName())
//                .templateType(app.getTemplateType())
//                .deployTime(now)
//                .contractFileUuid(request.getContractFileUuid())
//
//                .contractName(request.getContractName())
//                .bytecode(request.getBytecode())
//                .contractAddress(result.getContractAddress())
//                .constructorJson(JSON.toJSONString(request.getConstructorParameterList()))
//                .build();
//
//        List<ContractDeploy> history = JSON.parseArray(app.getDeployHistory(), ContractDeploy.class);
//        history.add(contractDeploy);
//        app.setDeployHistory(JSON.toJSONString(history));
//        app.setDeployTime(now);
//        app.setContractStatus("1");
//        contractAppMapper.updateContractApp(app);
////        chainService.updateBalance(app.getUserId());
//        return result;
//    }

    public ResponseContractDepoly deployWithEncodedConstructorParam(String bytecode, String encodedConstructor, String privateKey) throws IOException, TransactionException {
        StaticGasProvider gasProvider = new StaticGasProvider(BigInteger.valueOf(4_100_000_000L), BigInteger.valueOf(1_000_000));
        Credentials credentials = Credentials.create(privateKey);
        String netVersion = web3j.netVersion().send().getNetVersion();
        RawTransactionManager rawTransactionManager = Web3jUtil.getTransactionManager(web3j, credentials, netVersion);
        BigInteger nonce = chainService.getNonce(web3j, credentials.getAddress());
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasProvider.getGasPrice(), gasProvider.getGasLimit(), null, BigInteger.ZERO, bytecode + encodedConstructor);
        log.info("bytecode: " + bytecode);
        log.info("encodedConstructor: " + encodedConstructor);
        EthSendTransaction ethSendTransaction = rawTransactionManager.signAndSend(rawTransaction);
        log.info("tx hash: " + ethSendTransaction.getTransactionHash());
        TransactionReceipt receipt = Web3jUtil.executeResponse(web3j, ethSendTransaction.getTransactionHash());
        log.info("contract address: " + receipt.getContractAddress());
        return new ResponseContractDepoly(receipt.getContractAddress(), receipt.getTransactionHash(), new BigInteger(receipt.getGasUsedRaw().substring(2), 16).toString(), new BigInteger(receipt.getBlockNumberRaw().substring(2), 16).toString());
    }

    public List<ResponseContractTemplate> getContractTemplates() {
        List<ResponseContractTemplate> result = new ArrayList<>();
        result.add(ResponseContractTemplate.builder()
                .templateType(CommonConst.PROOF)
                .contractFileUrl(cosService.getTempUrl(CommonConst.PROOF_TEMPLATE_KEY, paramsConfig.cosBucketName, CommonConst.FILE_URL_VALID_TIME))
                .abiContent(CommonConst.PROOF_ABI)
                .build());
        result.add(ResponseContractTemplate.builder()
                .templateType(CommonConst.VOTE)
                .contractFileUrl(cosService.getTempUrl(CommonConst.VOTE_TEMPLATE_KEY, paramsConfig.cosBucketName, CommonConst.FILE_URL_VALID_TIME))
                .abiContent(CommonConst.VOTE_ABI)
                .build());
        return result;
    }

    @Transactional
    public void updateFileList(String userId, RequestContractUpdate request) {
        ContractApp app = contractAppMapper.getContractApp(userId, request.getAppName());
        if (app == null) {
            throw new CommonException(ReturnCode.APP_NOT_EXIST);
        }
        app.setContractFileUuids(JSON.toJSONString(request.getContractFileUuidList()));
        contractAppMapper.updateContractApp(app);
        cosFileMapper.markFileUsed(request.getContractFileUuidList());
    }
}
