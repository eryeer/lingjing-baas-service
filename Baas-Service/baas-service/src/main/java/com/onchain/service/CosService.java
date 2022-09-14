package com.onchain.service;

import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.CosFile;
import com.onchain.entities.request.RequestMarkFiles;
import com.onchain.entities.response.ResponseCosFile;
import com.onchain.entities.response.ResponseFile;
import com.onchain.exception.CommonException;
import com.onchain.mapper.CosFileMapper;
import com.onchain.util.CommonUtil;
import com.onchain.util.FileUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CosService {

    @Autowired
    private COSClient cosClient;
    @Autowired
    private ParamsConfig paramsConfig;
    @Autowired
    private CosFileMapper cosFileMapper;

    // 从服务器本地上传文件
    public void putObj(File localFile, String key) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(paramsConfig.cosBucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        log.info("Put object " + localFile.getAbsolutePath() + " as " + key + putObjectResult.getCrc64Ecma());
    }

    // 从输入流上传文件
    public void putObj(InputStream inputStream, String key, Long length, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(length);
        // 设置 Content type, 默认是 application/octet-stream 例如 "application/pdf"
        objectMetadata.setContentType(contentType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(paramsConfig.cosBucketName, key, inputStream, objectMetadata);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        log.info("Put object " + key + putObjectResult.getCrc64Ecma());
    }

    // 获取当前目录下文件对象列表(不包含子目录下的对象)
    public List<COSObjectSummary> GetObjList(String prefix) {
        List<COSObjectSummary> result = new ArrayList<>();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(paramsConfig.cosBucketName);
        // prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix(prefix);
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("/");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosClientException e) {
                e.printStackTrace();
                throw e;
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            // List<String> commonPrefixs = objectListing.getCommonPrefixes();
            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            result.addAll(cosObjectSummaries);
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        return result;
    }

    // 获取下载输入流
    public COSObjectInputStream downloadStream(String key) {
        String bucketName = paramsConfig.cosBucketName;
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        COSObject cosObject = cosClient.getObject(getObjectRequest);
        return cosObject.getObjectContent();
    }

    // 下载文件到本地
    public ObjectMetadata downloadLocal(String key, String localPath) {
        String bucketName = paramsConfig.cosBucketName;
        File downFile = new File(localPath);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        return cosClient.getObject(getObjectRequest, downFile);
    }

    // 获取对象元数据
    public ObjectMetadata getObjMeta(String key) {
        String bucketName = paramsConfig.cosBucketName;
        return cosClient.getObjectMetadata(bucketName, key);
    }

    // 删除文件
    public void delObj(String key) {
        String bucketName = paramsConfig.cosBucketName;
        cosClient.deleteObject(bucketName, key);
    }

    // 获取临时地址
    public String getTempUrl(String key, String bucketName, Integer expireSeconds) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + expireSeconds * 1000L);
        req.setExpiration(expirationDate);
        URL url = cosClient.generatePresignedUrl(req);
        return url.toString();
    }

    // 若文件不存在，返回null
    public ResponseCosFile getCosFile(String uuid) {
        ResponseCosFile file = cosFileMapper.getFileInfo(uuid);
        if (file != null) {
            file.setUrl(getTempUrl(file.getFileKey(), file.getBucketName(), CommonConst.FILE_URL_VALID_TIME));
        }
        return file;
    }

    public List<ResponseCosFile> getCosFiles(List<String> uuids) {
        List<ResponseCosFile> files = cosFileMapper.getFileByIds(uuids);
        for (ResponseCosFile file : files) {
            file.setUrl(getTempUrl(file.getFileKey(), file.getBucketName(), CommonConst.FILE_URL_VALID_TIME));
        }
        return files;
    }

    public ResponseFile uploadFile(MultipartFile file, String fileType, String userId) throws IOException, CommonException {
        String fileName = file.getOriginalFilename();
        String suffix = FileUtil.getFileSuffix(fileName);
        if (!FileUtil.isFileTypeSupport(suffix, fileType)) {
            throw new CommonException(ReturnCode.FILE_UPLOAD_FAILED, "不支持的文件类型");
        }

        if (file.getSize() > CommonConst.FILE_MAX_SIZE) {
            throw new CommonException(ReturnCode.FILE_SIZE_ERROR);
        }

        String uuid = CommonUtil.getUUID();
        String fileKey = String.format("%s%s/%s.%s", CommonConst.FILE_ROOT, fileType, uuid, suffix);
        putObj(file.getInputStream(), fileKey, file.getSize(), file.getContentType());
        // 插入文件表
        cosFileMapper.insertFile(CosFile.builder().bucketName(paramsConfig.cosBucketName)
                .fileKey(fileKey)
                .fileLength(file.getSize())
                .fileName(fileName)
                .fileSuffix(suffix)
                .fileType(fileType)
                .isTemp(true)
                .userId(userId)
                .uuid(uuid)
                .build());
        // 获取临时地址
        String tempUrl = getTempUrl(fileKey, paramsConfig.cosBucketName, CommonConst.FILE_URL_VALID_TIME);
        return ResponseFile.builder()
                .fileName(fileName)
                .uuid(uuid)
                .tempUrl(tempUrl)
                .build();
    }

    public ResponseFile updateFile(MultipartFile file, String uuid, String userId) throws IOException, CommonException {
        String fileName = file.getOriginalFilename();
        String suffix = FileUtil.getFileSuffix(fileName);
        CosFile cosFile = cosFileMapper.getFile(uuid);

        if (!StringUtils.equals(fileName, cosFile.getFileName())) {
            throw new CommonException(ReturnCode.FILE_UPDATE_ERROR, "不支持的文件类型");
        }
        if (!StringUtils.equals(userId, cosFile.getUserId())) {
            throw new CommonException(ReturnCode.FILE_UPDATE_ERROR, "userId 不一致");
        }
        if (file.getSize() > CommonConst.FILE_MAX_SIZE) {
            throw new CommonException(ReturnCode.FILE_SIZE_ERROR);
        }

        cosFile.setFileLength(file.getSize());
        putObj(file.getInputStream(), cosFile.getFileKey(), file.getSize(), file.getContentType());
        // 更新文件表
        cosFileMapper.updateFile(cosFile);
        // 获取临时地址
        String tempUrl = getTempUrl(cosFile.getFileKey(), paramsConfig.cosBucketName, CommonConst.FILE_URL_VALID_TIME);
        return ResponseFile.builder()
                .fileName(fileName)
                .uuid(uuid)
                .tempUrl(tempUrl)
                .build();
    }

    @Transactional
    public ResponseFile uploadLocalFile(String localFilePath, String fileType, String userId) throws IOException, CommonException {
        File file = new File(localFilePath);
        String fileName = file.getName();
        String suffix = FileUtil.getFileSuffix(fileName);

        String uuid = CommonUtil.getUUID();
        String fileKey = String.format("%s%s/%s.%s", CommonConst.FILE_ROOT, fileType, uuid, suffix);
        putObj(file, fileKey);
        // 插入文件表
        cosFileMapper.insertFile(CosFile.builder().bucketName(paramsConfig.cosBucketName)
                .fileKey(fileKey)
                .fileLength(file.length())
                .fileName(fileName)
                .fileSuffix(suffix)
                .fileType(fileType)
                .isTemp(false)
                .userId(userId)
                .uuid(uuid)
                .build());
        // 获取临时地址
        String tempUrl = getTempUrl(fileKey, paramsConfig.cosBucketName, CommonConst.FILE_URL_VALID_TIME);
        return ResponseFile.builder()
                .fileName(fileName)
                .uuid(uuid)
                .tempUrl(tempUrl)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void markFiles(RequestMarkFiles requestMarkFiles) {
        if (requestMarkFiles.getLinkFileIds().size() > 0) {
            cosFileMapper.markFileUsed(requestMarkFiles.getLinkFileIds());
        }

        if (requestMarkFiles.getDeleteFileIds().size() > 0) {
            cosFileMapper.deleteFiles(requestMarkFiles.getLinkFileIds());
        }
    }

    public void deleteOutDateCos() {
        Date date = DateUtils.addMonths(new Date(), -1);
        cosFileMapper.deleteOutDateCos(date);
    }
}
