package com.onchain.entities.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePdfsUpload {
    private String bcAction;
    private FileInfo data;
    private String desc;
    private Integer error;
    private String version;

    @Data
    public class FileInfo {
        String fileHash;
        String txHash;
        Integer height;
    }
}
