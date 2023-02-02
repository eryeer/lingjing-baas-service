package com.onchain.dna2explorer.service;

import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.mapper.BlockMapper;
import com.onchain.dna2explorer.mapper.NodeMapper;
import com.onchain.dna2explorer.mapper.SummaryMapper;
import com.onchain.dna2explorer.mapper.TransactionMapper;
import com.onchain.dna2explorer.model.dao.Block;
import com.onchain.dna2explorer.model.dao.Node;
import com.onchain.dna2explorer.model.dao.Summary;
import com.onchain.dna2explorer.model.response.ResponseSummary;
import com.onchain.dna2explorer.model.response.ResponseTotalSummary;
import com.onchain.dna2explorer.model.response.ResponseTxSummary;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class SummaryService {

    private final BlockMapper blockMapper;
    private final TransactionMapper transactionMapper;
    private final NodeMapper nodeMapper;
    private final SummaryMapper summaryMapper;
    private static final HashMap<String, Integer> missingTimes = new HashMap<>();

    public ResponseTotalSummary getTotalSummary() {
        ResponseTotalSummary result = blockMapper.getTotalSummary();
        List<Node> nodeList = nodeMapper.selectAllNodes();
        result.setNodeCount(nodeList.size());
        result.setActiveCount(((int) nodeList.stream().filter(p -> p.getIsActive()).count()));
        result.setNetStatus(result.getActiveCount() > 0 && result.getActiveCount() + result.getActiveCount() >= result.getNodeCount());
        return result;
    }

    public List<ResponseSummary> getAddressSummary(Integer limit) {
        return summaryMapper.getAddressSummary(limit);
    }

    public List<ResponseSummary> getBlockSummary(Integer limit) {
        return summaryMapper.getBlockSummary(limit);
    }

    public ResponseTxSummary getTransactionSummary(Integer limit) {
        return ResponseTxSummary.builder()
                .dailySummary(summaryMapper.getTransactionDailySummary(limit))
                .monthlySummary(summaryMapper.getTransactionMonthlySummary(limit))
                .build();
    }

    /**
     * 根据节点列表检查节点状态
     */
    public void syncNodeInfo() {
        List<Node> nodes = nodeMapper.selectAllNodes();
        List<Node> nodesToUpdate = new ArrayList<>();

        for (Node node : nodes) {
            String ip = node.getIp();
            Integer port = node.getRestPort();
            String nodeAddress = String.format("http://%s:%d", ip, port);
            String version = "";
            Web3j web3j = null;
            try {
                web3j = Web3j.build(new HttpService(nodeAddress));
                version = web3j.netVersion().send().getNetVersion();
            } catch (Exception ex) {
                log.info("syncNodeInfo exception: {} ", ex.getMessage());
            } finally {
                if (web3j != null) {
                    web3j.shutdown();
                }
            }

            boolean isActive = !StringUtils.isEmpty(version);
            // set active
            if (isActive && !node.getIsActive()) {
                node.setVersion(version);
                node.setIsActive(true);
                nodesToUpdate.add(node);
                missingTimes.put(nodeAddress, 0);
            }
            // set inactive
            else if (!isActive && node.getIsActive()) {
                Integer times = missingTimes.getOrDefault(nodeAddress, 0);
                missingTimes.put(nodeAddress, ++times);
                if (times > Constant.NodeMissingConfirm) {
                    node.setIsActive(false);
                    nodesToUpdate.add(node);
                }
            }
        }
        for (Node node : nodesToUpdate) {
            nodeMapper.updateNodeActive(node);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDailySummary() {
        Long startTime = summaryMapper.getDailyStartTime();
        if (startTime == null) {
            Block block = blockMapper.getBlock(1);
            // empty block table
            if (block == null) {
                return;
            }
            startTime = block.getBlockTime();
            startTime = DateUtils.truncate(new Date(startTime), Calendar.DATE).getTime();
        }
        Long currentTime = blockMapper.latestBlockTime();
        Long endTime = startTime + Constant.OneDayMilliseconds;
        while (startTime < currentTime) {
            Summary summary = blockMapper.getSummary(startTime, endTime);
            summary.setSummaryTime(startTime);
            summary.setActiveAddressCount((int) transactionMapper.getAddressListByBlockTime(startTime, endTime).stream().distinct().count());
            if (summaryMapper.selectDailySummary(startTime) != null) {
                summaryMapper.updateDaily(summary);
            } else {
                summaryMapper.insertDaily(summary);
            }
            startTime = endTime;
            endTime = endTime + Constant.OneDayMilliseconds;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMonthlySummary() {
        Long startTime = summaryMapper.getMonthlyStartTime();
        if (startTime == null) {
            Block block = blockMapper.getBlock(1);
            // empty block table
            if (block == null) {
                return;
            }
            startTime = block.getBlockTime();
            startTime = DateUtils.truncate(new Date(startTime), Calendar.MONTH).getTime();
        }
        Long currentTime = blockMapper.latestBlockTime();
        Long endTime = DateUtils.addMonths(new Date(startTime), 1).getTime();
        while (startTime < currentTime) {
            Summary summary = blockMapper.getSummary(startTime, endTime);
            summary.setSummaryTime(startTime);
            summary.setActiveAddressCount((int) transactionMapper.getAddressListByBlockTime(startTime, endTime).stream().distinct().count());
            if (summaryMapper.selectMonthlySummary(startTime) != null) {
                summaryMapper.updateMonthly(summary);
            } else {
                summaryMapper.insertMonthly(summary);
            }
            startTime = endTime;
            endTime = DateUtils.addMonths(new Date(startTime), 1).getTime();
        }
    }
}
