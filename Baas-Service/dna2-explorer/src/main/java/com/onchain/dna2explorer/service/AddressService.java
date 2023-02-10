package com.onchain.dna2explorer.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.exception.CommonException;
import com.onchain.dna2explorer.mapper.*;
import com.onchain.dna2explorer.model.dao.Account;
import com.onchain.dna2explorer.model.dao.Transaction;
import com.onchain.dna2explorer.model.response.ResponseAddress;
import com.onchain.dna2explorer.model.response.ResponseTransfer;
import com.onchain.dna2explorer.model.response.ResponseTransferPageInfo;
import com.onchain.dna2explorer.utils.CsvUtil;
import com.onchain.entities.response.ResponseUserContractInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class AddressService {

    private final TransactionMapper transactionMapper;
    private final TransferMapper transferMapper;
    private final AccountMapper accountMapper;
    private final ContractMapper contractMapper;
    private final TokenMapper tokenMapper;
    private final Web3j web3j;

    public PageInfo<ResponseAddress> getAddressList(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<ResponseAddress> list = accountMapper.getAddressList();
        return new PageInfo<>(list);
    }

    public List<ResponseAddress> getAddressList(List<String> addressList) {
        return accountMapper.getAddressListByAddress(addressList);
    }

    public ResponseAddress getAddress(String address) {
        ResponseAddress result = accountMapper.getAddress(address);
        if (result != null && result.getType() == 1) {
            Transaction tx = transactionMapper.getContractCreateTx(address);
            if (tx != null) {
                result.setCreator(tx.getFromAddress());
                result.setCreateTxHash(tx.getTxHash());
            }
            Long sum =tokenMapper.getNFTHolderSumByContractAddress(address);
            result.setTokenHolderSum(sum);
            result.setContractInfo(contractMapper.getResponseContract(address));
        }
        return result;
    }

    public ResponseTransferPageInfo getTransferListByAddress(Integer pageNumber, Integer pageSize, String address) {
        Integer offset = (pageNumber - 1)*pageSize;
        List<ResponseTransfer> list = transferMapper.getTransferList(address, offset, pageSize);
        ResponseTransferPageInfo responseTransferPageInfo = new ResponseTransferPageInfo();
        responseTransferPageInfo.setList(list);
        responseTransferPageInfo.setPageNum(pageNumber);
        responseTransferPageInfo.setPageSize(pageSize);
        Integer total = transferMapper.getTransferListCountByTransferFrom(address);
        total = total + transferMapper.getTransferListCountByTransferTo(address);
        responseTransferPageInfo.setTotal(total);
        return responseTransferPageInfo;
    }

    // get balance and nonce for the accounts
    public void updateAccounts(List<Account> accounts) throws Exception {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Account account : accounts) {
            futures.add(web3j
                    .ethGetBalance(account.getAddress(), DefaultBlockParameter.valueOf(Constant.LatestBlockNumberKey))
                    .sendAsync()
                    .thenApply(balance -> {
                        account.setBalance(balance.getBalance().divide(Constant.GWeiFactor).toString());
                        return balance;
                    })
            );
            futures.add(web3j
                    .ethGetTransactionCount(account.getAddress(), DefaultBlockParameter.valueOf(Constant.LatestBlockNumberKey))
                    .sendAsync()
                    .thenApply(count -> {
                        account.setNonce(count.getTransactionCount().intValue());
                        return count;
                    })
            );
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }


    public void getLatest5kTransferListByAddress(String address, long startTime, long endTime, HttpServletResponse response) throws CommonException {
        List<ResponseTransfer> transferList = transferMapper.getLatest5KTransferList(address, startTime, endTime);
        CsvUtil.transfersToCsvOutputStream(transferList, response);
    }

}
