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
    `approve_status` varchar(15) NOT NULL DEFAULT 'Pending' COMMENT '审批状态（Pending：待认证 Approved：已认证 Rejected：已拒绝 ToBeUpdated：待修改）',
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
insert into tbl_user (user_id,user_type,user_name,phone_number,password,role,
id_number,company_name,uni_social_credit_code,legal_person_name,legal_person_idn,
approve_status,approve_feedback,approve_time,ida_file_uuid,idb_file_uuid,
legal_person_ida_file_uuid,legal_person_idb_file_uuid,business_license_file_uuid)
values ("admin","EC","admin","18801791237","9a13ee20f791275ef0b253fe78c8bb8016c76e6d652e02dcbf65d8ad0dd3a6b0","PM",
'','','','','',
'Approved','',now(),'','',
'','','');

DROP TABLE IF EXISTS tbl_approve_history;
CREATE TABLE `tbl_approve_history` (
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
    `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `approve_status` varchar(15) NOT NULL DEFAULT 'Pending' COMMENT '审批状态（Pending：待认证 Approved：已认证 Rejected：已拒绝 ToBeUpdated：待修改）',
    `approve_feedback` varchar(500) NOT NULL DEFAULT '' COMMENT '审批反馈信息',
    `approve_time` datetime NULL COMMENT '审批时间',
    `kyc_type` varchar(10) NOT NULL DEFAULT 'NEW' COMMENT '核验类型（NEW 新用户KYC, UPDATE 用户认证信息变更）',

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
    `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1:已启用 0:已删除 2:已停用）',

    `user_id` varchar(32) NOT NULL COMMENT '用户id',
    `user_address` varchar(42) NOT NULL COMMENT '链账户地址',
    `balance` varchar(30) NOT NULL COMMENT '账户燃料余额',
    `apply_time` datetime NULL COMMENT '最近申领燃料时间',
    `private_key` varchar(66) NOT NULL COMMENT '链账户私钥',
    `wallet_pass` varchar(30) NOT NULL COMMENT '钱包文件密码',
    
    -- files
    `wallet_file_uuid` varchar(32) NOT NULL COMMENT '钱包文件uuid',

    UNIQUE KEY `uk_user_id`(`user_id`),
    PRIMARY KEY (`id`)
) COMMENT='用户链账户表';

DROP TABLE IF EXISTS tbl_gas_apply;
CREATE TABLE `tbl_gas_apply` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1：有效  0：无效）',

    `user_id` varchar(32) NOT NULL COMMENT '用户id',
    `user_address` varchar(42) NOT NULL COMMENT '链账户地址',
    `apply_amount` varchar(30) NOT NULL COMMENT '燃料申领数量',
    `apply_time` datetime NULL COMMENT '燃料申领时间',

    KEY `idx_user_id`(`user_id`),
    PRIMARY KEY (`id`)
) COMMENT='燃料申领记录表';

