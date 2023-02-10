--v5   第五个迭代
alter table tbl_gas_apply add retries tinyint not null default 0 comment '重试次数';
alter table tbl_gas_apply modify column status varchar(20) NOT NULL DEFAULT '1' comment '状态（默认字段,1：上链中 2:成功  0：失败）';

--v6 第六迭代

alter table tbl_user add has_kyc_notify tinyint not null default 0 comment '是否有Kyc变更信息';

DROP TABLE IF EXISTS tbl_kyc_update;
CREATE TABLE `tbl_kyc_update` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（默认字段,1:已启用 0:已删除）',

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
) COMMENT='用户认证信息信息变更表';

