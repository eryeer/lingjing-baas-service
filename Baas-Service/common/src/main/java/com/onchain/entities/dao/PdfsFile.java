package com.onchain.entities.dao;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PdfsFile extends Base {
    private String userId;
    private String fileHash;
    private String txHash;
    private String fileSuffix;
    private Long fileLength;
    private String fileName;
    private Date uploadTime;
}
