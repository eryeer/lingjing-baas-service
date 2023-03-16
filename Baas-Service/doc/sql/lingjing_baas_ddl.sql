DROP DATABASE IF EXISTS lingjing_baas;
CREATE DATABASE lingjing_baas
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci;
USE lingjing_baas;

DROP TABLE IF EXISTS tbl_user;
CREATE TABLE `tbl_user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1:已启用 0:已删除 2:已停用）',

    `user_id` varchar(32) NOT NULL COMMENT '用户id',
    `user_type` varchar(5) NOT NULL DEFAULT '' COMMENT '认证类型（PC 个人认证, EC 企业认证）',
    `user_name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户姓名',
    `phone_number` varchar(20) NOT NULL COMMENT '手机',
    `password` varchar(64) NOT NULL COMMENT '密码',
    `role` varchar(5) NOT NULL COMMENT '角色（CU 普通用户, PM 平台管理员）',
    `id_number` varchar(18) NOT NULL DEFAULT '' COMMENT '身份证号',

    `company_name` varchar(50) NOT NULL DEFAULT '' COMMENT '企业名称',
    `uni_social_credit_code` varchar(18) NOT NULL DEFAULT '' COMMENT '统一社会信用代码（必须为18位的字母数字组合）',
    `legal_person_name` varchar(10) NOT NULL DEFAULT '' COMMENT '法人姓名',
    `legal_person_idn` varchar(18) NOT NULL DEFAULT '' COMMENT '法人身份证号码',
    `apply_time` bigint NOT NULL DEFAULT 0 COMMENT '申请时间',
    `approve_status` varchar(15) NOT NULL DEFAULT '' COMMENT '审批状态（'': 未提交, Pending：待认证 Approved：已认证 Rejected：已拒绝 ToBeUpdated：待修改）',
    `approve_feedback` varchar(500) NOT NULL DEFAULT '' COMMENT '审批反馈信息',
    `approve_time` bigint NOT NULL DEFAULT 0 COMMENT '审批时间',

    -- files
    `business_license_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '营业执照正本',
    `ida_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '身份证正面（人像）',
    `idb_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '身份证反面（国徽）',
    `legal_person_ida_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '法人身份证正面',
    `legal_person_idb_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '法人身份证反面',

    UNIQUE KEY `idx_phone_number`(`phone_number`),
    UNIQUE KEY `uk_user_id`(`user_id`),
    PRIMARY KEY (`id`)
) COMMENT='用户信息表';

-- 默认平台管理员(密码采用SHA256格式)
insert into tbl_user (user_id,user_type,user_name,phone_number,password,role,approve_status)
values ("admin","EC","admin","18801791237","9a13ee20f791275ef0b253fe78c8bb8016c76e6d652e02dcbf65d8ad0dd3a6b0","PM",'Approved');

DROP TABLE IF EXISTS tbl_approve_history;
CREATE TABLE `tbl_approve_history` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1:已启用 0:已删除 2:已停用）',

    `kyc_type` varchar(10) NOT NULL DEFAULT 'NEW' COMMENT '核验类型（NEW 新用户KYC, UPDATE 用户认证信息变更）',

    `user_id` varchar(32) NOT NULL COMMENT '用户id',
    `user_type` varchar(5) NOT NULL DEFAULT '' COMMENT '认证类型（PC 个人认证, EC 企业认证）',
    `user_name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户姓名',
    `phone_number` varchar(20) NOT NULL COMMENT '手机',
    `password` varchar(64) NOT NULL COMMENT '密码',
    `role` varchar(5) NOT NULL COMMENT '角色（CU 普通用户, PM 平台管理员）',
    `id_number` varchar(18) NOT NULL DEFAULT '' COMMENT '身份证号',

    `company_name` varchar(50) NOT NULL DEFAULT '' COMMENT '企业名称',
    `uni_social_credit_code` varchar(18) NOT NULL DEFAULT '' COMMENT '统一社会信用代码（必须为18位的字母数字组合）',
    `legal_person_name` varchar(10) NOT NULL DEFAULT '' COMMENT '法人姓名',
    `legal_person_idn` varchar(18) NOT NULL DEFAULT '' COMMENT '法人身份证号码',
    `apply_time` bigint NOT NULL DEFAULT 0 COMMENT '申请时间',
    `approve_status` varchar(15) NOT NULL DEFAULT 'Pending' COMMENT '审批状态（Pending：待认证 Approved：已认证 Rejected：已拒绝 ToBeUpdated：待修改）',
    `approve_feedback` varchar(500) NOT NULL DEFAULT '' COMMENT '审批反馈信息',
    `approve_time` bigint NOT NULL DEFAULT 0 COMMENT '审批时间',

    -- files
    `business_license_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '营业执照正本',
    `ida_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '身份证正面（人像）',
    `idb_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '身份证反面（国徽）',
    `legal_person_ida_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '法人身份证正面',
    `legal_person_idb_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '法人身份证反面',

    KEY `idx_phone_number`(`phone_number`),
    KEY `idx_user_id`(`user_id`),
    PRIMARY KEY (`id`)
) COMMENT='用户认证审批历史表';

DROP TABLE IF EXISTS tbl_login_log;
CREATE TABLE `tbl_login_log` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1：有效  0：无效）',

    `user_id` varchar(32) NOT NULL COMMENT '会员号',
    `login_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
	KEY `idx_user_id` (`user_id`),
	PRIMARY KEY (`id`)
) COMMENT='用户登录日志表';

DROP TABLE IF EXISTS tbl_sms_code;
CREATE TABLE `tbl_sms_code` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
  `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1：有效  0：无效）',

  `phone_number` varchar(18) NOT NULL COMMENT '手机号',
  `code` varchar(6) NOT NULL COMMENT '验证码',
  `code_type` varchar(8) NOT NULL COMMENT '验证码类型（R:注册验证码, L:登录验证码）',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `expiration_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`)
) COMMENT='短信验证码表';

DROP TABLE IF EXISTS tbl_cos_file;
CREATE TABLE `tbl_cos_file` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
  `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1：有效  0：无效）',

  `user_id` varchar(32) NOT NULL COMMENT '上传用户id',
  `uuid` varchar(32) NOT NULL COMMENT '文件唯一字符串标识',
  `file_suffix` varchar(8) NOT NULL COMMENT '文件后缀, jpg/png/pdf/sol/...',
  `file_length` bigint NOT NULL COMMENT '文件大小, 字节数',
  `bucket_name` varchar(50)  NOT NULL COMMENT 'bucket名称',
  `file_name` varchar(255) NULL DEFAULT '' COMMENT '原始文件名',
  `file_key` varchar(255) NULL DEFAULT '' COMMENT '云服务文件key',
  `is_temp` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否临时文件',
  `file_type` varchar(10) NOT NULL COMMENT '业务类型（前端约定枚举类型（身份证，营业执照,合约等，详见接口文档））',
  UNIQUE KEY `uk_uuid`(`uuid`),
  PRIMARY KEY (`id`)
) COMMENT='文件上传信息表';

DROP TABLE IF EXISTS tbl_chain_account;
CREATE TABLE `tbl_chain_account` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(32) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1:已启用 0:已删除 2:已停用）',
    `user_id` varchar(32) NOT NULL COMMENT '用户id',
    `name` varchar(32) NOT NULL COMMENT '链账户名称',
    `is_gas_transfer` tinyint NOT NULL COMMENT '链用户是否可以gas 转账  1:允许 0:不允许',
    `is_custody` tinyint NOT NULL COMMENT '链用户私钥是否托管 1:托管 0:不托管',
    `user_address` varchar(42) NOT NULL COMMENT '链账户地址',
    `encode_key` varchar(108) NOT NULL DEFAULT '' COMMENT '链账户私钥 经过 aes 与 base64处理过的密文',
    KEY `idx_user_id`(`user_id`),
    KEY `idx_user_address`(`user_address`),
    PRIMARY KEY (`id`)
) COMMENT='用户链账户表';

DROP TABLE IF EXISTS tbl_gas_summary;
CREATE TABLE `tbl_gas_summary` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1：有效  0：无效）',

    `user_id` varchar(32) NOT NULL COMMENT '用户id',
    `apply_amount` varchar(100) NOT NULL DEFAULT '0' COMMENT '申领总数量',
    `agreement_amount` varchar(100) NOT NULL DEFAULT '0' COMMENT '签约总数量',
    `apply_time` bigint NOT NULL  DEFAULT 0 COMMENT '燃料最近申领时间',
    `agreement_time` bigint NOT NULL  DEFAULT 0 COMMENT '燃料最近签约时间',

    unique `uk_user_id`(`user_id`),
    PRIMARY KEY (`id`)
) COMMENT='燃料汇总表 包括申领 签约信息';

DROP TABLE IF EXISTS tbl_gas_apply;
CREATE TABLE `tbl_gas_apply` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1：上链中 2:成功  0：失败）',

    `user_id` varchar(32) NOT NULL COMMENT '用户id',
    `user_address` varchar(42) NOT NULL COMMENT '链账户地址',
    `name` varchar(32) NOT NULL COMMENT '链账户名称',
    `apply_amount` varchar(100) NOT NULL COMMENT '燃料申领数量',
    `apply_time` bigint NOT NULL  DEFAULT 0 COMMENT '燃料申领时间',
    `tx_hash` VARCHAR(66)  NOT NULL DEFAULT '' COMMENT '交易hash',
    `retries` tinyint  NOT NULL DEFAULT 0 COMMENT '重试次数',

    KEY `idx_user_id`(`user_id`),
    KEY `idx_user_address`(`user_address`),
    PRIMARY KEY (`id`)
) COMMENT='燃料申领记录表';


DROP TABLE IF EXISTS tbl_gas_contract;
CREATE TABLE `tbl_gas_contract` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '0' COMMENT '签约状态（默认字段,2: 驳回 1：通过  0：审核中）',
    `contract_file_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '合同文件的uuid',
    `flow_id` varchar(20) NOT NULL DEFAULT '' COMMENT '流水号（LSH-HT-六位数字字符）',
    `user_id` varchar(32) NOT NULL COMMENT '用户id',
    `agreement_amount` varchar(100) NOT NULL COMMENT '签约的燃料数量',
    `approved_time` bigint NOT NULL DEFAULT 0 COMMENT '审核完成的时间',
    `upload_time` bigint NOT NULL  DEFAULT 0 COMMENT '合同上传时间',
    `feedback` varchar(500) NOT NULL DEFAULT '' COMMENT '反馈意见',

    KEY `idx_user_id`(`user_id`),
    UNIQUE KEY `uk_flow_id`(`flow_id`),
    PRIMARY KEY (`id`)
) COMMENT='燃料签约记录表';

DROP TABLE IF EXISTS tbl_internal_txns;
create table tbl_internal_txns
(
    id            bigint                                not null auto_increment,
    create_time   datetime    default CURRENT_TIMESTAMP not null,
    update_time   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    status        varchar(20) default ''                not null,

    type          varchar(20)                           not null comment 'sub-call type',
    value         varchar(30)                           not null comment 'value of transfer in Gwei',
    from_address  varchar(42)                           not null comment 'hex string of the caller account address',
    to_address    varchar(42)                           not null comment 'hex string of the receiver account address',
    gas           bigint                                not null comment 'gas limit, in wei',
    gas_used      bigint                                not null comment 'gas used, in wei',
    input         mediumtext                            not null comment 'call data',
    output        mediumtext                            not null comment 'return data',
    error         mediumtext                            not null comment 'error',
    parent_id     bigint      default 0                 not null,

    tx_hash       varchar(66)                           not null comment 'transaction hash',

    block_time    bigint                                not null comment 'timestamp',
    block_number  bigint                                not null comment 'block number',


    KEY `uk_from` (`from_address`),
    KEY `uk_to` (`to_address`),
    KEY `uk_tx_hash` (`tx_hash`),
    PRIMARY KEY (`id`)
) COMMENT ='内部交易记录表' charset = utf8mb4 collate utf8mb4_general_ci;