package com.onchain.entities.dao;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class CosFile extends Base {
    private String userId;
    private String uuid;
    private String fileSuffix;
    private Long fileLength;
    private String bucketName;
    private String fileName;
    private String fileKey;
    private Boolean isTemp;
    private String fileType;
}
