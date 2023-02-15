<h1>Lingjing Baas接口功能设计</h1>

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
- 校验：所有字段非空校验，注册验证码校验，用户手机号重复校验，密码格式校验

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
- 行为：个人可以获取自己的信息，平台管理员可获取全部信息
- 校验：accessToken校验，角色校验，userId非空校验

### 2.1.7. changePassword

- 功能说明：修改用户密码
- url: /baas/user/changePassword
- auth: 所有角色
- 行为：个人可以修改自己的密码
- 校验：accessToken校验，密码强度校验

### 2.1.8. changePhoneNumber

- 功能说明：修改用户手机号
- url: /baas/user/changePhoneNumber
- auth: 所有角色
- 行为：个人可以修改自己的手机号，修改后登出系统
- 校验：accessToken校验，验证码校验

### 2.1.9. resetPassword

- 功能说明：重置用户已忘记的密码
- url: /baas/user/resetPassword
- auth: 未登录状态
- 行为：个人可以通过手机号验证来修改自己的密码
- 校验：手机号校验，验证码校验，密码强度校验

### 2.1.10. submitUserKyc

- 功能说明：提交用户kyc信息
- url: /baas/user/submitUserKyc
- auth: 所有角色
- 行为：提交用户KYC信息
- 校验：统一社会信用码格式，身份证号格式，字段非空校验

### 2.1.11. approveUserKyc

- 功能说明：审批用户kyc信息（包括新增和变更）
- url: /baas/user/approveUserKyc
- auth: PM
- 行为：PM审批用户注册申请，同意或拒绝，拒绝时审批意见必填
- 校验：accessToken校验，角色校验，字段非空校验

### 2.1.12. getUserList

- 功能说明：根据查询条件查询用户列表（查询条件： 认证状态（必填），认证类型，手机号，用户姓名，企业名称，身份证号，统一社会信用代码，申请日期，认证时间）
- url: /baas/user/getUserList
- auth: PM
- 行为：PM查询已核验用户列表
- 校验：accessToken校验，角色校验，查询条件校验

### 2.1.13. getUserKycRecordList

- 功能说明：根据查询条件获取用户核验记录列表（查询条件： 认证状态，认证类型，核验类型，手机号，用户姓名，企业名称，身份证号，统一社会信用代码，申请日期（认证时间））
- url: /baas/user/getUserKycRecordList
- auth: PM
- 行为：PM查询用户核验记录列表
- 校验：accessToken校验，角色校验，查询条件校验

### 2.1.14 submitUserKycUpdate

- 功能说明：提交用户kyc变更信息
- url: /baas/user/submitUserKycUpdate
- auth: 普通用户
- 行为：提交用户KYC变更信息
- 校验：统一社会信用码格式，身份证号格式，字段非空校验

### 2.1.15. getKycUpdateById

- 功能说明：获取某个用户KYC变更信息
- url: /baas/user/getKycUpdateById/{userId}
- auth: 所有用户
- 行为：个人可以获取自己的信息，平台管理员可获取全部信息
- 校验：accessToken校验，角色校验，userId非空校验

### 2.1.16. markKycNotify

- 功能说明：标记KYC变更反馈信息不再提示
- url: /baas/user/markKycNotify
- auth: 普通用户
- 行为：标记KYC变更反馈信息为不再提示
- 校验：accessToken校验，角色校验

### 2.1.17. getKycUpdateList

- 功能说明：根据查询条件查询用户KYC变更列表（查询条件： 认证状态（必填），认证类型，手机号，用户姓名，企业名称，身份证号，统一社会信用代码，申请日期，认证时间）
- url: /baas/user/getKycUpdateList
- auth: PM
- 行为：PM查询用户KYC列表
- 校验：accessToken校验，角色校验，查询条件校验

## 2.2. 链账户管理

### 2.2.1. accountCreate

- 功能说明：创建链账户（关联链账户也适用）
- url: /baas/chain/accountCreate
- auth: 企业用户/个人用户
- 行为：链账户表新增记录，保存链账户名称和地址
- 校验：accessToken校验，校验签名

### 2.2.2. privateKeyCustody

- 功能说明：托管链账户私钥
- url: /baas/chain/privateKeyCustody
- auth: 企业用户/个人用户
- 行为：加密保存用户的私钥
- 校验：accessToken校验，链账户地址和私钥需要能对应

### 2.2.3. downloadPrivateKey

- 功能说明：下载链账户私钥
- url: /baas/chain/downloadPrivateKey
- auth: 企业用户/个人用户
- 行为：根据链账户ID下载私钥明文
- 校验：accessToken校验，校验地址是否存在，是否已托管

### 2.2.4. getChainAccount

- 功能说明：根据用户id获取链账户
- url: /baas/chain/getChainAccount
- auth: 所有角色
- 行为：根据用户id获取链账户，支持账户名称、地址、托管状态、燃料可转账状态、创建时间的筛选
- 校验：accessToken校验，userId非空校验

### 2.2.5. deleteChainAccount

- 功能说明：根据地址批量删除链账户
- url: /baas/chain/deleteChainAccount
- auth: 企业用户/个人用户
- 行为：批量删除链账户，同时关闭燃料可转账状态
- 校验：accessToken校验，校验地址是否存在

### 2.2.6. changeGasTransferStatus

- 功能说明：批量开启/关闭燃料转账
- url: /baas/chain/changeGasTransferStatus
- auth: 企业用户/个人用户
- 行为：链上调用批量开启/关闭燃料转账，同时结果同步数据库
- 校验：accessToken校验，校验地址列表是否存在

## 2.3. 燃料管理

### 2.3.1. createGasContract

- 功能说明：创建燃料签约合同
- url: /baas/gas/createGasContract
- auth: 企业用户/个人用户
- 行为：燃料表新增记录，上传合同PDF到COS服务，生成流水号，保存合同文件、签约燃料数量等数据
- 校验：accessToken校验

### 2.3.2. getGasContractHistoryList

- 功能说明：获取燃料签约记录列表
- url: /baas/gas/getGasContractList
- auth: 企业用户/个人用户
- 行为：获取其主体的燃料签约历史记录，支持分页查询，携带包含详情维度的信息量（燃料记录详情不再做单独接口）
- 校验：accessToken校验

### 2.3.3. getGasSummary

- 功能说明：获取燃料统计
- url: /baas/gas/getGasSummary
- auth: 企业用户/个人用户
- 行为：返回已签约燃料总量、已申领燃料总量、剩余可申领燃料总量，根据用户id获取链账户信息列表，限展示10个地址，按已申领燃料数量倒排
- 校验：accessToken校验

### 2.3.4. getAdminGasContractList

- 功能说明：获取PM视角燃料签约记录列表
- url: /baas/gas/getAdminGasContractList
- auth: PM
- 行为：可获取全部公司的燃料签约历史记录，支持分页查询，携带包含详情维度的信息量（燃料记录详情不再做单独接口）
- 校验：accessToken校验

### 2.3.5. approveGasContract

- 功能说明：PM审批燃料签约合同
- url: /baas/gas/approveGasContract
- auth: PM
- 行为：审批燃料签约合同，支持修改燃料数额，以及填写核验反馈，核验反馈在驳回的时候为必填项
- 校验：accessToken校验

### 2.3.6. getGasContactStatisticList

- 功能说明：PM获取燃料信息库中签约信息统计列表
- url: /baas/gas/getGasContactStatisticList
- auth: PM
- 行为：可获取全部公司的燃料签约统计信息列表，支持分页查询
- 校验：accessToken校验

### 2.3.7. getChainAccountListForGasManagement

- 功能说明：获取燃料申领页的链账户列表
- url: /baas/gas/getChainAccountListForGasManagement
- auth: 企业用户/个人用户
- 行为：可根据用户id获取链账户信息列表，支持分页查询
- 校验：accessToken校验

### 2.3.8. acquireGas

- 功能说明：申领燃料
- url: /baas/gas/acquireGas
- auth: 企业用户/个人用户
- 行为：申领燃料，燃料申领数量不低于可申领下限，且不高于可申领燃料额度，触发合约燃料转账
- 校验：accessToken校验

### 2.3.9. getGasClaimHistory

- 功能说明：获取申领燃料记录
- url: /baas/gas/getGasClaimHistory
- auth: 所有
- 行为：用户查询自己的链账户申领记录，PM可查询所有的链账户申领记录，支持分页查询
- 校验：accessToken校验

### 2.3.10. getGasClaimSummary

- 功能说明：获取申领燃料统计列表
- url: /baas/gas/getGasClaimSummary
- auth: PM
- 行为：PM可获取全部公司的链账户申领记录列表，支持分页查询
- 校验：accessToken校验

## 2.4. Dashboard总览

### 2.4.1. getDashboardSummary

- 功能说明：获取Dashboard总览数据
- url: /baas/dashboard/getDashboardSummary
- auth: 企业用户/个人用户
- 行为：通过查询baas数据库和区块链浏览器数据接口，获取总览的公共统计数据和个人账户统计数据
- 校验：accessToken校验

## 2.5. 公共接口

### 2.5.1. sendVerifyCode

- 功能说明：发送注册短信验证码
- url: /baas/common/sendVerifyCode
- auth: 未登录状态
- 行为：通过参数指定发送的是注册、登录、修改手机号、忘记密码的短信验证码，返回发送成功或失败
- 校验：手机号格式校验，注册条件校验

### 2.5.2. uploadFile

- 功能说明：上传文件
- url: /baas/common/uploadFile
- auth: 已登录状态，不限角色
- 行为：上传文件到COS，返回文件uuid,原始文件名和临时url
- 校验：文件大小校验（7M），格式校验（jpg, jpeg, bmp, png，pdf）,图片类型校验（jpg, jpeg, bmp, png）

### 2.5.3. updateFile

- 功能说明：更新上传文件
- url: /baas/common/updateFile
- auth: 已登录状态，不限角色
- 行为：更新上传文件到COS，返回文件uuid,原始文件名和临时url
- 校验：文件大小校验（10M），文件名校验, userId校验

## 2.6. 合约相关接口

### 2.6.1. getContractByChainAccountAddress

- 功能说明: 获取改用户下的所有链账户的部署合约的信息
- url: /baas/contract/getContractByChainAccountAddress
- auth: 企业用户/个人用户
- 行为: 通过查询用户id信息，获取链上该用户关联的链用户部署的合约的信息，可分页查询
- 校验：accessToken校验

# 3. 接口参数设计参见swagger

# 4. 字典代码

## 4.1. Baas Service模块

### 4.1.1. 用户角色

字段名称： role

|代码|说明| 
|--|--| 
|CU|普通用户| 
|PM|平台管理员|

### 4.1.2. 用户认证类型

字段名称： user_type

|代码|说明| 
|--|--| 
|PC|个人认证| 
|EC|企业认证| 

### 4.1.3. 文件类型

字段名称： file_type

|代码|说明| 
|--|--| 
|IDA|身份证正面| 
|IDB|身份证反面| 
|BL|营业执照| 
|GC|燃料签约合同|

### 4.1.4. 用户认证状态

字段名称： approve_status

|代码|说明| 
|--|--| 
|Pending|待认证| 
|Approved|认证通过| 
|Rejected|已拒绝|
|ToBeUpdated|需修改再提交|

### 4.1.5. 短信验证码类型

字段名称： code_type

|代码|说明| 
|--|--| 
|R|注册验证码| 
|L|登录验证码| 
|CP|修改手机号验证码|
|RS|忘记密码验证码|

### 4.1.6. 核验类型

字段名称： kyc_type

|代码|说明| 
|--|--| 
|NEW|新用户认证| 
|UPDATE|认证信息变更| 

# 5. 系统自动任务

## 5.1. 用户管理模块

## 5.2. clearTempFiles

- 功能说明：临时文件清理
- 执行时间或周期: 每天
- 行为：每天清理更新时间超过14天的临时文件


