package com.onchain.dna2explorer.service;

import com.alibaba.fastjson.JSONArray;
import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.constants.ReturnCode;
import com.onchain.dna2explorer.exception.CommonException;
import com.onchain.dna2explorer.mapper.ContractMapper;
import com.onchain.dna2explorer.mapper.MethodMapMapper;
import com.onchain.dna2explorer.mapper.TransactionMapper;
import com.onchain.dna2explorer.model.dao.Contract;
import com.onchain.dna2explorer.model.dao.MethodMap;
import com.onchain.dna2explorer.model.dao.Transaction;
import com.onchain.dna2explorer.model.response.ResponseAddress;
import com.onchain.dna2explorer.utils.EthUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class ContractService {

    private final TransactionMapper transactionMapper;
    private final ContractMapper contractMapper;
    private final MethodMapMapper methodMapMapper;
    private final Web3j web3j;

    @Transactional(rollbackFor = Exception.class)
    public void uploadAbi(ResponseAddress address, String abi, String signature) {
        List<AbiDefinition> abiDefinitions = JSONArray.parseArray(abi, AbiDefinition.class);
        String contractType = "";
        String tokenName = "";
        String tokenSymbol = "";
        int decimals = 0;
        if (isERC(abiDefinitions, Constant.ERC20_ABI)) {
            contractType = Constant.ERC20;
        } else if (isERC(abiDefinitions, Constant.ERC721_ABI)) {
            contractType = Constant.ERC721;
        }
        try {
            if (abiDefinitions.stream().anyMatch(p -> StringUtils.equals(Constant.NAME, p.getName()))) {
                tokenName = EthUtil.callContractString(web3j, address.getAddress(), Constant.NAME, Collections.emptyList());
            }
            if (abiDefinitions.stream().anyMatch(p -> StringUtils.equals(Constant.SYMBOL, p.getName()))) {
                tokenSymbol = EthUtil.callContractString(web3j, address.getAddress(), Constant.SYMBOL, Collections.emptyList());
            }
            if (abiDefinitions.stream().anyMatch(p -> StringUtils.equals(Constant.DECIMALS, p.getName()))) {
                decimals = Integer.parseInt(EthUtil.callContractUint(web3j, address.getAddress(), Constant.DECIMALS, Collections.emptyList()));
            }
        } catch (Exception ex) {
            log.error("EthUtil.callContract error:", ex);
        }

        Transaction tx = transactionMapper.getContractCreateTx(address.getAddress());
        // todo: delete after re-sync the block
        if (tx == null) {
            tx = Transaction.builder().fromAddress("").txHash("").build();
        }

        boolean res = EthUtil.isContractDeployer(signature, abi, tx.getFromAddress());
        if (!res) {
            throw new CommonException(ReturnCode.CONTRACT_ABI_UPLOADER_INVALID);
        }

        Contract contract = Contract.builder()
                .contractType(contractType)
                .abi(abi)
                .blockTime(address.getBlockTime())
                .address(address.getAddress())
                .creator(tx.getFromAddress())
                .createTxHash(tx.getTxHash())
                .tokenName(tokenName)
                .tokenSymbol(tokenSymbol)
                .decimals(decimals)
                .build();

        List<MethodMap> methodMapList = getNewMethodMaps(abiDefinitions);
        if (contractMapper.getContract(address.getAddress()) == null) {
            contractMapper.batchInsert(Collections.singletonList(contract));
        } else {
            contractMapper.batchUpdate(Collections.singletonList(contract));
        }
        methodMapMapper.batchInsert(methodMapList);
    }

    // check if the abi is match the erc methods
    private boolean isERC(List<AbiDefinition> abi, String ercAbiStr) {
        List<AbiDefinition> ercAbi = JSONArray.parseArray(ercAbiStr, AbiDefinition.class);
        for (AbiDefinition item : ercAbi) {
            AbiDefinition mapped = abi.stream().filter(p -> StringUtils.equals(p.getName(), item.getName()) && p.getInputs().size() == item.getInputs().size()).findFirst().orElse(null);
            // name and type
            if (mapped == null || !StringUtils.equals(mapped.getType(), item.getType())) {
                return false;
            }
            // outputs size
            if (item.getOutputs() == null || item.getOutputs().isEmpty()) {
                if (mapped.getOutputs() != null && mapped.getOutputs().size() > 0) {
                    return false;
                }
            } else if (mapped.getOutputs() == null || item.getOutputs().size() != mapped.getOutputs().size()) {
                return false;
            }

            for (int i = 0; i < item.getInputs().size(); i++) {
                if (!StringUtils.equals(item.getInputs().get(i).getType(), mapped.getInputs().get(i).getType())) {
                    return false;
                }
                if (item.getInputs().get(i).isIndexed() != mapped.getInputs().get(i).isIndexed()) {
                    return false;
                }
            }
            if (item.getOutputs() != null && item.getOutputs().size() > 0) {
                for (int i = 0; i < item.getOutputs().size(); i++) {
                    if (!StringUtils.equals(item.getOutputs().get(i).getType(), mapped.getOutputs().get(i).getType())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private List<MethodMap> getNewMethodMaps(List<AbiDefinition> abi) {
        List<MethodMap> result = new ArrayList<>();
        for (AbiDefinition item : abi) {
            if (!StringUtils.equalsAny(item.getType(), Constant.TYPE_EVENT, Constant.TYPE_FUNCTION)) {
                continue;
            }
            String methodSignature = EthUtil.buildMethodSignature(item.getName(), item.getInputs());
            String methodHash = EthUtil.buildMethodHash(methodSignature);
            MethodMap newItem = MethodMap.builder()
                    .methodHash(methodHash)
                    .methodId(EthUtil.getMethodId(methodHash))
                    .methodName(item.getName())
                    .methodSignature(methodSignature)
                    .build();
            result.add(newItem);
        }
        return result;
    }



}
