
-- v7 第七次迭代
USE dna2_explorer;
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

    KEY `idx_from` (`from_address`),
    KEY `idx_to` (`to_address`),
    KEY `idx_tx_hash` (`tx_hash`),
    PRIMARY KEY (`id`)
) COMMENT ='内部交易记录表' charset = utf8mb4 collate utf8mb4_general_ci;