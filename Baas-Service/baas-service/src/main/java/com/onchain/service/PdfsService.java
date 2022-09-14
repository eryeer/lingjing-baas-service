package com.onchain.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.PdfsFile;
import com.onchain.entities.dao.PdfsShare;
import com.onchain.entities.dao.User;
import com.onchain.entities.rest.RequestPdfsDownload;
import com.onchain.entities.rest.RequestPdfsUpload;
import com.onchain.entities.rest.ResponsePdfsDownload;
import com.onchain.entities.rest.ResponsePdfsUpload;
import com.onchain.exception.CommonException;
import com.onchain.mapper.PdfsMapper;
import com.onchain.mapper.PdfsShareMapper;
import com.onchain.mapper.UserMapper;
import com.onchain.util.FileUtil;
import com.onchain.util.SFTPUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PdfsService {

    private final PdfsMapper pdfsMapper;
    private final UserMapper userMapper;
    private final PdfsShareMapper pdfsShareMapper;
    private final ParamsConfig paramsConfig;
    private final RestTemplate restTemplate;
    private final SFTPUtil sftpUtil;

    public void uploadPdfs(String userId, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new CommonException(ReturnCode.FILE_UPLOAD_FAILED, "文件名不能为空！");
        }
        // Check if the file's name contains invalid characters
        if (fileName.contains("..")) {
            throw new CommonException(ReturnCode.FILE_UPLOAD_FAILED, "Sorry! Filename contains invalid path sequence " + fileName);
        }
        String suffix = FileUtil.getFileSuffix(fileName);

        if (file.getSize() > CommonConst.FILE_MAX_SIZE) {
            throw new CommonException(ReturnCode.FILE_SIZE_ERROR);
        }

//        try {
//            sftpUtil.login();
//            sftpUtil.upload(paramsConfig.pdfsBaseDir, StringUtils.join(new String[]{paramsConfig.pdfsUploadFolder, userId}, "/"), fileName, file.getInputStream());
//
//        } catch (Exception ex) {
//            log.error("storeFile error: ", ex);
//            throw new CommonException(ReturnCode.FILE_UPLOAD_FAILED, "Could not store file " + fileName + ". Please try again!");
//        } finally {
//            sftpUtil.logout();
//        }
        String localPath = StringUtils.join(new String[]{paramsConfig.pdfsBaseDir, paramsConfig.pdfsUploadFolder, userId, fileName}, "/");

        storeFile(localPath, file);
        RequestPdfsUpload request = RequestPdfsUpload.builder()
                .bcAction("uploadData")
                .version("1.0.0")
                .filePath(localPath)
                .fileDesc(fileName)
                .build();
        ResponsePdfsUpload response = restTemplate.postForObject(paramsConfig.pdfsUploadUrl, request, ResponsePdfsUpload.class);
        if (response == null || !CommonConst.SUCCESS.equals(response.getDesc())) {
            throw new CommonException(ReturnCode.FILE_UPLOAD_FAILED, "PDFS rest call failed: " + JSON.toJSONString(response));
        }

        // 插入文件表
        pdfsMapper.insertPdfsFile(PdfsFile.builder()
                .fileHash(response.getData().getFileHash())
                .txHash(response.getData().getTxHash())
                .fileLength(file.getSize())
                .fileName(fileName)
                .fileSuffix(suffix)
                .userId(userId)
                .uploadTime(new Date())
                .build());
    }

    public void sharePdfs(String fileHash, String fromUserId, String phoneNumber) {
        User toUser = userMapper.getUserByPhoneNumber(phoneNumber);
        if (toUser == null) {
            throw new CommonException(ReturnCode.USER_NOT_EXIST);
        }
        // self not need to share
        if (StringUtils.equals(fromUserId, toUser.getUserId())) {
            return;
        }

        PdfsFile file = pdfsMapper.getPdfsFileByHash(fileHash);
        if (file == null) {
            throw new CommonException(ReturnCode.FILE_NOT_FOUND);
        }

        if (!StringUtils.equals(fromUserId, file.getUserId())) {
            PdfsShare pdfsShare = pdfsShareMapper.getPdfsShare(fromUserId, fileHash);
            if (pdfsShare == null) {
                throw new CommonException(ReturnCode.FILE_SHARE_ERROR, "没有对应文件共享权限");
            }
        }

        pdfsShareMapper.insertPdfsShare(PdfsShare.builder().fromUserId(fromUserId).toUserId(toUser.getUserId()).fileHash(fileHash).build());
    }

    public PageInfo<PdfsFile> getPdfsList(Integer pageNumber, Integer pageSize, String uploaderType, String userId) {
        PageHelper.startPage(pageNumber, pageSize);
        List<PdfsFile> result = pdfsMapper.getPdfsFileList(uploaderType, userId);
        return new PageInfo<>(result);
    }

    public PdfsFile getPdfsFile(String userId, String fileHash) {
        return pdfsMapper.getPdfsFile(userId, fileHash);
    }

    public Resource downloadPdfs(String fileHash) {
        RequestPdfsDownload request = RequestPdfsDownload.builder()
                .bcAction("uploadData")
                .version("1.0.0")
                .fileHash(fileHash)
                .build();

        ResponsePdfsDownload response = restTemplate.postForObject(paramsConfig.pdfsDownloadUrl, request, ResponsePdfsDownload.class);
        if (response == null || !CommonConst.SUCCESS.equals(response.getDesc())) {
            throw new CommonException(ReturnCode.FILE_DOWNLOAD_FAILED, "PDFS rest call failed: " + JSON.toJSONString(response));
        }
//        try {
//            sftpUtil.login();
//            Path fullPath = Paths.get(paramsConfig.pdfsBaseDir, response.getData().getFilePath());
//            // use linux path
//            byte[] bytes = sftpUtil.downloadBytes(fullPath.getParent().toString().replace('\\', '/'), fullPath.getFileName().toString());
//            return new ByteArrayResource(bytes);
//        } catch (Exception ex) {
//            log.error("storeFile error: ", ex);
//            throw new CommonException(ReturnCode.FILE_DOWNLOAD_FAILED, "Could not get file from pdfs-cli. Please try again!");
//        } finally {
//            sftpUtil.logout();
//        }
        return loadFileAsResource(Paths.get(paramsConfig.pdfsBaseDir, response.getData().getFilePath()));
    }

    public Resource loadFileAsResource(Path filePath) {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new CommonException(ReturnCode.FILE_DOWNLOAD_FAILED, "File not found " + filePath);
            }
        } catch (MalformedURLException ex) {
            log.error("FileStorageService loadFileAsResource error: ", ex);
            throw new CommonException(ReturnCode.FILE_DOWNLOAD_FAILED, "File not found " + filePath);
        }
    }

    public String storeFile(String filePath, MultipartFile file) {
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(filePath);
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toAbsolutePath().toString();
        } catch (IOException ex) {
            log.error("storeFile error: ", ex);
            throw new CommonException(ReturnCode.FILE_UPLOAD_FAILED, "Could not store file " + file.getOriginalFilename() + ". Please try again!");
        }
    }

}
