package com.onchain.entities.dao;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PdfsShare extends Base {
    private String fromUserId;
    private String fileHash;
    private String toUserId;
}
