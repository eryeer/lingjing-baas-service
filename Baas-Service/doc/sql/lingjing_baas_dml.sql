--v5   第五个迭代
alter table tbl_gas_apply add retries tinyint not null default 0 comment '重试次数';
alter table tbl_gas_apply modify column status varchar(20) NOT NULL DEFAULT '1' comment '状态（默认字段,1：上链中 2:成功  0：失败）';
