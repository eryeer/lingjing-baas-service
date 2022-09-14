package com.onchain.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.CommonConst;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.PdfsFile;
import com.onchain.entities.dao.User;
import com.onchain.entities.request.RequestSharePdfs;
import com.onchain.exception.CommonException;
import com.onchain.service.JwtService;
import com.onchain.service.PdfsService;
import com.onchain.util.FileUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class PdfsController {

    private final PdfsService pdfsService;
    private final JwtService jwtService;

    @PostMapping(value = UrlConst.UPLOAD_PDFS)
    @ApiOperation(value = "上传文件到PDFS", notes = "上传文件到PDFS")
    @OperLogAnnotation(description = "uploadPdfs")
    public ResponseFormat<?> uploadPdfs(@RequestParam("file") MultipartFile file,
                                        @RequestHeader(name = CommonConst.HEADER_ACCESS_TOKEN, required = false) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        pdfsService.uploadPdfs(user.getUserId(), file);
        return new ResponseFormat<>();
    }

    @PostMapping(value = UrlConst.SHARE_PDFS)
    @ApiOperation(value = "共享文件", notes = "共享文件")
    @OperLogAnnotation(description = "sharePdfs")
    public ResponseFormat<?> sharePdfs(@Valid @RequestBody RequestSharePdfs requestSharePdfs,
                                       @RequestHeader(name = CommonConst.HEADER_ACCESS_TOKEN, required = false) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        pdfsService.sharePdfs(requestSharePdfs.getFileHash(), user.getUserId(), requestSharePdfs.getPhoneNumber());
        return new ResponseFormat<>();
    }

    @GetMapping(value = UrlConst.GET_PDFS_LIST)
    @ApiOperation(value = "获取PDFS文件列表", notes = "获取PDFS文件列表")
    @OperLogAnnotation(description = "getPdfsList")
    public ResponseFormat<PageInfo<PdfsFile>> getPdfsList(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                          @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                          @RequestParam(required = false) String uploaderType,
                                                          @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        PageInfo<PdfsFile> result = pdfsService.getPdfsList(pageNumber, pageSize, uploaderType, user.getUserId());
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.DOWNLOAD_PDFS)
    @ApiOperation(value = "根据文件哈希下载文件", notes = "根据文件哈希下载文件")
    @OperLogAnnotation(description = "downloadPdfs")
    public ResponseEntity<Resource> downloadPdfs(@RequestParam @NotBlank String fileHash,
                                                 @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        PdfsFile pdfsFile = pdfsService.getPdfsFile(user.getUserId(), fileHash);
        Resource resource = pdfsService.downloadPdfs(fileHash);
        String contentType = FileUtil.getContentType(pdfsFile.getFileSuffix());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfsFile.getFileName() + "\"")
                .body(resource);
    }

}
