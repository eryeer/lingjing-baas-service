# 1. 搭建节点

## 1.1. 安装环境

Ubuntu 16 或 18
CentOS 7
MacOS
Golang 版本 1.14 及以上，同时需要 C 编译器

## 1.2. 获取源代码

```shell
$ git clone https://github.com/DNAProject/Zion
```

## 1.3. 编译源代码

### 1.3.1. 编译 geth

```shell
$ make geth
```

### 1.3.2. 全部编译

```shell
$ make all
```

编译后可在 build/bin 目录中生成二进制可执行文件。

## 1.4. 环境搭建

环境搭建以单机搭建 4 节点私链网络为例。

1. 生成节点初始配置信息

利用 genesisTool 生成节点初始化配置文件。

```shell
./geth genesisTool generate --basePath <outputPath> --nodeCount <nodeCount> --nodePass <nodePass>
```

以下是生成 4 节点配置文件示例：

```shell
./geth genesisTool generate --basePath ./genesisOutput --nodeCount 4 --nodePass 1234
```

生成三份文件：
genesis.json

```js
{
	"config": {
		"chainId": 10898,
		"homesteadBlock": 0,
		"eip150Block": 0,
		"eip155Block": 0,
		"eip158Block": 0,
		"byzantiumBlock": 0,
		"constantinopleBlock": 0,
		"petersburgBlock": 0,
		"istanbulBlock": 0,
		"hotstuff": {
			"protocol": "basic"
		}
	},
	"alloc": {
		"0x49525E980345C81498fE0e30a9ACC7f4dC9E237B": {
			"publicKey": "0x0213db218e3638d64ae0cb440482c5cfda460ad02759c51a0b53a42f4954e40137",
			"balance": "100000000000000000000000000000"
		},
		"0xA29cfe2827fFf2d38e300be374c8a89214fa5C95": {
			"publicKey": "0x033b2d6b8db288cffe1b10de45e3c920942b069dc6db2a4110a63194fa147352f9",
			"balance": "100000000000000000000000000000"
		},
		"0xAD048c8a4Fc1002B8414F23e4a0105799e9A232D": {
			"publicKey": "0x037595cdec137c1c81fffc70a9bdd77af3add53e91e28cce4675e856de79128cf6",
			"balance": "100000000000000000000000000000"
		},
		"0xeffc0210C58fFE4c523309F0e0918b89911C0985": {
			"publicKey": "0x023bfcaab2a46272bbc5fa5a1fe8c8e19af32120e6299ad7006b6038dd892510ed",
			"balance": "100000000000000000000000000000"
		}
	},
	"coinbase": "0x0000000000000000000000000000000000000000",
	"difficulty": "0x1",
	"extraData": "0x0000000000000000000000000000000000000000000000000000000000000000f89bf8549449525e980345c81498fe0e30a9acc7f4dc9e237b94a29cfe2827fff2d38e300be374c8a89214fa5c9594ad048c8a4fc1002b8414f23e4a0105799e9a232d94effc0210c58ffe4c523309f0e0918b89911c0985b8410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000c080",
	"gasLimit": "0xffffffff",
	"nonce": "0x4510809143055965",
	"mixhash": "0x0000000000000000000000000000000000000000000000000000000000000000",
	"parentHash": "0x0000000000000000000000000000000000000000000000000000000000000000",
	"timestamp": "0x00"
}
```

nodes.json 节点的私钥、公钥、地址、静态通讯地址

```shell
[
	{
		"address": "0x1D3a781db87D57a2091f968734186c8C72353116",
		"nodeKey": "0x1ca9dcf44053a4241b2b8350b08dcade1b52ecba340237be69004caf76d6ab43",
		"keystore": {
			"address": "1d3a781db87d57a2091f968734186c8c72353116",
			"crypto": {
				"cipher": "aes-128-ctr",
				"ciphertext": "2d5c5cc5ff6c7e2f2e49cb53859df5457fbada13b179f41e160bb6d80897d87f",
				"cipherparams": {
					"iv": "978375653c56f3255763bd509336782b"
				},
				"kdf": "scrypt",
				"kdfparams": {
					"dklen": 32,
					"n": 262144,
					"p": 1,
					"r": 8,
					"salt": "de8296b9cdb58b1f220841c1c84bbc2452a87fbce2d4d7c9cfbaf16e5501dee2"
				},
				"mac": "40cecc8a51db200f9afde4b85d7715951be0295723d148dfaa46ca7da20b0643"
			},
			"id": "93415801-0d57-4d62-8329-f82ebae37939",
			"version": 3
		},
		"pubKey": "0x02b68faf75dd7ec8f2e9a3a23354795069c77e7955ee6b2bdb42e8858d06968a2a",
		"static": "enode://b68faf75dd7ec8f2e9a3a23354795069c77e7955ee6b2bdb42e8858d06968a2a4bfe1073f3a74e3b61b3efa86bb25d655b664a2c106c5fbc07b4455039f84742@127.0.0.1:30300?discport=0"
	},
    ........
```

static-nodes.json 静态节点通讯配置

```shell
[
	"enode://f460929ebaf0ec94f872246c653a5e47c137ca9c8bddb2c872c6cd96d209311ec2380242061e0f5cc6015d137a51430877167c4442099d86a608d2af8b857004@127.0.0.1:30300?discport=0",
	"enode://93a5568672fa325a1c36b0a73c974489c36d6658f71bf56da7b3aa6cc46a3397688a6ae289f7cde8c585c6368aa5a92e0eeedc62888f9cb16c274681e1f040c2@127.0.0.1:30300?discport=0",
	"enode://fdacbff85c9544af0c4dd072d5c570e4854fd9ee7d1677384a1bd6e2d13b245491109e1c2a50b3625fa5ea59dd1682ad7a67f6a340fce3d896f270d92bd1778a@127.0.0.1:30300?discport=0",
	"enode://999ae3f263795e025fb89f96a177287fe620e0509c0c511f2c0c144bbd77b5c52c43bd681d888f1a39a295b7d655e142eac4456c3c1bfcb72b6a602a200047e6@127.0.0.1:30300?discport=0"
]
```

2. 拷贝安装辅助文件目录到安装文件夹

点击获取辅助文件。

```shell
$ cd zion/docs/install_guide/install_file
$ ls
init.sh setup start.sh stop.sh
$ cp -r setup /your/install/folder/.
$ cp *.sh /your/install/folder/.
​
$ cd zion/build/bin
$ cp geth /your/install/folder/.
​
$ cd /path/to/genesisOutput
$ ls genesis.json nodes.json static-nodes.json
$ cp *.json /your/instal/folder/setup/.
```

3. 将 setup/node i 的目录中的 nodekey 和 pubkey 改成 setup/nodes.json 对应的 key
   将 setup/node i 的目录中的 nodekey 的内容改为 setup/nodes.json 对应编号的 keystore 内容。例如：

```shell
$ cat node1/nodekey
{
    "address": "1d3a781db87d57a2091f968734186c8c72353116",
    "crypto": {
        "cipher": "aes-128-ctr",
        "ciphertext": "2d5c5cc5ff6c7e2f2e49cb53859df5457fbada13b179f41e160bb6d80897d87f",
        "cipherparams": {
            "iv": "978375653c56f3255763bd509336782b"
        },
        "kdf": "scrypt",
        "kdfparams": {
            "dklen": 32,
            "n": 262144,
            "p": 1,
            "r": 8,
            "salt": "de8296b9cdb58b1f220841c1c84bbc2452a87fbce2d4d7c9cfbaf16e5501dee2"
        },
        "mac": "40cecc8a51db200f9afde4b85d7715951be0295723d148dfaa46ca7da20b0643"
    },
    "id": "93415801-0d57-4d62-8329-f82ebae37939",
    "version": 3
}
```

4. 修改 setup/static-nodes.json 的 ip 和端口
   将各节点的 IP 和端口改为节点的所在机器 IP 和要使用的端口。
5. 修改 start.sh
   顺序修改 start.sh 中的 coinbase 为 setup/nodes.json 中的各节点 address
6. 执行 init.sh，初始化各个节点
   按顺序执行 init.sh, 在 console 的交互中输入 0-n 的节点号序号和密码（密码为 genesisTool 生成文件时的 nodePass 参数）。
7. 启动节点
   按顺序执行 start.sh, 在 console 的交互中输入 0-n 的节点号序号和密码（密码为 genesisTool 生成文件时的 nodePass 参数）。
8. 停止节点
   按顺序执行 stop.sh, 在 console 的交互中输入 0-n 的节点号序号，或者使用 killall geth 指令停止当前机器上所有节点。

# 2. 浏览器搭建

## 2.1. 安装环境

Ubuntu 16 或 18
CentOS 7
MacOS
jdk 版本 1.8.0_251

## 2.2. 中间件

redis 4 以上
mysql 5.7

## 2.3. 后端搭建

项目地址： https://e.gitee.com/onchain_1/repos/onchain_1/lingjing-baas-service/sources

// 安装项目
$ git clone https://gitee.com/onchain_1/lingjing-baas-service.git Lingjing-Baas-Service

// 编译(使用推荐使用 gradle 6.9 版本)
$ gradle build

// copy
$ cp Baas-Service/dna2-explorer/build/libs/dna2-explorer.jar /your/install/folder

// 修改启动参数配置
$ cd /your/install/folder
$ vi start.sh

```shell
#!/bin/bash

JDBC_URL="jdbc:mysql://127.0.0.1:3306/dna2_explorer?characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true&rewriteBatchedStatements=true"
nohup java -jar -DENV=dev \
-DPORT=8702 \
-DMYSQL_URL=${JDBC_URL} \
-DMYSQL_USERNAME=root \
-DMYSQL_PASSWORD=xxx \
-DREDIS_HOST=127.0.0.1 \
-DREDIS_PASSWORD=password \
-DREDIS_PORT=6379 \
-DNODE_URL=http://127.0.0.1:8545 \
dna2-explorer.jar > Explorer.log 2>&1  &

```

// 启动，后台开始同步区块到数据库
$ ./start.sh

## 2.4. 前端搭建

https://e.gitee.com/onchain_1/repos/onchain_1/lingjing-baas-web/sources

// 安装项目
$ git clone https://gitee.com/onchain_1/lingjing-baas-web.git Lingjing-Baas-Web

// 安装依赖
$ yarn install

// cd 到目录，比如管理端
$ cd Lingjing-Baas-Web/manager

// 运行
$ yarn serve

// 打包
$ yarn build

# 3. 共识节点和账户治理

## 背景

酒泉链使用原生合约来提供链上管理的功能，主要包含节点管理合约和账户管理合约。

## 节点管理合约

节点管理合约接口如下：

```js
interface INodeManager {

    // 发起提案
    function propose(uint64 startHeight, bytes memory peers) external returns (bool);
    // 投票提案
    function vote(uint64 epochID, bytes memory epochHash) external returns (bool);
    // 获取提案列表
    function getEpochListJson(uint64 epochID) external view returns (string memory);
    // 获取当前共识节点列表
    function getCurrentEpochJson() external view returns (string memory);
    // 获取投票通过的待生效提案
    function getChangingEpochJson() external view returns (string memory);

    event Proposed(bytes epoch);
    event Voted(uint64 epochID, bytes epochHash, uint64 votedNumber, uint64 groupSize);
    event EpochChanged(bytes epoch, bytes nextEpoch);
    event ConsensusSigned(string method, bytes input, address signer, uint64 size);
}

```

修改共识节点列表流程，示例依赖 hardhat 框架：

### 1.发起提案

```js
// 获取原生合约对象
var contract = await hre.ethers.getContractAt(
  "INodeManager",
  "0xA4Bf827047a08510722B2d62e668a72FCCFa232C"
);

// 共识节点列表（提案）, 新提案中共识节点修改不能超过1/3
const peersObj = [
  [
    [
      // 节点公钥
      ethers.utils.hexlify(
        ethers.utils.toUtf8Bytes(
          "0x0361c6591a660424c1a0ed727dcc4190b45c593146a768503ef96d80a489522371"
        )
      ),
      // 节点地址
      "0x2d3913c12aca0e4a2278f829fb78a682123c0125",
    ],
    [
      "0x3078303262356338366638383139636635313932363437393432653237396435333665316663353363333033316433636439646565636636383266366364666239646233",
      "0x45d53a40ea246bb8ecb1417a7f3ce8bf5dccc6e3",
    ],
    ...
  ],
];
const peersStr = ethers.utils.RLP.encode(peersObj);

// 新提案生效区块高度
const height = 8500;
// 发起提案，必须由共识节点发起
const tx = await contract.propose(height, peersStr);
console.log(tx.hash);
const receipt = await tx.wait();
console.log(receipt);
// 获取提案列表
console.log(
    "getEpochListJson: ",
    (await contract.getEpochListJson(epochID)).toString()
  );
```

### 2. 投票提案

```js
// 获取当前epochID
console.log(
  "getCurrentEpochJson: ",
  (await contract.getCurrentEpochJson()).toString()
);
const newEpochID = epochID + 1;

// 获取提案列表
console.log(
  "getEpochListJson: ",
  (await contract.getEpochListJson(newEpochID)).toString()
);
// 选择认可的提案hash
const epochHash = "xxx";
// 共识节点投票
const txVote = await contract.vote(newEpochID, epochHash);
console.log(txVote.hash);
const receiptVote = await txVote.wait();
console.log(receiptVote);
```

### 3. 投票超过共识节点 2/3

```js
// 获取待生效提案，投票通过提案会进入待生效状态，同一 epochID 其他提案停止投票
console.log(
  "getChangingEpoch: ",
  (await contract.getChangingEpoch()).toString()
);
```

## 账户管理合约

账户管理合约接口如下：

```js
interface IMaasConfig {
    // 设置 owner 方法，只有 owner 才能调用，没有 owner 时任意账户可以调用
    function changeOwner(address addr) external returns (bool);
    // 获取 owner
    function getOwner() external view returns (address);

    // 将 addr 加入或移除黑名单，只有管理员能调用，可以将非管理员地址设置为黑名单，地址可以是用户账户或合约地址，用户账户设置黑名单后不能发起交易，合约地址设置黑名单后不能再被调用或读取；
    function blockAccount(address addr, bool doBlock) external returns (bool);
    // 是否进入黑名单
    function isBlocked(address addr) external view returns (bool);
    // 获取黑名单列表
    function getBlacklist() external view returns (string memory);

    // 启用或停用燃料管理
    function enableGasManage(bool doEnable) external returns (bool);
    // 获取燃料管理是否启用
    function isGasManageEnabled() external view returns (bool);
    // 设置燃料管理账户， 燃料管理账户可以转账燃料给任意账户，普通用户不能转账
    function setGasManager(address addr, bool isManager) external returns (bool);
    // 获取是否燃料管理账户
    function isGasManager(address addr) external view returns (bool);
    // 获取燃料管理账户列表
    function getGasManagerList() external view returns (string memory);

    function setGasUsers(address[] memory addrs, bool addOrRemove) external returns (bool);
    function isGasUser(address addr) external view returns (bool);
    function getGasUserList() external view returns (string memory);

    function setAdmins(address[] memory addrs, bool addOrRemove) external returns (bool);
    function isAdmin(address addr) external view returns (bool);
    function getAdminList() external view returns (string memory);

    event ChangeOwner(address indexed oldOwner, address indexed newOwner);
    event BlockAccount(address indexed addr, bool doBlock);
    event EnableGasManage(bool doEnable);
    event SetGasManager(address indexed addr, bool isManager);
    event SetGasUsers(address[] addrs, bool addOrRemove);
    event SetAdmins(address[] addrs, bool addOrRemove);
}

```

### 地址黑名单管理

管理员将用户账户或合约地址加入黑名单后，用户账户不能发起交易，合约地址不能再被调用或读取。

```js
const signers = await hre.ethers.getSigners();

const address = "0xD62B67170A6bb645f1c59601FbC6766940ee12e5";
// const blockedAccount ="0x5bCA6B1c89e1f4f77b6ad1A0b9b8362c4b316556";
const owner = signers[0].address;

// We get the native contract
var contract = await hre.ethers.getContractAt(
  "IMaasConfig",
  address,
  signers[0]
);

// 获取合约信息
console.log("contract name: ", (await contract.name()).toString());
console.log("blacklist: ", (await contract.getBlacklist()).toString());
console.log("gas user list: ", (await contract.getGasUserList()).toString());
console.log("owner: ", (await contract.getOwner()).toString());
console.log("admin list: ", (await contract.getAdminList()).toString());
console.log("manager list: ", (await contract.getGasManagerList()).toString());

// 修改合约 owner
var tx = await contract.changeOwner(owner);
console.log(tx.hash);
var receipt = await tx.wait();
console.log(receipt.status);
console.log("new owner: ", (await contract.getOwner()).toString());

// 拉入黑名单
tx = await contract.blockAccount(blockedAccount, true);
console.log(tx.hash);
receipt = await tx.wait();
console.log(receipt.status);
console.log("isBlocked: ", await contract.isBlocked(blockedAccount));
console.log("blacklist: ", await contract.getBlacklist());

// 移除黑名单
tx = await contract.blockAccount(blockedAccount, false);
console.log(tx.hash);
receipt = await tx.wait();
console.log(receipt.status);
console.log("isBlocked: ", await contract.isBlocked(blockedAccount));
console.log("blacklist: ", await contract.getBlacklist());
```

### 燃料管理

燃料管理功能可以设置开启或关闭，开启时相关配置才会生效，关闭后燃料转账限制将被取消，
燃料管理功能开启时，普通账户默认不能发送和接收燃料，
gasManager 账户可以给任意地址发送燃料，管理员可以设置 gasManager，
gasUser 账户可以接收任意地址的燃料转账，gasAdmin 账户可以设置 gasUser 账户列表，
管理员可以设置 gasAdmin 列表。

```js
// 开启燃料管理功能，需要管理员发起
tx = await contract.enableGasManage(true);
console.log(tx.hash);
receipt = await tx.wait();
console.log(receipt.status);
console.log("isGasManageEnabled: ", await contract.isGasManageEnabled());

// 设置 gasManager 账户
tx = await contract.connect(signers[1]).setGasManager(signers[2].address, true);
console.log(tx.hash);
receipt = await tx.wait();
console.log(receipt.status);
console.log("isGasManager: ", await contract.isGasManager(signers[2].address));
console.log("getGasManagerList: ", await contract.getGasManagerList());

// 批量设置 gasUser 账户（gasAdmin 或管理员 账户可调用）
tx = await contract.setGasUsers([signers[0].address], true);
console.log(tx.hash);
receipt = await tx.wait();
console.log(receipt.status);
console.log("isGasUser: ", await contract.isGasUser(signers[0].address));
console.log("getGasUserList: ", await contract.getGasUserList());

// 批量设置 gasAdmin
tx = await contract.setAdmins([signers[0].address, signers[1].address], true);
console.log(tx.hash);
receipt = await tx.wait();
console.log(receipt.status);
console.log("isAdmin: ", await contract.isAdmin(signers[0].address));
console.log("getAdminList: ", await contract.getAdminList());
```
