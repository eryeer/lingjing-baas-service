package com.onchain.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.GasContractStatus;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.GasApply;
import com.onchain.entities.dao.GasContract;
import com.onchain.entities.dao.GasSummary;
import com.onchain.entities.request.RequestAccGasRequire;
import com.onchain.entities.request.RequestApproveGasContract;
import com.onchain.entities.request.RequestGasCreate;
import com.onchain.entities.response.*;
import com.onchain.exception.CommonException;
import com.onchain.mapper.ChainAccountMapper;
import com.onchain.mapper.CosFileMapper;
import com.onchain.mapper.GasApplyMapper;
import com.onchain.mapper.GasContractMapper;
import com.onchain.untils.Web3jUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GasService {

    private final GasContractMapper gasContractMapper;
    private final RedisService redisService;
    private final CosFileMapper cosFileMapper;
    private final CosService cosService;
    private final ChainAccountMapper chainAccountMapper;
    private final GasApplyMapper gasApplyMapper;
    private final ParamsConfig paramsConfig;
    private final Web3j web3j;

    @Transactional(rollbackFor = Exception.class)
    public ResponseGasContract createGasContract(String userId, RequestGasCreate requestGasCreate) {
        Long flowId = redisService.incrByKey("flow_id");
        String standardFlowId = "LSH-HT-" + String.format("%06d", flowId);
        GasContract gasContract = GasContract.builder()
                .flowId(standardFlowId)
                .contractFileUUID(requestGasCreate.getContractFileUUID())
                .agreementAmount(requestGasCreate.getAgreementAmount())
                .uploadTime(System.currentTimeMillis())
                .approvedTime(0L)
                .feedback("")
                .userId(userId).build();
        gasContractMapper.createGasContract(gasContract);
        cosFileMapper.markFileUsed(Arrays.asList(requestGasCreate.getContractFileUUID()));
        ResponseGasContract responseGasContract = gasContractMapper.getGasContractByFlowId(standardFlowId);
        responseGasContract.setContractFile(cosService.getCosFile(responseGasContract.getContractFileUUID()));
        return responseGasContract;
    }

    public PageInfo<ResponseGasContract> getGasContractList(Integer pageNumber, Integer pageSize, String userId, String flowId, Long uploadStartTime, Long uploadEndTime, Integer status, Long approvedStartTime, Long approvedEndTime) {
        PageHelper.startPage(pageNumber, pageSize);

        List<ResponseGasContract> gasContracts = gasContractMapper.getGasContractList(userId, status, flowId, uploadStartTime, uploadEndTime, approvedStartTime, approvedEndTime);
        for (ResponseGasContract gasContract : gasContracts) {
            gasContract.setContractFile(cosService.getCosFile(gasContract.getContractFileUUID()));
        }
        return new PageInfo<>(gasContracts);
    }

    public ResponseUserGasSummary getGasContractSummary(String userId) {
        try {
            ResponseUserGasSummary responseUserGasSummary = ResponseUserGasSummary.builder().userId(userId).build();
            BigInteger totalAgreementAmount = new BigInteger("0");
            List<ResponseGasContract> gasContracts = gasContractMapper.getSuccessGasContractListByUserId(userId);
            for (ResponseGasContract gasContract : gasContracts) {
                totalAgreementAmount = totalAgreementAmount.add(new BigInteger(gasContract.getAgreementAmount()));
            }
            responseUserGasSummary.setApplyAmount("0");
            responseUserGasSummary.setUnApplyAmount(String.valueOf(totalAgreementAmount));
            responseUserGasSummary.setTotalAmount(String.valueOf(totalAgreementAmount));
            List<ResponseChainAccount> chainAccounts = chainAccountMapper.getChainAccountByUserId(userId);
            List<ResponseChainAccountGasSummary> responseChainAccountGasSummaries = gasApplyMapper.getChainAccountApplyList(userId);
            for (ResponseChainAccountGasSummary responseChainAccountGasSummary : responseChainAccountGasSummaries) {
                BigInteger remain = Web3jUtil.getBalanceByAddress(web3j, responseChainAccountGasSummary.getAccountAddress());
                responseChainAccountGasSummary.setRemain(remain.toString());
            }
            for (ResponseChainAccount chainAccount : chainAccounts) {
                ResponseChainAccountGasSummary responseChainAccountGasSummary = ResponseChainAccountGasSummary.builder()
                        .accountAddress(chainAccount.getUserAddress())
                        .applyAmount("0").remain("0")
                        .accountName(chainAccount.getName())
                        .Id(chainAccount.getId())
                        .build();
                responseChainAccountGasSummaries.add(responseChainAccountGasSummary);
            }
            responseUserGasSummary.setChainAccountGasDistribute(responseChainAccountGasSummaries);
            return responseUserGasSummary;
        }catch (Exception e){
            throw new CommonException(ReturnCode.GET_BALANCE_ERROR);
        }
    }

    public PageInfo<ResponseAdminGasContract> getAdminGasContractList(Integer pageNumber, Integer pageSize, String phoneNumber, String companyName, String agreementAmount, String flowId, Long uploadStartTime, Long uploadEndTime, Integer status, Boolean isApproving, Long approvedStartTime, Long approvedEndTime) {
        PageHelper.startPage(pageNumber, pageSize);

        List<ResponseAdminGasContract> gasContracts = gasContractMapper.getAdminGasContractList(phoneNumber, companyName, agreementAmount, status, isApproving, flowId, uploadStartTime, uploadEndTime, approvedStartTime, approvedEndTime);
        for (ResponseAdminGasContract gasContract : gasContracts) {
            gasContract.setContractFile(cosService.getCosFile(gasContract.getContractFileUUID()));
        }
        return new PageInfo<>(gasContracts);
    }

    public void approveGasContract(RequestApproveGasContract request) {
        ResponseGasContract res = gasContractMapper.getGasContractByFlowId(request.getFlowId());
        if (res == null) {
            throw new CommonException(ReturnCode.GAS_CONTRACT_NOT_EXIST);
        }
        if (!(GasContractStatus.Approving.ordinal() == res.getStatus())) {
            throw new CommonException(ReturnCode.GAS_CONTRACT_STATUS_ERROR);
        }

        GasContract gasContract = GasContract.builder()
                .flowId(request.getFlowId())
                .agreementAmount(request.getAgreementAmount())
                .approvedTime(System.currentTimeMillis())
                .feedback(request.getFeedback())
                .build();
        if (request.getIsPass()) {
            gasContract.setStatus(String.valueOf(GasContractStatus.Success.ordinal()));
        } else {
            gasContract.setStatus(String.valueOf(GasContractStatus.Fail.ordinal()));
        }
        gasContractMapper.approveGasContract(gasContract);
    }

    public PageInfo<ResponseGasContractStatistic> getGasContactStatisticList(Integer pageNumber, Integer pageSize, String phoneNumber, String companyName, Long approvedStartTime, Long approvedEndTime) {
        PageHelper.startPage(pageNumber, pageSize);
        List<ResponseGasContractStatistic> gasContracts = gasContractMapper.getGasContactStatisticList(phoneNumber, companyName, approvedStartTime, approvedEndTime);
        return new PageInfo<>(gasContracts);
    }

    @Transactional(rollbackFor = Exception.class)
    public void accquireGas(String userId, RequestAccGasRequire requestAccGasRequire) {
        try {
//            if (!Web3jUtil.isSignatureValid(requestAccGasRequire.getSignedMessage(), requestAccGasRequire.getMessage(), requestAccGasRequire.getApplyAccountAddress())){
//                throw new CommonException(ReturnCode.CHAIN_ACCOUNT_SIGNATURE_ERROR);
//            }
            BigInteger remainAmount= new BigInteger(gasApplyMapper.getRemainAmountByUserId(userId));
            if (remainAmount.compareTo(new BigInteger(requestAccGasRequire.getApplyAmount())) < 0) {
                throw new CommonException(ReturnCode.REMAIN_NOT_ENOUGH_ERROR);
            }
            String transactionHash = Web3jUtil.transfer(web3j, paramsConfig.maasAdminAccount,  requestAccGasRequire.getApplyAccountAddress(), requestAccGasRequire.getApplyAmount());
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();

            TransactionReceipt transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt().get();
            if (transactionReceipt.getStatus().equals("0x1")){
                GasApply gasApply = GasApply.builder().applyTime(System.currentTimeMillis())
                        .applyAmount(requestAccGasRequire.getApplyAmount())
                        .txHash(transactionHash)
                        .userAddress(requestAccGasRequire.getApplyAccountAddress())
                        .userId(userId).build();
                gasApplyMapper.insertGasApply(gasApply);
                List<ResponseGasContract> successGasContract = gasContractMapper.getSuccessGasContractListByUserId(userId);
                Long agreementTime = successGasContract.get(0).getApprovedTime();
                BigInteger agreementAmount = new BigInteger("0");
                for (ResponseGasContract responseGasContract : successGasContract) {
                    agreementAmount = agreementAmount.add(new BigInteger(responseGasContract.getAgreementAmount()));
                }
                GasSummary gasSummary = GasSummary.builder().agreementTime(agreementTime)
                        .applyTime(gasApply.getApplyTime())
                        .applyAmount(gasApply.getApplyAmount())
                        .agreementAmount(agreementAmount.toString()).build();
                gasApplyMapper.updateGasSummaryInfo(gasSummary);
                return;
            }else{
                throw new CommonException(ReturnCode.TRANSFER_ERROR);
            }
        }catch (ExecutionException var1){
            throw new CommonException(ReturnCode.TRANSFER_ERROR);
        }
        catch(InterruptedException var2){
            throw new CommonException(ReturnCode.TRANSFER_ERROR);
        }
    }



    public PageInfo<ReponseChainAccountGasApplySummary> getChainAccountListForGasManagement(Integer pageNumber, Integer pageSize,String userId, String userAddress, String name, Long applyStartTime, Long applyEndTime){
        PageHelper.startPage(pageNumber, pageSize);
        List<ReponseChainAccountGasApplySummary> chainAccountGasInfoList = gasApplyMapper.getChainAccountGasInfoList(userId, userAddress, applyStartTime, applyEndTime, name);
        try {
            for (ReponseChainAccountGasApplySummary chainAccountGasApplyInfo : chainAccountGasInfoList) {
                BigInteger balance = Web3jUtil.getBalanceByAddress(web3j, chainAccountGasApplyInfo.getAccountAddress());
                chainAccountGasApplyInfo.setRemainGas(balance.toString());
                if(StringUtils.isEmpty(chainAccountGasApplyInfo.getAppliedGas())){
                    chainAccountGasApplyInfo.setAppliedGas("0");
                }
                if(StringUtils.isEmpty(chainAccountGasApplyInfo.getRemainGas())){
                    chainAccountGasApplyInfo.setRemainGas("0");
                }
            }
        }catch (Exception e){
            throw  new CommonException(ReturnCode.GET_BALANCE_ERROR);
        }
        return new PageInfo<>(chainAccountGasInfoList);
    }
}
