DROP DATABASE IF EXISTS dna2_explorer;
CREATE DATABASE dna2_explorer
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci;
USE dna2_explorer;

DROP TABLE IF EXISTS `tbl_block`;
CREATE TABLE `tbl_block` (
    `id`             	BIGINT       AUTO_INCREMENT PRIMARY KEY,
    `create_time`    	DATETIME     DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`    	DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`         	VARCHAR(20)  DEFAULT ''                                            NOT NULL,

    `block_number`   	BIGINT       NOT NULL COMMENT '区块高度',
    `block_hash`     	VARCHAR(66)  NOT NULL DEFAULT '' COMMENT '区块hash值',
    `block_time`     	BIGINT     	 NOT NULL COMMENT '区块时间戳',
    `miner`          	VARCHAR(42)  NOT NULL COMMENT 'hex string of miner address',
    `difficulty`     	bigint       NOT NULL COMMENT 'difficulty',
    `total_difficulty` 	bigint       NOT NULL COMMENT 'total difficulty',
    `block_size`        int  	     NOT NULL COMMENT 'data size in bytes',
    `gas_used`      	bigint  	 NOT NULL COMMENT 'gas used',
    `gas_limit`     	bigint 	 	 NOT NULL COMMENT 'gas limit',
    `nonce`         	VARCHAR(18)  NOT NULL COMMENT 'nonce',
    `extra_data`    	text 		COMMENT 'extra data',
    `parent_hash`   	VARCHAR(66)  NOT NULL COMMENT 'hex string of block parent hash',
    `uncle_hash`    	VARCHAR(66)  NOT NULL COMMENT 'hex string of block uncle hash',
    `state_root`    	VARCHAR(66)  DEFAULT NULL COMMENT 'state hash root',
    `receipts_root` 	VARCHAR(66)  DEFAULT NULL COMMENT 'receipts hash root',
    `transactions_root` VARCHAR(66)  DEFAULT NULL COMMENT 'transactions hash root',
    `tx_count`       	INT          NOT NULL COMMENT '区块里的交易数量',
    UNIQUE KEY `idx_block_number` (`block_number`),
    UNIQUE KEY `idx_block_hash` (`block_hash`),
    KEY `idx_block_time` (`block_time`)
) ;

DROP TABLE IF EXISTS `tbl_transaction`;
CREATE TABLE `tbl_transaction` (
    `id`             	BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`         	VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `tx_hash`           VARCHAR(66)    NOT NULL DEFAULT '' COMMENT '交易hash',
    `block_hash`        VARCHAR(66)    NOT NULL COMMENT 'hex string of block hash',
    `block_number`      BIGINT         NOT NULL COMMENT 'block number',
    `from_address`      VARCHAR(42)    NOT NULL COMMENT 'hex string of the sender account address',
    `to_address`        VARCHAR(42)    NOT NULL COMMENT 'hex string of the receiver account address',
    `contract_address`  VARCHAR(42)    NULL COMMENT '合约地址',
    `tx_value`          VARCHAR(30)    DEFAULT NULL COMMENT 'value of transaction in Gwei',
    `tx_status` 		VARCHAR(10)    NOT NULL COMMENT 'transaction status, 0: failed; 1: success; ',
    `block_time`        BIGINT         DEFAULT NULL COMMENT 'transaction timestamp',
    `nonce`             int            NOT NULL DEFAULT '0' COMMENT 'transaction nonce',
    `tx_index`          int            NOT NULL DEFAULT '0' COMMENT 'transaction position index',
    `tx_type`           tinyint(1)     DEFAULT '0' COMMENT 'transaction type, 0: normal; 1: contract creation',
    `data`              mediumtext     COMMENT 'transaction data',
    `gas_price`         BIGINT         DEFAULT NULL COMMENT 'gas price',
    `gas_limit`         BIGINT         DEFAULT NULL COMMENT 'gas limit',
    `gas_used`          BIGINT         DEFAULT NULL COMMENT 'gas used',
    UNIQUE KEY (`tx_hash`),
    KEY `idx_transaction_from` (`from_address`),
    KEY `idx_transaction_to` (`to_address`),
    KEY `idx_blknumber_txindex` (`block_number`,`tx_index`),
    KEY `idx_transaction_timestamp` (`block_time`)
);

DROP TABLE IF EXISTS `tbl_transaction_log`;
CREATE TABLE `tbl_transaction_log` (
    `id`             	BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`         	VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `tx_hash`           VARCHAR(66)    NOT NULL DEFAULT '' COMMENT '交易hash值',
    `tx_type`           tinyint(2)     DEFAULT '0' COMMENT 'transaction type, 0: normal; 1: contract creation',
    `block_time`        BIGINT         DEFAULT NULL COMMENT 'transaction timestamp',
    `block_hash`        VARCHAR(66)    NOT NULL COMMENT 'hex string of block hash',
    `block_number`      BIGINT         NOT NULL COMMENT '区块高度',
    `log_index`         INT            NOT NULL COMMENT 'log在交易里的索引',
    `tx_index`          INT            NOT NULL COMMENT '交易在区块里的索引',
    `address`           VARCHAR(42)    NOT NULL DEFAULT '' COMMENT '合约地址',
    `data`              mediumtext     COMMENT '该日志未indexed的参数',
    `type`              VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '？',
    `topics`            JSON           NOT NULL  COMMENT 'indexed 参数列表',
    KEY `idx_tx_hash`(`tx_hash`),
    KEY `idx_block_number`(`block_number`),
    KEY `idx_address`(`address`)
);

DROP TABLE IF EXISTS `tbl_account`;
CREATE TABLE `tbl_account` (
    `id`             	BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`         	VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `address`           VARCHAR(42)    NOT NULL COMMENT 'hex string of account address hash 用户地址',
    `type`              tinyint(2)     NOT NULL DEFAULT '0' COMMENT 'account type, 0: normal; 1: contract; 2: native',
    `balance`           VARCHAR(30)    NOT NULL DEFAULT '0' COMMENT 'account balance in Gwei',
    `nonce`             INT            NOT NULL DEFAULT '0' COMMENT '该地址发出的交易数量',
    `tx_count`          INT            NOT NULL DEFAULT '0' COMMENT '发出或接收的总交易数量',
    `block_time`        BIGINT         NOT NULL COMMENT 'block timestamp 账户生成时间',
    UNIQUE KEY `idx_address` (`address`),
    KEY `idx_account_balance` (`balance`)
);

insert ignore into tbl_account(address, type, balance, nonce, tx_count, block_time)
values('0x0000000000000000000000000000000000000000', 2, 0, 0, 0, 0);
insert ignore into tbl_account(address, type, balance, nonce, tx_count, block_time)
values('0xD62B67170A6bb645f1c59601FbC6766940ee12e5', 2, 0, 0, 0, 0);
insert ignore into tbl_account(address, type, balance, nonce, tx_count, block_time)
values('0xA4Bf827047a08510722B2d62e668a72FCCFa232C', 2, 0, 0, 0, 0);
update tbl_account set type = 2 where address in ('0x0000000000000000000000000000000000000000','0xD62B67170A6bb645f1c59601FbC6766940ee12e5','0xA4Bf827047a08510722B2d62e668a72FCCFa232C');
update tbl_transfer set from_type = 2 where transfer_from in ('0x0000000000000000000000000000000000000000','0xD62B67170A6bb645f1c59601FbC6766940ee12e5','0xA4Bf827047a08510722B2d62e668a72FCCFa232C');
update tbl_transfer set to_type = 2 where transfer_to in ('0x0000000000000000000000000000000000000000','0xD62B67170A6bb645f1c59601FbC6766940ee12e5','0xA4Bf827047a08510722B2d62e668a72FCCFa232C');

DROP TABLE IF EXISTS `tbl_daily_summary`;
CREATE TABLE `tbl_daily_summary` (
    `id`                   BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`               VARCHAR(20)    DEFAULT ''                                            NOT NULL,
    `summary_time`         BIGINT         NOT NULL COMMENT '当天的UTC0点时间戳',
    `block_count`          INT            NOT NULL COMMENT '当天的区块数量',
    `tx_count`             INT            NOT NULL COMMENT '当天的交易数量',
    `active_address_count` INT            NOT NULL COMMENT '当天的活跃地址数量',
    UNIQUE KEY `idx_summary_time`(`summary_time`) USING BTREE
);

DROP TABLE IF EXISTS `tbl_monthly_summary`;
CREATE TABLE `tbl_monthly_summary` (
    `id`                   BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`               VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `summary_time`         BIGINT         NOT NULL COMMENT '当月第一天的UTC0点时间戳',
    `block_count`          INT            NOT NULL COMMENT '当月的区块数量',
    `tx_count`             INT            NOT NULL COMMENT '当月的交易数量',
    `active_address_count` INT            NOT NULL COMMENT '当天的活跃地址数量',
    UNIQUE KEY `idx_summary_time`(`summary_time`) USING BTREE
);

 DROP TABLE IF EXISTS `tbl_node`;
 CREATE TABLE `tbl_node` (
     `id`                   BIGINT         AUTO_INCREMENT PRIMARY KEY,
     `create_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
     `update_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
     `status`               VARCHAR(20)    DEFAULT ''                                            NOT NULL,

     `name`                 VARCHAR(255)   NOT NULL COMMENT '节点名称',
     `region`               VARCHAR(255)   NOT NULL COMMENT '地区',
     `ip`                   VARCHAR(15)    NOT NULL COMMENT '节点的IP',
     `rest_port`            INT            NOT NULL default '8545' COMMENT '节点的restful端口',
     `version`              VARCHAR(50)    NOT NULL COMMENT '节点的版本号',
     `is_active`            BOOLEAN        NOT NULL COMMENT '是否处于活跃状态'
 );

-- INSERT INTO `tbl_node` (`name`, `region`, `ip`, `rest_port`, `version`, `is_active`)
-- VALUES ('1', '0', '127.0.0.1', '8545', '0', '0');

 DROP TABLE IF EXISTS `tbl_contract`;
 CREATE TABLE `tbl_contract` (
    `id`              BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`     DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`     DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`          VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `address`         VARCHAR(42)    NOT NULL COMMENT 'hex string of contract address hash 合约地址',
    `name`            VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约名称',
    `abi`             JSON           NOT NULL COMMENT '合约abi',

    `block_time`      BIGINT         NOT NULL COMMENT '合约上链时间',
    `creator`         VARCHAR(42)    NOT NULL COMMENT '合约创建者',
    `create_tx_hash`  VARCHAR(66)    NOT NULL COMMENT '合约创建交易哈希',
    `contract_type`   VARCHAR(10)    NOT NULL DEFAULT '' COMMENT '合约分类(ERC20, ERC721)',
    `token_name`      VARCHAR(66)    NOT NULL COMMENT 'Token名称，abi中有name方法时调用获取',
    `token_symbol`    VARCHAR(66)    NOT NULL COMMENT 'Token标识，abi中有symbol方法时调用获取',
    `decimals`        INT            NOT NULL COMMENT 'Token小数位数，abi中有decimals方法时调用获取',
     UNIQUE KEY `idx_contract_address` (`address`)
 ) ;

DROP TABLE IF EXISTS `tbl_method_map`;
CREATE TABLE `tbl_method_map` (
    `id`             	BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`         	VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `method_name`       VARCHAR(50)    NOT NULL COMMENT '方法名称',
    `method_hash`       VARCHAR(66)    NOT NULL COMMENT '方法签名hash',
    `method_id`         VARCHAR(10)    NOT NULL COMMENT '方法签名Id',
    `method_signature`  VARCHAR(255)   NOT NULL COMMENT '方法签名（只包含方法名和参数类型）',
    UNIQUE KEY `idx_method_hash` (`method_hash`),
    KEY `idx_method_id` (`method_id`)
);

DROP TABLE IF EXISTS `tbl_transfer`;
CREATE TABLE `tbl_transfer` (
    `id`             	BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`    	DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`         	VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `tx_hash`           VARCHAR(66)    NOT NULL DEFAULT '' COMMENT '交易hash',
    `block_hash`        VARCHAR(66)    NOT NULL COMMENT 'hex string of block hash',
    `block_number`      BIGINT         NOT NULL COMMENT 'block number',
    `from_address`      VARCHAR(42)    NOT NULL COMMENT 'hex string of the sender account address',
    `to_address`        VARCHAR(42)    NOT NULL COMMENT 'hex string of the receiver contract address',
    `contract_address`  VARCHAR(42)    NOT NULL COMMENT 'transfer contract address',
    `transfer_from`     VARCHAR(42)    NOT NULL COMMENT 'transfer from address',
    `transfer_to`       VARCHAR(42)    NOT NULL COMMENT 'hex string of the receiver account address',
    `from_type`         TINYINT(1)     NOT NULL COMMENT 'account type',
    `to_type`           TINYINT(1)     NOT NULL COMMENT 'account type',
    `token_id`          VARCHAR(80)    NOT NULL COMMENT 'token id for ERC721',
    `block_time`        BIGINT         DEFAULT NULL COMMENT 'transaction timestamp',
    `log_index`         INT            NOT NULL COMMENT 'log在交易里的索引',
    UNIQUE KEY `idx_tx_hash_log_index` (`tx_hash`, `log_index`),
    KEY `idx_transfer_from` (`transfer_from`),
    KEY `idx_transfer_to` (`transfer_to`),
    KEY `idx_transfer_block_number` (`block_number`)
);

DROP TABLE IF EXISTS `tbl_nft_holder`;
create table tbl_nft_holder(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '',
    `contract_address`  VARCHAR(42)   not NULL COMMENT 'hex string of 合约地址',
    `account_address` varchar(42)  not null comment 'hex string of the account address',
    `count` bigint not null default 0 comment 'account address 持有的某个 nft的数量',
    UNIQUE KEY `uk_contract_address_account_address` (`contract_address`, `account_address`),
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `tbl_table_height`;
create table tbl_table_height(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id, 非用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（默认字段）',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（默认字段）',
    `status` varchar(20) NOT NULL DEFAULT '',
    `table_name` varchar(200) not null default '' comment 'tbl_name 需要统计高度的表名称',
    `height` bigint not null default 0 comment 'table_name对应的table目前统计到的高度',
    UNIQUE KEY `uk_table_name` (`table_name`),
    PRIMARY KEY (`id`)
);