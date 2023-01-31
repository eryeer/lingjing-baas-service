package com.onchain.dna2explorer.utils;

import com.onchain.dna2explorer.constants.ReturnCode;
import com.onchain.dna2explorer.exception.CommonException;
import com.onchain.dna2explorer.model.response.ResponseTransaction;
import com.onchain.dna2explorer.model.response.ResponseTransfer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.web3j.utils.Convert;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CsvUtil {
    private static SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void transfersToCsvOutputStream(List<ResponseTransfer> transferList, HttpServletResponse response) throws CommonException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter buffCvsWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        String[] headArr = new String[]{"交易哈希", "从", "转账到", "数字藏品ID", "数字藏品标识", "合约地址", "生成时间"};


        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename="+  URLEncoder.encode("transfers","UTF-8") );
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            buffCvsWriter.write('\ufeff');
            buffCvsWriter.write(String.join(",", headArr)+"\n");
            for(ResponseTransfer responseTransfer : transferList) {
                String icon = responseTransfer.getTokenName() != null && !StringUtils.equals(responseTransfer.getTokenName(), "")  ? responseTransfer.getTokenName() : "";
                icon += responseTransfer.getTokenSymbol() != null && !StringUtils.equals(responseTransfer.getTokenSymbol(), "") ? "("+responseTransfer.getTokenSymbol()+")" : "";
                buffCvsWriter.write(StringUtils.join(new String[]{
                                responseTransfer.getTxHash(), responseTransfer.getTransferFrom(), responseTransfer.getTransferTo(),responseTransfer.getTokenId(), icon, responseTransfer.getContractAddress(), DateFor.format(new Date(responseTransfer.getBlockTime()))},
                        ","));
                buffCvsWriter.newLine();
            }
            buffCvsWriter.flush();
            FileCopyUtils.copy(outputStream.toByteArray(), response.getOutputStream());
        } catch (Exception e) {
            throw new CommonException(ReturnCode.GENERATE_CSV_ERROR);
        } finally {
            // 释放资源
            try {
                buffCvsWriter.close();
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
                throw new CommonException(ReturnCode.GENERATE_CSV_ERROR);
            }
        }

    }

    public static void transactionsToCsvOutputStream(List<ResponseTransaction> transactionsList, HttpServletResponse response) throws CommonException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter buffCvsWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        String[] headArr = new String[]{"交易哈希", "状态", "方法名", "区块高度", "发起地址", "接收地址", "数量（Ether）", "消耗燃料（GWei）", "生成时间"};

        response.reset();
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("transactions", "UTF-8"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            buffCvsWriter.write('\ufeff');
            buffCvsWriter.write(String.join(",", headArr)+"\n");

            for (ResponseTransaction transaction : transactionsList) {
                String txStatus = StringUtils.equals(transaction.getTxStatus(), "0x1") ? "成功":"失败";
                String txValue = StringUtils.equals(transaction.getTxValue(), "0") ? "0" : Convert.fromWei(Convert.toWei(transaction.getTxValue(), Convert.Unit.GWEI), Convert.Unit.ETHER).toString();
                String consumedGasFee = Convert.fromWei(String.valueOf(transaction.getGasUsed() * transaction.getGasPrice()), Convert.Unit.GWEI).toString();
                buffCvsWriter.write(StringUtils.join(new String[]{transaction.getTxHash(), txStatus, transaction.getMethod(),
                        transaction.getBlockNumber().toString(), transaction.getFromAddress(), transaction.getToAddress(), txValue, consumedGasFee, DateFor.format(new Date(transaction.getBlockTime()))}, ","));
                buffCvsWriter.newLine();
            }
            buffCvsWriter.flush();
            FileCopyUtils.copy(outputStream.toByteArray(), response.getOutputStream());
        } catch (Exception e) {
            throw new CommonException(ReturnCode.GENERATE_CSV_ERROR);
        } finally {
            // 释放资源
            try {
                buffCvsWriter.close();
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
                throw new CommonException(ReturnCode.GENERATE_CSV_ERROR);
            }
        }
    }
}