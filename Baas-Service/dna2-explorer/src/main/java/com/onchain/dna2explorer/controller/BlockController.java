package com.onchain.dna2explorer.controller;

import com.github.pagehelper.PageInfo;

import com.onchain.dna2explorer.aop.operlog.OperLogAnnotation;
import com.onchain.dna2explorer.constants.UrlConst;
import com.onchain.dna2explorer.model.dao.Block;
import com.onchain.dna2explorer.model.dao.ResponseFormat;
import com.onchain.dna2explorer.model.response.ResponseBlock;
import com.onchain.dna2explorer.service.BlockService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @GetMapping(value = UrlConst.GET_BLOCK_LIST)
    @ApiOperation(value = "获取区块列表", notes = "获取区块列表")
    @OperLogAnnotation(description = "getBlockList")
    public ResponseFormat<PageInfo<ResponseBlock>> getBlockList(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                                @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize) {
        PageInfo<ResponseBlock> result = blockService.getBlockList(pageNumber, pageSize);
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.GET_BLOCK)
    @ApiOperation(value = "获取区块详情", notes = "获取区块详情")
    @OperLogAnnotation(description = "getBlock")
    public ResponseFormat<Block> getBlock(@RequestParam Integer blockNumber) {
        Block result = blockService.getBlock(blockNumber);
        return new ResponseFormat<>(result);
    }
}
