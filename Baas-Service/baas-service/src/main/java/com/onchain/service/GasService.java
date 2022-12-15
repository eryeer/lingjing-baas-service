package com.onchain.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.GasContractStatus;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.GasApply;
import com.onchain.entities.dao.GasContract;
import com.onchain.entities.dao.GasSummary;
import com.onchain.entities.request.RequestAcRequireGas;
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
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

import java.io.IOException;
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
            List<ResponseChainAccountGasSummary> responseChainAccountGasSummaries = gasApplyMapper.getChainAccountApplySummary(userId);
            for (ResponseChainAccountGasSummary responseChainAccountGasSummary : responseChainAccountGasSummaries) {
                BigInteger remain = Web3jUtil.getBalanceByAddress(web3j, responseChainAccountGasSummary.getAccountAddress());
                responseChainAccountGasSummary.setRemain(remain.toString());
                if (StringUtils.isEmpty(responseChainAccountGasSummary.getApplyAmount())){
                    responseChainAccountGasSummary.setApplyAmount("0");
                }
            }
            GasSummary gasSummary = gasApplyMapper.getGasSummaryInfoByUserId(userId);

            responseUserGasSummary.setChainAccountGasDistribute(responseChainAccountGasSummaries);

            responseUserGasSummary.setApplyAmount(gasSummary.getApplyAmount());
            BigInteger unApplyAmount = new BigInteger(gasSummary.getAgreementAmount()).subtract(new BigInteger(gasSummary.getApplyAmount()));
            responseUserGasSummary.setUnApplyAmount(unApplyAmount.toString());
            responseUserGasSummary.setTotalAmount(gasSummary.getAgreementAmount());
            return responseUserGasSummary;
        }catch (Exception e){
            log.error("getGasContractSummary error: ", e.getMessage());
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

    @Transactional(rollbackFor = Exception.class)
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
        //审批完成 更新 tbl_gas_summary
        ResponseGasContract gasContractByFlowId = gasContractMapper.getGasContractByFlowId(gasContract.getFlowId());
        String userId = gasContractByFlowId.getUserId();
        String approvedAmount = gasApplyMapper.getApprovedAmountByUserID(userId);
        BigInteger latestApprovedAmount = new BigInteger("0");
        if (!StringUtils.isEmpty(approvedAmount)){
            latestApprovedAmount = new BigInteger(approvedAmount);
        }
        latestApprovedAmount = latestApprovedAmount.add(new BigInteger(request.getAgreementAmount()));
        GasSummary oldSummaryInfo = gasApplyMapper.getGasSummaryInfoByUserId(userId);
        GasSummary gasSummary = GasSummary.builder()
                .agreementAmount(latestApprovedAmount.toString())
                .agreementTime(gasContract.getApprovedTime())
                .userId(userId).build();
        String applyAmount = "0";
        Long applyTime = 0l;
        if (oldSummaryInfo != null){
           applyAmount = StringUtils.isEmpty(oldSummaryInfo.getApplyAmount())?applyAmount:oldSummaryInfo.getApplyAmount();
           applyTime = oldSummaryInfo.getApplyTime();
        }
        gasSummary.setApplyAmount(applyAmount);
        gasSummary.setApplyTime(applyTime);
        gasApplyMapper.updateGasSummaryInfo(gasSummary);
    }

    public PageInfo<ResponseGasContractStatistic> getGasContactStatisticList(Integer pageNumber, Integer pageSize, String phoneNumber, String companyName, Long approvedStartTime, Long approvedEndTime) {
        PageHelper.startPage(pageNumber, pageSize);
        List<ResponseGasContractStatistic> gasContracts = gasContractMapper.getGasContactStatisticList(phoneNumber, companyName, approvedStartTime, approvedEndTime);
        return new PageInfo<>(gasContracts);
    }

    @Transactional(rollbackFor = Exception.class)
    public void acquireGas(String userId, RequestAcRequireGas requestAccGasRequire) throws InterruptedException, ExecutionException, IOException, TransactionException {
        String remainAmountByStr = gasApplyMapper.getRemainAmountByUserId(userId);
        BigInteger acquireAmount = new BigInteger(requestAccGasRequire.getApplyAmount());
        BigInteger minTransferAmount = Convert.toWei("10000", Convert.Unit.GWEI).toBigInteger();
        if (acquireAmount.compareTo(minTransferAmount) < 0) {
            throw new CommonException(ReturnCode.UN_MATCH_MIN_TRANSFR_AMOUNT_ERROR);
        }
        BigInteger remainAmount= StringUtils.isEmpty(remainAmountByStr)? new BigInteger(CommonConst.ZERO_STR):new BigInteger(remainAmountByStr);

        if (remainAmount.compareTo(remainAmount) < 0) {
            throw new CommonException(ReturnCode.REMAIN_NOT_ENOUGH_ERROR);
        }
        String signedRawTransaction = Web3jUtil.getSignedRawTransaction(web3j, paramsConfig.maasAdminAccount,  requestAccGasRequire.getApplyAccountAddress(), requestAccGasRequire.getApplyAmount());
        String txHash = Hash.sha3(signedRawTransaction);
        // 先进行数据库事务
        GasApply gasApply = GasApply.builder().applyTime(System.currentTimeMillis())
                .applyAmount(requestAccGasRequire.getApplyAmount())
                .txHash(txHash)
                .userAddress(requestAccGasRequire.getApplyAccountAddress())
                .userId(userId).build();
        gasApplyMapper.insertGasApply(gasApply);
        List<ResponseGasContract> successGasContract = gasContractMapper.getSuccessGasContractListByUserId(userId);
        Long agreementTime = successGasContract.get(0).getApprovedTime();
        BigInteger agreementAmount = new BigInteger("0");
        for (ResponseGasContract responseGasContract : successGasContract) {
            agreementAmount = agreementAmount.add(new BigInteger(responseGasContract.getAgreementAmount()));
        }
        List<ResponseChainAccountGasSummary> chainAccountApplyList = gasApplyMapper.getChainAccountApplyList(userId);
        BigInteger applyAmount = new BigInteger("0");
        for (ResponseChainAccountGasSummary accountGasApply : chainAccountApplyList) {
            String applyAmountStr = StringUtils.isEmpty(accountGasApply.getApplyAmount())?"0":accountGasApply.getApplyAmount();
            applyAmount = applyAmount.add(new BigInteger(applyAmountStr));
        }
        GasSummary gasSummary = GasSummary.builder().agreementTime(agreementTime)
                .applyTime(gasApply.getApplyTime())
                .applyAmount(applyAmount.toString())
                .agreementAmount(agreementAmount.toString())
                .userId(userId).build();
        gasApplyMapper.updateGasSummaryInfo(gasSummary);
        // 进行转账操作
        String transactionHash = Web3jUtil.transfer(web3j, signedRawTransaction);
        if (!txHash.equals(transactionHash)){
            throw new CommonException(ReturnCode.TX_HASH_MISMATCH_ERROR);
        }
        PollingTransactionReceiptProcessor pollingTransactionReceiptProcessor = new PollingTransactionReceiptProcessor(web3j, 5000L, 10);
        TransactionReceipt transactionReceipt = pollingTransactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
        if (!transactionReceipt.getStatus().equals("0x1")){
            throw new CommonException(ReturnCode.TRANSFER_ERROR);
        }
    }



    public PageInfo<ReponseChainAccountGasApplySummary> getChainAccountListForGasManagement(Integer pageNumber, Integer pageSize,String userId, String userAddress, String name, Long applyStartTime, Long applyEndTime) throws IOException {
        PageHelper.startPage(pageNumber, pageSize);
        List<ReponseChainAccountGasApplySummary> chainAccountGasInfoList = gasApplyMapper.getChainAccountGasInfoList(userId, userAddress, applyStartTime, applyEndTime, name);
        for (ReponseChainAccountGasApplySummary chainAccountGasApplyInfo : chainAccountGasInfoList) {
            BigInteger balance = Web3jUtil.getBalanceByAddress(web3j, chainAccountGasApplyInfo.getAccountAddress());
            chainAccountGasApplyInfo.setRemainGas(balance.toString());
            if(StringUtils.isEmpty(chainAccountGasApplyInfo.getAppliedGas())){
                chainAccountGasApplyInfo.setAppliedGas(CommonConst.ZERO_STR);
            }
            if(StringUtils.isEmpty(chainAccountGasApplyInfo.getRemainGas())){
                chainAccountGasApplyInfo.setRemainGas(CommonConst.ZERO_STR);
            }
        }
        return new PageInfo<>(chainAccountGasInfoList);
    }
}
