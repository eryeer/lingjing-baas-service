package com.onchain.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.constants.GasContractStatus;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.GasContract;
import com.onchain.entities.request.RequestApproveGasContract;
import com.onchain.entities.request.RequestGasCreate;
import com.onchain.entities.response.*;
import com.onchain.exception.CommonException;
import com.onchain.mapper.ChainAccountMapper;
import com.onchain.mapper.CosFileMapper;
import com.onchain.mapper.GasContractMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GasService {

    private final GasContractMapper gasContractMapper;
    private final RedisService redisService;
    private final CosFileMapper cosFileMapper;
    private final CosService cosService;
    private final ChainAccountMapper chainAccountMapper;

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
        ResponseUserGasSummary responseUserGasSummary = ResponseUserGasSummary.builder().userId(userId).build();
        BigInteger totalAgreementAmount = new BigInteger("0");
        List<ResponseGasContract> gasContracts = gasContractMapper.getSuccessGasContractListByUserId(userId);
        for (ResponseGasContract gasContract : gasContracts) {
            totalAgreementAmount = totalAgreementAmount.add(new BigInteger(gasContract.getAgreementAmount()));
        }
        responseUserGasSummary.setApplyAmount("0");
        responseUserGasSummary.setUnApplyAmount(String.valueOf(totalAgreementAmount));
        responseUserGasSummary.setTotalAmount(String.valueOf(totalAgreementAmount));
        ArrayList<ResponseChainAccountGasSummary> responseChainAccountGasSummaries = new ArrayList<>();
        List<ResponseChainAccount> chainAccounts = chainAccountMapper.getChainAccountByUserId(userId);
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
    }

    public PageInfo<ResponseAdminGasContract> getAdminGasContractList(Integer pageNumber, Integer pageSize, String phoneNumber, String userName, String agreementAmount, String flowId, Long uploadStartTime, Long uploadEndTime, Integer status, Long approvedStartTime, Long approvedEndTime) {
        PageHelper.startPage(pageNumber, pageSize);

        List<ResponseAdminGasContract> gasContracts = gasContractMapper.getAdminGasContractList(phoneNumber, userName, agreementAmount, status, flowId, uploadStartTime, uploadEndTime, approvedStartTime, approvedEndTime);
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
        }
        gasContractMapper.approveGasContract(gasContract);
    }

    public PageInfo<ResponseGasContractStatistic> getGasContactStatisticList(Integer pageNumber, Integer pageSize, String phoneNumber, String userName, Long approvedStartTime, Long approvedEndTime) {
        PageHelper.startPage(pageNumber, pageSize);
        List<ResponseGasContractStatistic> gasContracts = gasContractMapper.getGasContactStatisticList(phoneNumber, userName, approvedStartTime, approvedEndTime);
        return new PageInfo<>(gasContracts);
    }
}
