package com.onchain.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.constants.GasContractStatus;
import com.onchain.entities.dao.GasContract;
import com.onchain.entities.request.RequestGasCreate;
import com.onchain.entities.response.ResponseChainAccount;
import com.onchain.entities.response.ResponseChainAccountGasSummary;
import com.onchain.entities.response.ResponseGasContract;
import com.onchain.entities.response.ResponseUserGasSummary;
import com.onchain.mapper.CosFileMapper;
import com.onchain.mapper.GasContractMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GasService {

    private final GasContractMapper gasContractMapper;
    private final RedisService redisService;
    private final CosFileMapper cosFileMapper;
    private final CosService cosService;

    @Transactional(rollbackFor = Exception.class)
    public ResponseGasContract createGasContract(String userId, RequestGasCreate requestGasCreate){
        Long flowId = redisService.incrByKey("flow_id");
        String standardFlowId = "LSH-HT-" + String.format("%06d", flowId);
        GasContract gasContract = GasContract.builder()
                .flowId(standardFlowId)
                .contractFileUUID(requestGasCreate.getContractFileUUID())
                .agreementAmount(requestGasCreate.getAgreementAmount())
                .uploadTime(System.currentTimeMillis())
                .approvedTime(0l)
                .feedback("")
                .userId(userId).build();
        gasContractMapper.createGasContract(gasContract);
        cosFileMapper.markFileUsed(Arrays.asList(requestGasCreate.getContractFileUUID()));
        return gasContractMapper.getGasContractByFlowId(standardFlowId);
    }

    public PageInfo<ResponseGasContract> getGasContractList(Integer pageNumber, Integer pageSize, String userId, String flowId, Long uploadStartTime, Long uploadEndTime, Integer status, Long approvedStartTime, Long approvedEndTime) {
        PageHelper.startPage(pageNumber, pageSize);

        List<ResponseGasContract> gasContracts = gasContractMapper.getGasContractList(userId, status, flowId, uploadStartTime, uploadEndTime, approvedStartTime, approvedEndTime);
        for (ResponseGasContract gasContract : gasContracts) {
            gasContract.setContractFile(cosService.getCosFile(gasContract.getContractFileUUID()));
            if (gasContract.getStatus() == GasContractStatus.Approving.ordinal()) {
                gasContract.setApprovedTime(0l);
            }
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
        responseUserGasSummary.setChainAccountGasDistribute(responseChainAccountGasSummaries);
        return responseUserGasSummary;
    }

}
