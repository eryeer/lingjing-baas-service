package com.onchain.dna2explorer.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableHeight {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;
    private Long height;
    private String tableName;
}
