package com.onchain.entities.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePdfsDownload {
    private String bcAction;
    private String version;
    private String desc;
    private Integer error;
    private FileInfo data;

    @Data
    public class FileInfo {
        private Integer blockHeight;
        private String fileHash;
        private String txHash;
        private String filePath;
    }
}

