package com.onchain.controller.rest;

import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.request.RequestMarkFiles;
import com.onchain.entities.response.ResponseCosFile;
import com.onchain.service.CosService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserManageController {

    final CosService cosService;

    @GetMapping(value = UrlConst.REST_GET_FILE_BY_UUID)
    @ApiOperation(value = "根据uuid获取文件信息", notes = "根据uuid获取文件信息")
    @OperLogAnnotation(description = "getFileByUuid")
    public ResponseFormat<ResponseCosFile> getFileByUuid(@RequestParam String uuid) {
        return new ResponseFormat<>(cosService.getCosFile(uuid));
    }

    @PostMapping(value = UrlConst.REST_GET_FILE_BY_UUIDS)
    @ApiOperation(value = "根据uuid获取文件列表", notes = "根据uuid获取文件列表")
    @OperLogAnnotation(description = "getFileByUuids")
    public ResponseFormat<List<ResponseCosFile>> getFileByUuids(@RequestParam @NotEmpty List<String> uuids) {
        return new ResponseFormat<>(cosService.getCosFiles(uuids));
    }

    @PostMapping(value = UrlConst.REST_MARK_FILES)
    @ApiOperation(value = "标记文件状态", notes = "标记文件状态")
    @OperLogAnnotation(description = "markFiles")
    public ResponseFormat<?> markFiles(@Valid @RequestBody RequestMarkFiles requestMarkFiles) {
        cosService.markFiles(requestMarkFiles);
        return new ResponseFormat<>();
    }

}
