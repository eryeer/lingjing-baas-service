package com.onchain.entities.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPdfsDownload {
    private String bcAction;
    private String version;
    private String fileHash;
}

