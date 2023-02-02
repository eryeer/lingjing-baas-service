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
public class Node {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 地区
     */
    private String region;

    /**
     * 节点的IP
     */
    private String ip;

    /**
     * 节点的rest端口
     */
    private Integer restPort;

    /**
     * 节点的版本号
     */
    private String version;

    /**
     * 是否处于活跃状态
     */
    private Boolean isActive;
}