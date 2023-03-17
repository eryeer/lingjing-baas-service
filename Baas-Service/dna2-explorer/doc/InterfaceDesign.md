<h1>DNA2.0 浏览器接口功能设计</h1>

# 1. 基本规范

* 通讯协议:http
* 传输格式:json

# 3. 区块链浏览器接口

## 3.1. Block

### 3.1.1. getBlockList

- 功能说明：获取区块列表，按区块高度倒序
- url: /explorer/block/getBlockList
- auth: 已登录，任意角色
- 行为：分页查询区块列表
- 校验：无

### 3.1.2. getBlock

- 功能说明：Get block detail by height
- url: /explorer/block/getBlock
- auth: 已登录，任意角色
- 行为：查询区块详情
- 校验：无

## 3.2. Transaction

### 3.2.1. getTransactionList

- 功能说明：获取交易列表
- url: /explorer/transaction/getTransactionList
- auth: 已登录，任意角色
- 行为：分页查询交易列表，按区块高度倒排，再按交易序号排序。可选参数：区块高度
- 校验：无

### 3.2.2. getTransaction

- 功能说明：获取交易详情
- url: /explorer/transaction/getTransaction
- auth: 已登录，任意角色
- 行为：根据交易哈希获取交易详细信息，包括日志记录
- 校验：无

## 3.3. Address

### 3.3.1. getAddressList

- 功能说明：获取地址列表
- url: /explorer/address/getAddressList
- auth: 已登录，任意角色
- 行为：分页查询地址列表，按交易数量倒排
- 校验：无

### 3.3.2. getAddress

- 功能说明：获取地址详情
- url: /explorer/address/getAddress
- auth: 已登录，任意角色
- 行为：获取地址详情
- 校验：无

### 3.3.3. getTransactionListByAddress

- 功能说明：根据地址获取交易列表
- url: /explorer/address/getTransactionListByAddress
- auth: 已登录，任意角色
- 行为：根据发送或接收地址分页查询交易列表，按区块高度倒排，再按交易序号排序。必填参数：地址
- 校验：无

### 3.3.4. getInternalTxListByAddress

- 功能说明：根据地址获取内部交易列表
- url: /explorer/address/getInternalTxListByAddress
- auth: 已登录，任意角色
- 行为：根据发送或接收地址分页查询内部交易列表，按id倒排。必填参数：地址
- 校验：无

## 3.4. Summary

### 3.4.1. getTotalSummary

- 功能说明：获取统计汇总信息
- url: /explorer/summary/getTotalSummary
- auth: 已登录，任意角色
- 行为：获取统计汇总信息，包括区块高度，交易数量，节点数和状态
- 校验：无

### 3.4.2. getAddressSummary

- 功能说明：获取活跃地址统计列表
- url: /explorer/summary/getAddressSummary
- auth: 已登录，任意角色
- 行为：获取活跃地址统计列表
- 校验：无

### 3.4.3. getBlockSummary

- 功能说明：获取每日新增区块数量统计列表
- url: /explorer/summary/getBlockSummary
- auth: 已登录，任意角色
- 行为：获取每日新增区块数量统计列表
- 校验：无

### 3.4.4. getTransactionSummary

- 功能说明：根据参数获取每日/每月交易数量统计列表
- url: /explorer/summary/getTransactionSummary
- auth: 已登录，任意角色
- 行为：根据参数获取每日/每月交易数量统计列表
- 校验：无

## 3.5. Contract

### 3.5.1. uploadAbi

- 功能说明：上传合约ABI文件
- url: /explorer/contract/uploadAbi
- auth: 已登录，任意角色
- 行为：上传合约ABI文件
- 校验：authkey 校验

### 3.5.2 getContractByCreatorAddress

- 功能说明：查询给出的链账户表列中账户部署的合约信息
- url: /explorer/contract/getContractByCreatorAddress
- auth: 已登录，任意角色
- 行为：查询给出的链账户表列中账户部署的合约信息，可分页查询
- 校验：authkey 校验

## 3.6 External-Api

### 3.6.1 getTokenHolder

- 功能说明：获取token的持有信息
- url: /external/token/getTokenHolder
- auth: 任意角色
- 行为：根据 合约地址 获取 token的持有信息

# 4. 接口参数设计参见swagger

# 5. 字典代码

## 5.2. 区块链浏览器

### 5.2.1. 地址类型

字段名称： type

|代码|说明| 
|--|--| 
|0|账户地址| 
|1|合约地址|

### 5.2.2. 交易状态

字段名称： tx_status

|代码|说明| 
|--|--| 
|0x0|失败| 
|0x1|成功|

### 5.2.3. 交易类型

字段名称： tx_type

|代码|说明| 
|--|--| 
|0|一般类型| 
|1|合约部署|

# 7. 系统自动任务

## 7.2 区块链浏览器模块

## 7.2.1 syncBlock

- 功能说明：同步区块交易等区块链信息
- 执行时间或周期: 每隔30秒
- 行为：每隔30秒同步直到最新区块高度

## 7.2.2 syncNode

- 功能说明：同步节点状态
- 执行时间或周期: 每隔60秒
- 行为：每隔60秒同步最新节点状态

## 7.2.3 updateDailySummary

- 功能说明：更新每日统计信息
- 执行时间或周期: 每隔60秒
- 行为：更新每日统计信息

## 7.2.4 updateMonthlySummary

- 功能说明：更新每月统计信息
- 执行时间或周期: 每隔60秒
- 行为：更新每月统计信息

## 7.2.5 countNFTHolder

- 功能说明：更新nft的每个用户的持有信息
- 执行时间或周期: 每隔60秒
- 行为：更新nft的每个用户的持有信息