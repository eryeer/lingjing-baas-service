<h1>DNA2.0 Baas接口功能设计</h1>

# 1. 基本规范

* 通讯协议:http
* 传输格式:json

# 2. Baas服务模块功能清单

## 2.1. 用户管理

### 2.1.1. registerUser

- 功能说明：注册用户
- url: /baas/user/registerUser
- auth: 未登录状态
- 行为：用户表新增用户
- 校验：所有字段非空校验，注册验证码校验，用户手机号重复校验，（公司名称重复校验）

### 2.1.2. checkUserRegister

- 功能说明：检测注册用户手机号是否符合注册条件
- url: /baas/user/checkUserRegister
- auth: 未登录状态
- 行为：检查用户表中手机号是否已存在
- 校验：手机号格式校验

### 2.1.3. login

- 功能说明：用户登录
- url: /baas/user/login
- auth: 未登录状态
- 行为：校验手机号，密码和验证码，返回token
- 校验：手机号格式校验，用户状态校验，注册状态校验

### 2.1.4. refreshToken

- 功能说明：更新accessToken
- url: /baas/user/refreshToken
- auth: 未登录状态
- 行为：通过redis服务查询refreshToken，生成并返回新的accessToken
- 校验：refreshToken校验

### 2.1.5. logout

- 功能说明：用户登出
- url: /baas/user/logout
- auth: 所有角色（已登录）
- 行为：登出账户，删除redis中的refreshToken
- 校验：accessToken校验

### 2.1.6. getUserById

- 功能说明：获取某个用户详细信息，关联公司信息
- url: /baas/user/getUserById/{userId}
- auth: 所有角色
- 行为：个人可以获取自己的信息，PM可获取全部信息
- 校验：accessToken校验，角色校验，userId非空校验

### 2.1.7. changePassword

- 功能说明：修改用户密码
- url: /baas/user/changePassword
- auth: 所有角色
- 行为：个人可以修改自己的密码
- 校验：accessToken校验，密码强度校验

### 2.1.8. approveUser

- 功能说明：审批用户
- url: /baas/user/approveUser
- auth: PM
- 行为：PM审批用户注册申请，同意或拒绝，拒绝时审批意见必填
- 校验：accessToken校验，角色校验，字段非空校验

### 2.1.9. getUserList

- 功能说明：根据查询条件获取用户列表（查询条件： 认证状态（必填），客户编号，用户姓名，企业名称，用户手机，申请日期）
- url: /baas/user/getUserList
- auth: PM
- 行为：PM查询用户列表
- 校验：accessToken校验，角色校验，查询条件校验

## 2.2. 公共接口

### 2.2.1. sendRegisterCode

- 功能说明：发送注册短信验证码
- url: /baas/common/sendRegisterCode
- auth: 未登录状态
- 行为：发送注册短信验证码，返回发送成功或失败
- 校验：手机号格式校验，注册条件校验

### 2.2.2. sendLoginCode

- 功能说明：发送登录短信验证码
- url: /baas/common/sendLoginCode
- auth: 未登录状态
- 行为：发送登录短信验证码，返回发送成功或失败
- 校验：手机号格式校验，注册条件校验

### 2.2.3. uploadFile

- 功能说明：上传文件
- url: /baas/common/uploadFile
- auth: 已登录状态，不限角色
- 行为：上传文件到COS，返回文件uuid,原始文件名和临时url
- 校验：文件大小校验（10M），格式校验（jpg, png, pdf）,图片类型校验（jpg/png）

### 2.2.4. updateFile

- 功能说明：更新上传文件
- url: /baas/common/updateFile
- auth: 已登录状态，不限角色
- 行为：更新上传文件到COS，返回文件uuid,原始文件名和临时url
- 校验：文件大小校验（10M），文件名校验, userId校验

### 2.2.5. getTools

- 功能说明：获取开发组件列表
- url: /baas/common/getTools
- auth: 已登录状态，不限角色
- 行为：获取开发组件列表
- 校验：登录校验

## 2.3. 应用合约管理

### 2.3.1. appCreate

- 功能说明：创建合约应用
- url: /baas/contract/appCreate
- auth: 普通用户
- 行为：合约应用表新增记录
- 校验：accessToken校验，所有字段非空校验，用户链账户已创建

### 2.3.2. appRemove

- 功能说明：删除应用
- url: /baas/contract/appRemove
- auth: 普通用户
- 行为：标记删除用户应用
- 校验：accessToken校验，应用名称非空

### 2.3.3. getAppList

- 功能说明：根据用户id获取应用列表
- url: /baas/contract/getAppList
- auth: 普通用户
- 行为：根据用户id获取应用列表
- 校验：accessToken校验，userId非空校验

### 2.3.4. getApp

- 功能说明：根据用户id和应用名称获取应用详情
- url: /baas/contract/getApp
- auth: 普通用户
- 行为：根据用户id和应用名称获取应用详情
- 校验：accessToken校验，对象非空校验

### 2.3.5. getContractTemplates

- 功能说明：获取合约模板列表
- url: /baas/contract/getContractTemplates
- auth: 普通用户
- 行为：获取合约模板列表
- 校验：accessToken校验

### 2.3.6. deploy

- 功能说明：合约部署
- url: /baas/contract/deploy
- auth: 普通用户
- 行为：使用当前用户账户，合约字节码和参数部署合约，更新链账户余额
- 校验：accessToken校验，根据模板类型校验不同的参数

### 2.3.7. updateFileList

- 功能说明：更新合约文件列表
- url: /baas/contract/updateFileList
- auth: 普通用户
- 行为：更新合约文件列表
- 校验：accessToken校验，根据模板类型校验不同的参数

## 2.4. 链账户管理

### 2.4.1. accountCreate

- 功能说明：创建链账户
- url: /baas/chain/accountCreate
- auth: 普通用户
- 行为：链账户表新增记录
- 校验：accessToken校验，钱包密码非空校验，用户链账户未创建

### 2.4.2. getChainAccount

- 功能说明：根据用户id获取链账户
- url: /baas/chain/getChainAccount
- auth: 普通用户
- 行为：根据用户id获取链账户
- 校验：accessToken校验，userId非空校验

### 2.4.3. applyGas

- 功能说明：申领燃料
- url: /baas/chain/applyGas
- auth: 普通用户
- 行为：申领燃料
- 校验：accessToken校验，申领时间校验

### 2.4.4. getApplyList

- 功能说明：获取燃料申领记录
- url: /baas/chain/getApplyList
- auth: 普通用户
- 行为：根据用户id获取燃料申领记录
- 校验：accessToken校验

## 2.5. 跨链管理

### 2.5.1. sendCrossChain

- 功能说明：发送跨链交易
- url: /cross/chain/sendCrossChain
- auth: 普通用户
- 行为: 构造调用跨链合约额的交易并发送到DNA2.0
- 校验：accessToken校验，用户链账户存在和余额校验

### 2.5.2. getCrossChainList

- 功能说明：获取跨链记录列表
- url: /cross/chain/getCrossChainList
- auth: 普通用户
- 行为: 分页获取跨链记录列表
- 校验：accessToken校验

### 2.5.3. getCrossChain

- 功能说明：获取跨链记录详情
- url: /cross/chain/getCrossChain
- auth: 普通用户
- 行为: 根据交易哈希获取跨链记录详情
- 校验：accessToken校验

### 2.5.4. getCrossTbdList

- 功能说明：获取待跨链记录列表
- url: /cross/chain/getCrossTbdList
- auth: 普通用户
- 行为: 根据源联合约地址和用户id获取待跨链记录列表
- 校验：accessToken校验

### 2.5.5. setCrossDst

- 功能说明：历史数据跨链同步
- url: /cross/chain/setCrossDst
- auth: 普通用户
- 行为: 历史数据跨链同步
- 校验：accessToken校验

## 2.6. 文件存储管理

### 2.6.1. uploadPdfs

- 功能说明：上传文件
- url: /baas/pdfs/uploadPdfs
- auth: 普通用户
- 行为: 上传文件到PDFS分布式存储系统
- 校验：accessToken校验

### 2.6.2. getPdfsList

- 功能说明：获取PDFS文件列表
- url: /baas/pdfs/getPdfsList
- auth: 普通用户
- 行为: 根据用户Id获取文件列表
- 校验：accessToken校验，分页按上传时间倒序

### 2.6.3. downloadPdfs

- 功能说明：文件下载
- url: /baas/pdfs/downloadPdfs
- auth: 普通用户
- 行为: 根据文件哈希下载文件
- 校验：accessToken校验

### 2.6.4. sharePdfs

- 功能说明：共享PDFS文件
- url: /baas/pdfs/sharePdfs
- auth: 普通用户
- 行为: 共享用户文件
- 校验：accessToken校验，验证用户文件权限

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

### 2.3.1. getAddressList

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

# 4. 接口参数设计参见swagger

# 5. 字典代码

## 5.1. Baas Service模块

### 5.1.1 用户角色

字段名称： role

|代码|说明| 
|--|--| 
|CU|普通用户| 
|PM|平台管理员|

### 5.1.2. 用户状态

字段名称： status

|代码|说明| 
|--|--| 
|1|已启用| 
|0|已删除| 
|2|已停用|

### 5.1.3. 文件类型

字段名称： file_type

|代码|说明| 
|--|--| 
|IDA|身份证正面| 
|IDB|身份证反面| 
|BL|营业执照正本| 
|BLC|营业执照副本| 
|SOL|智能合约文件|

### 5.1.4. 用户认证状态

字段名称： approve_status

|代码|说明| 
|--|--| 
|Pending|待认证| 
|Approved|认证通过| 
|Rejected|已拒绝|

### 5.1.5. 合约模板类型

字段名称： template_type

|代码|说明| 
|--|--| 
|PROOF|存证合约| 
|VOTE|投票合约| 
|CUSTOM|自定义|

### 5.1.6. 部署状态

字段名称： contact_status

|代码|说明| 
|--|--| 
|0|未部署| 
|1|已部署|

### 5.1.7. 跨链目标链名称

字段名称： dst_chain

|代码|说明| 
|--|--| 
|BSC_TESTNET|BSC测试网| 
|POLY_TESTNET|POLY测试网| 
|FABRIC|Fabric|
|KOVAN_TESTNET|以太坊Kovan测试网| 
|DNA_V_ONE|DNA 1.0|
|RNA|RNA| 

### 5.1.8. 跨链状态

字段名称： status

|代码|说明| 
|--|--| 
|crossing|跨链中| 
|done|跨链完成|

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

## 7.1 用户管理模块

## 7.1.1 clearTempFiles

- 功能说明：临时文件清理
- 执行时间或周期: 每天
- 行为：每天清理更新时间超过14天的临时文件

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
