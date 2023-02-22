package com.onchain.dna2explorer.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import com.onchain.dna2explorer.exception.TransferInformationException;
import com.onchain.dna2explorer.mapper.TableHeightMapper;
import com.onchain.dna2explorer.mapper.TokenMapper;
import com.onchain.dna2explorer.mapper.TransferMapper;
import com.onchain.dna2explorer.model.dao.NFTHolder;
import com.onchain.dna2explorer.model.dao.Transfer;
import com.onchain.dna2explorer.model.response.ResponseAccountNFTHolder;
import com.onchain.dna2explorer.model.response.ResponseNFTHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.onchain.dna2explorer.constant.Constant.ZERO_ADDRESS;
import static com.onchain.dna2explorer.constants.CommonConst.TBL_TRANSFER_ID_FOR_TOKEN_HOLDER;


@Slf4j
@Service
@AllArgsConstructor
public class TokenService {

    private final TokenMapper tokenMapper;
    private final TransferMapper transferMapper;
    private final TableHeightMapper tableHeightMapper;

    public ResponseNFTHolder getNFTHolderListByContractAddress(Integer pageNumber, Integer pageSize, String contractAddress) {
        PageHelper.startPage(pageNumber, pageSize);
        contractAddress = contractAddress.toLowerCase();
        ResponseNFTHolder responseNFTHolder = ResponseNFTHolder.builder()
                .holderCount(0L)
                .contractAddress(contractAddress)
                .responseAccountNFTHolderPageInfo(new PageInfo<>(new ArrayList<>(0)))
                .build();
        List<ResponseAccountNFTHolder> list = tokenMapper.getNFTHolderByContractAddress(contractAddress);
        if (list == null || list.isEmpty()) {
            return responseNFTHolder;
        }
        Long sum = tokenMapper.getNFTTokenSumByContractAddress(contractAddress);
        if (null == sum || sum == 0) {
            return responseNFTHolder;
        }
        for (int i = 0; i < list.size(); i++) {
            double percentage = list.get(i).getCount().intValue() / sum.floatValue();
            list.get(i).setPercentage(percentage);
            list.get(i).setRank((long) (i + 1));
        }
        PageInfo<ResponseAccountNFTHolder> responseAccountNFTHolderPageInfo = new PageInfo<>(list);
        responseNFTHolder.setHolderCount(responseAccountNFTHolderPageInfo.getTotal());
        responseNFTHolder.setResponseAccountNFTHolderPageInfo(responseAccountNFTHolderPageInfo);
        return responseNFTHolder;
    }

    @Transactional(rollbackFor = Exception.class)
    public void countingNFTHolder(Long batchExecuteSize) throws TransferInformationException {
        // 获取tbl_transfer 上次更新的统计高度
        Long lastestTransferTableHeight = tableHeightMapper.getLastestTableHeightByTableName(TBL_TRANSFER_ID_FOR_TOKEN_HOLDER);
        if (lastestTransferTableHeight == null) {
            lastestTransferTableHeight = 0L;
        }
        // 获取大于上次高度的新增区间的transfer信息
        List<Transfer> transferList = transferMapper.getTransferListByGTId(lastestTransferTableHeight, batchExecuteSize);
        if (transferList == null || transferList.size() == 0)
            return;
        //获取统计区间的终止id值
        Transfer transfer = transferList.get(transferList.size() - 1);
        Long endId = transfer.getId();
        // 按合同地址分组遍历
        List<String> contractAddressList = transferList.stream().map(Transfer::getContractAddress).distinct().collect(Collectors.toList());
        for (String contractAddress : contractAddressList) {
            // 某个账户流出
            Map<String, Long> accountOut = transferList.stream().filter(x -> x.getContractAddress().equals(contractAddress)).collect(Collectors.groupingBy(Transfer::getTransferFrom, Collectors.counting()));
            // 某个账户流入
            Map<String, Long> accountIn = transferList.stream().filter(x -> x.getContractAddress().equals(contractAddress)).collect(Collectors.groupingBy(Transfer::getTransferTo, Collectors.counting()));
            // 某个账户 的流入减去流出
            Set<String> accountInKeys = accountIn.keySet();
            Set<String> accountOutInKeys = accountOut.keySet();
            //获取统计到的所有账户
            Sets.SetView<String> transferKeys = Sets.union(accountInKeys, accountOutInKeys);
            Map<String, Long> statisticAccountHolder = new HashMap<>();
            // 遍历账户 获取 token的差值
            for (String transferKey : transferKeys) {
                Long accountInValue = 0L;
                if (accountIn.containsKey(transferKey)) {
                    accountInValue = accountIn.get(transferKey);
                }
                Long accountOutValue = 0L;
                if (accountOut.containsKey(transferKey)) {
                    accountOutValue = accountOut.get(transferKey);
                }
                statisticAccountHolder.put(transferKey, accountInValue - accountOutValue);
            }
            // 按用户遍历更新 holders
            for (Map.Entry<String, Long> entry : statisticAccountHolder.entrySet()) {
                String accountAddress = entry.getKey();
                Long count = entry.getValue();
                if (ZERO_ADDRESS.equals(accountAddress)) {
                    continue;
                }
                NFTHolder nftHolder = NFTHolder.builder().count(count).accountAddress(accountAddress).contractAddress(contractAddress).build();
                NFTHolder nftHolderFromDB = tokenMapper.getNFTHolderByContractAddressAndAccountAddress(contractAddress, accountAddress);
                if (null == nftHolderFromDB) {
                    // 账户和合约唯一指定的地址没有值的时候
                    if (count < 0) {
                        throw new TransferInformationException("合约地址为:" + contractAddress + ",账户" + accountAddress + "持有的NFT的流入数小于流出数");
                    }
                    tokenMapper.insertNFTHolder(nftHolder);
                } else {
                    // 账户和合约唯一指定的地址有值的时候，需要增加或者减少
                    Long remain = nftHolderFromDB.getCount() + count;
                    if (remain < 0) {
                        throw new TransferInformationException("合约地址为:" + contractAddress + ",账户" + accountAddress + "持有的NFT的流入数小于流出数");
                    }
                    nftHolder.setCount(remain);
                    tokenMapper.updateNFTHolder(nftHolder);
                }
            }

        }
        //更新 tbl_transfer_table_height 的高度
        tableHeightMapper.updateTableHeightByTableName(TBL_TRANSFER_ID_FOR_TOKEN_HOLDER, endId);
    }
}
