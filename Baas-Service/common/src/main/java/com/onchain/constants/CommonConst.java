package com.onchain.constants;

public class CommonConst {
    public static final String PRODUCE = "application/json;charset=UTF-8";
    public static final String SPRING_APPLICATION_NAME = "${spring.application.name}";
    public static final String PLATFORM_NAME = "上海分布信息科技有限公司"; // 平台主体名称
    public static final String PLATFORM_PUB_KEY = "XXX"; // 平台主体公钥
    public static final String PAYMENT_COMMIT_KEY = "SCF/TEMP/付款承诺函.doc"; // 付款承诺函模板地址
    public static final String PROOF_TEMPLATE_KEY = "BAAS/TEMPLATE/Proof.sol"; // 存证合约模板地址
    public static final String VOTE_TEMPLATE_KEY = "BAAS/TEMPLATE/Voting.sol"; // 投票合约模板地址

    public static final String MULTI_LAN_DEPLOYER = "BAAS/TOOLS/MultiLanDeployer-1.0.zip"; // 多语言合约部署工具
    public static final String DNA2_JAVA_SDK = "BAAS/TOOLS/DNA2JavaSDK-1.0.zip"; // Java SDK 工具
    public static final String DNA2_GO_SDK = "BAAS/TOOLS/DNA2GolangSDK-1.0.zip"; // Golang SDK 工具
    public static final String DNA2_JS_SDK = "BAAS/TOOLS/DNA2JavaScriptSDK-1.0.zip"; // JavaScript SDK 工具

    public static final String zeroTo255 = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";
    public static final String IP_REGEX = zeroTo255 + "\\." + zeroTo255 + "\\."
            + zeroTo255 + "\\." + zeroTo255;
    public static long IDLE_CONNECTION_TIME = 60000;

    //public static final int GENESIS_TIME = 1530316800;
    public static final int GENESIS_TIME = 1580486400;
    public static final int GENESIS_MONTH = 1580486400; // 2020-02-01

    public static final int ONE_DAY_IN_SEC = 86400;
    public static final int TEN_MINUTES = 10 * 60;

    public static final String ADDR_DAILY_SUMMARY_NATIVETYPE = "0000000000000000000000000000000000000000";

    public static final int POOL_MAXTOTAL = 200;
    public static final int POOL_DEFAULT_MAX_PER_ROUTE = 200;
    public static final int EVICTOR_WAITTIME = 60000;
    public static final int REQUEST_CONNECTION_REQUEST_TIMEOUT = 500;
    public static final int REQUEST_CONNECTION_TIMEOUT = 5000;
    public static final int REQUEST_SOCKET_TIMEOUT = 30000;
    public static final boolean REQUEST_STALE_CONNECTION_CHECK_ENABLED = true;

    public static final String PHONE_REGEX = "[1][3-9]\\d{9}";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
    public static final String ID_NUMBER_REGEX = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(X|x)$)";
    public static final String PASSWORD_REGEX = "^.*(?=.{8,18})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String SOCIAL_CODE = "^([0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}|[1-9]\\d{14})$";
    public static final String BANK_CODE = "^[0-9]+$";

    public static final String HEX_32_REGEX = "^[a-fA-F0-9]{32}$";
    public static final String HEX_64_REGEX = "^[a-fA-F0-9]{64}$";
    public static final String NUMBER_REGEX = "^[0-9]+$";

    public static final Integer REQTIME_MAX_RANGE = 40 * 24 * 60 * 60;
    public static final Integer REQTIME_MAX_RANGE_WEEK = 7 * 24 * 60 * 60;
    public static final Integer FILE_URL_VALID_TIME = 60 * 60;

    public static final int COMPANY_CENTER_ID = 0;
    public static final int FINANCE_ID = 0;
    public static final int SUPPLIER_ID = 0;

    public static final String COMPANY_CENTER_TYPE = "companyCenterId";
    public static final String FINANCE_TYPE = "finance";
    public static final String SUPPLIER_TYPE = "supplier";
    public static final String CREDIT_NO_KEY = "creditNoKey";
    public static final String DEBT_TOKEN_KEY = "debtTokenKey";

    public static final int TOKEN_VALUE = 0;

    public static final Integer ZERO = 0;
    public static final Integer ONE = 1;
    public static final Integer NINE = 9;

    //--restTemplate用---//
    public static final int REST_TEMPLATE_CONNECT_TIMEOUT = 20000;
    public static final int REST_TEMPLATE_READ_TIMEOUT = 20000;
    public static final int REST_TEMPLATE_WRITE_TIMEOUT = 20000;


    //--JWT--//
    public static final int TOKEN_AVAILABLE = 0;
    public static final int TOKEN_EXPIRED = 1;
    public static final int TOKEN_ILLEGAL = 2;

    //----FeignClient用----//
    public static final String HYSTRIX_FALLBACK_DESC = "熔断打回";
    public static final String FEIGN_RESPONSE_NULL_DESC = "FeignClient响应为空";
    public static final String FEIGN_RETURNCODE_NULL_DESC = "FeignClient状态码为空";
    public static final String FEIGN_RESPONSE_FAIL = "FeignClient调用失败";

    //--Redis--//
    //重置Token  user_token_${userId}
    public static final String USER_REFRESH_TOKEN = "user_token_";
    public static final String HEADER_ACCESS_TOKEN = "accessToken";
    public static final String HEADER_CLIENT_TYPE = "clientType";

    //权限集合 authority_${userId}
    public static final String USER_AUTHORITY = "authority_";

    // SMS
    public static final String SMS_OK = "Ok";
    public static final String CN_CODE = "+86";
    // 验证码类型
    public static final String SMS_REGISTER = "R"; // 注册验证码
    public static final String SMS_LOGIN = "L"; // 登录验证码
    public static final String SMS_CHANGE_PHONE = "CP"; // 修改手机号验证码
    public static final String SMS_RESET = "RS"; // 重置验证码
    public static final String CODE_TYPE_REGEX = "^(R)|(L)|(CP)|(RS)$";


    // COS 对象存储
    public static final String JPG = "image/jpeg";
    public static final String PNG = "image/png";
    public static final String PDF = "application/pdf";
    public static final long FILE_MAX_SIZE = 10 * 1024 * 1024; // 10MB

    // Role
    public static final String PM = "PM";
    public static final String CU = "CU";
//
//    // 入口端类型
//    public static final String SUPPLIER = "S";
//    public static final String CENTER = "C";
//    public static final String FINANCE = "F";
//    public static final String PLATFORM = "P";

    // 记录状态（所有表都有1，0状态，用户和客户额外有停用状态）
    public static final String ACTIVE = "1"; // 已启用
    public static final String DELETED = "0"; // 已删除
    public static final String INACTIVE = "2"; // 已停用

    // 文件根目录
    public static final String FILE_ROOT = "BAAS/";
    // 文件类型
    public static final String FILE_IDA = "IDA"; // 身份证正面
    public static final String FILE_IDB = "IDB"; // 身份证反面
    public static final String FILE_BL = "BL"; // 营业执照正本
    public static final String FILE_BLC = "BLC"; // 营业执照副本
    public static final String FILE_SOL = "SOL"; // 智能合约文件
    public static final String FILE_WALLET = "WAL"; // 钱包文件

    // 用户认证状态
    public static final String PENDING = "Pending"; // 待审批
    public static final String APPROVED = "Approved"; // 审批通过
    public static final String REJECTED = "Rejected"; // 已拒绝
    public static final String TO_BE_UPDATED = "ToBeUpdated"; // 需修改
    public static final String APPROVE_STATUS = "^(Approved)|(Rejected)|(ToBeUpdated)$";

    // 核验类型
    public static final String KYC_NEW = "NEW"; // 审批通过
    public static final String KYC_UPDATE = "UPDATE"; // 审批通过
    public static final String KYC_TYPE_REGEX = "^(NEW)|(UPDATE)$";

    public static final String LatestBlockNumberKey = "latest";
    // 模板类型
    public static final String PROOF = "PROOF";
    public static final String VOTE = "VOTE";
    public static final String CUSTOM = "CUSTOM";
    public static final String TEMP_TYPE_REGEX = "^(PROOF)|(VOTE)|(CUSTOM)$";

    public static final String PROOF_BIN = "60806040523480156200001157600080fd5b5060405162000a4038038062000a40833981016040819052620000349162000134565b81516200004990600090602085019062000071565b50600180546001600160a01b0319166001600160a01b03929092169190911790555062000272565b8280546200007f906200021f565b90600052602060002090601f016020900481019282620000a35760008555620000ee565b82601f10620000be57805160ff1916838001178555620000ee565b82800160010185558215620000ee579182015b82811115620000ee578251825591602001919060010190620000d1565b50620000fc92915062000100565b5090565b5b80821115620000fc576000815560010162000101565b80516001600160a01b03811681146200012f57600080fd5b919050565b6000806040838503121562000147578182fd5b82516001600160401b03808211156200015e578384fd5b818501915085601f83011262000172578384fd5b8151818111156200018757620001876200025c565b604051601f8201601f19908116603f01168101908382118183101715620001b257620001b26200025c565b81604052828152602093508884848701011115620001ce578687fd5b8691505b82821015620001f15784820184015181830185015290830190620001d2565b828211156200020257868484830101525b95506200021491505085820162000117565b925050509250929050565b6002810460018216806200023457607f821691505b602082108114156200025657634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052604160045260246000fd5b6107be80620002826000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063693ec85e1161005b578063693ec85e146100de57806380599e4b146100f1578063cfad57a214610106578063e942b516146101195761007d565b806327d2fce5146100825780633b18f691146100a057806346008a07146100b3575b600080fd5b61008a61012c565b60405161009791906106a8565b60405180910390f35b61008a6100ae3660046105c4565b6101ba565b6001546100c6906001600160a01b031681565b6040516001600160a01b039091168152602001610097565b61008a6100ec3660046105c4565b6101de565b6101046100ff3660046105c4565b61028e565b005b610104610114366004610596565b610325565b6101046101273660046105ff565b61039d565b6000805461013990610737565b80601f016020809104026020016040519081016040528092919081815260200182805461016590610737565b80156101b25780601f10610187576101008083540402835291602001916101b2565b820191906000526020600020905b81548152906001019060200180831161019557829003601f168201915b505050505081565b80516020818301810180516002825292820191909301209152805461013990610737565b60606002826040516101f0919061068c565b9081526020016040518091039020805461020990610737565b80601f016020809104026020016040519081016040528092919081815260200182805461023590610737565b80156102825780601f1061025757610100808354040283529160200191610282565b820191906000526020600020905b81548152906001019060200180831161026557829003601f168201915b50505050509050919050565b6001546001600160a01b031633146102c15760405162461bcd60e51b81526004016102b8906106e9565b60405180910390fd5b6002816040516102d1919061068c565b908152602001604051809103902060006102eb9190610437565b7f834a2d47e948021d7136fb7275b3f1e1feae6333c0d683e8c13f901667defd8c8160405161031a91906106a8565b60405180910390a150565b6001546001600160a01b0316331461034f5760405162461bcd60e51b81526004016102b8906106e9565b600180546001600160a01b0319166001600160a01b0383169081179091556040519081527f91a8c1cc2d4a3bb60738481947a00cbb9899c822916694cf8bb1d68172fdcd549060200161031a565b6001546001600160a01b031633146103c75760405162461bcd60e51b81526004016102b8906106e9565b806002836040516103d8919061068c565b908152602001604051809103902090805190602001906103f9929190610476565b507fddc5a395ff29c22c0e109c1b1e032440d25c3f9452ffe7327b9dbb2f30fa632a828260405161042b9291906106bb565b60405180910390a15050565b50805461044390610737565b6000825580601f106104555750610473565b601f01602090049060005260206000209081019061047391906104fa565b50565b82805461048290610737565b90600052602060002090601f0160209004810192826104a457600085556104ea565b82601f106104bd57805160ff19168380011785556104ea565b828001600101855582156104ea579182015b828111156104ea5782518255916020019190600101906104cf565b506104f69291506104fa565b5090565b5b808211156104f657600081556001016104fb565b600082601f83011261051f578081fd5b813567ffffffffffffffff8082111561053a5761053a610772565b604051601f8301601f19908116603f0116810190828211818310171561056257610562610772565b8160405283815286602085880101111561057a578485fd5b8360208701602083013792830160200193909352509392505050565b6000602082840312156105a7578081fd5b81356001600160a01b03811681146105bd578182fd5b9392505050565b6000602082840312156105d5578081fd5b813567ffffffffffffffff8111156105eb578182fd5b6105f78482850161050f565b949350505050565b60008060408385031215610611578081fd5b823567ffffffffffffffff80821115610628578283fd5b6106348683870161050f565b93506020850135915080821115610649578283fd5b506106568582860161050f565b9150509250929050565b60008151808452610678816020860160208601610707565b601f01601f19169290920160200192915050565b6000825161069e818460208701610707565b9190910192915050565b6000602082526105bd6020830184610660565b6000604082526106ce6040830185610660565b82810360208401526106e08185610660565b95945050505050565b60208082526004908201526310b3b7bb60e11b604082015260600190565b60005b8381101561072257818101518382015260200161070a565b83811115610731576000848401525b50505050565b60028104600182168061074b57607f821691505b6020821081141561076c57634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052604160045260246000fdfea2646970667358221220843b146ffabe61a907d3aab520d3dfbd9cfa95004420d9a3a85f863d0aec4a0b64736f6c63430008020033";
    public static final String VOTE_BIN = "60806040523480156200001157600080fd5b5060405162000aa238038062000aa28339810160408190526200003491620001c3565b80516200004990600090602084019062000057565b50506000546002556200036b565b828054828255906000526020600020908101928215620000a9579160200282015b82811115620000a9578251805162000098918491602090910190620000bb565b509160200191906001019062000078565b50620000b792915062000146565b5090565b828054620000c99062000318565b90600052602060002090601f016020900481019282620000ed576000855562000138565b82601f106200010857805160ff191683800117855562000138565b8280016001018555821562000138579182015b82811115620001385782518255916020019190600101906200011b565b50620000b792915062000167565b80821115620000b75760006200015d82826200017e565b5060010162000146565b5b80821115620000b7576000815560010162000168565b5080546200018c9062000318565b6000825580601f10620001a05750620001c0565b601f016020900490600052602060002090810190620001c0919062000167565b50565b60006020808385031215620001d6578182fd5b82516001600160401b0380821115620001ed578384fd5b8185019150601f868184011262000202578485fd5b82518281111562000217576200021762000355565b620002268586830201620002e5565b81815285810190858701885b84811015620002d557815188018c603f8201126200024e578a8bfd5b898101518881111562000265576200026562000355565b62000278818901601f19168c01620002e5565b81815260408f818486010111156200028e578d8efd5b8d5b83811015620002ad578481018201518382018f01528d0162000290565b83811115620002be578e8e85850101525b505086525050928801929088019060010162000232565b50909a9950505050505050505050565b604051601f8201601f191681016001600160401b038111828210171562000310576200031062000355565b604052919050565b6002810460018216806200032d57607f821691505b602082108114156200034f57634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052604160045260246000fd5b610727806200037b6000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c80638d4b44d91161005b5780638d4b44d9146100e6578063a9a981a314610114578063b13c744b1461011d578063e89927ef1461013d5761007d565b80630d15fd77146100825780634a3bf3771461009e578063538c91b2146100c3575b600080fd5b61008b60035481565b6040519081526020015b60405180910390f35b6100b16100ac366004610419565b610152565b60405160ff9091168152602001610095565b6100d66100d1366004610419565b6101d0565b6040519015158152602001610095565b6100b16100f4366004610419565b805160208183018101805160018252928201919093012091525460ff1681565b61008b60025481565b61013061012b3660046104c3565b610281565b60405161009591906105be565b61015061014b366004610419565b61032d565b005b600061015d826101d0565b6101a55760405162461bcd60e51b815260206004820152601460248201527318d85b991a59185d19481a5cc81a5b9d985b1a5960621b60448201526064015b60405180910390fd5b6001826040516101b59190610507565b9081526040519081900360200190205460ff1690505b919050565b6000805b60005481101561027857826040516020016101ef9190610507565b604051602081830303815290604052805190602001206000828154811061022657634e487b7160e01b600052603260045260246000fd5b906000526020600020016040516020016102409190610523565b6040516020818303038152906040528051906020012014156102665760019150506101cb565b80610270816106aa565b9150506101d4565b50600092915050565b6000818154811061029157600080fd5b9060005260206000200160009150905080546102ac9061066f565b80601f01602080910402602001604051908101604052809291908181526020018280546102d89061066f565b80156103255780601f106102fa57610100808354040283529160200191610325565b820191906000526020600020905b81548152906001019060200180831161030857829003601f168201915b505050505081565b610336816101d0565b6103795760405162461bcd60e51b815260206004820152601460248201527318d85b991a59185d19481a5cc81a5b9d985b1a5960621b604482015260640161019c565b6001808260405161038a9190610507565b90815260405190819003602001902080546000906103ac90849060ff1661061a565b92506101000a81548160ff021916908360ff1602179055506001600360008282546103d79190610602565b90915550506040517f877cd47bbf47a162b34be0d8dc765f17cbe6086842779cd4fb69a87d6c0b6a209061040e90839033906105d8565b60405180910390a150565b60006020828403121561042a578081fd5b813567ffffffffffffffff80821115610441578283fd5b818401915084601f830112610454578283fd5b813581811115610466576104666106db565b604051601f8201601f19908116603f0116810190838211818310171561048e5761048e6106db565b816040528281528760208487010111156104a6578586fd5b826020860160208301379182016020019490945295945050505050565b6000602082840312156104d4578081fd5b5035919050565b600081518084526104f381602086016020860161063f565b601f01601f19169290920160200192915050565b6000825161051981846020870161063f565b9190910192915050565b815460009081906002810460018083168061053f57607f831692505b602080841082141561055f57634e487b7160e01b87526022600452602487fd5b8180156105735760018114610584576105b0565b60ff198616895284890196506105b0565b60008a815260209020885b868110156105a85781548b82015290850190830161058f565b505084890196505b509498975050505050505050565b6000602082526105d160208301846104db565b9392505050565b6000604082526105eb60408301856104db565b905060018060a01b03831660208301529392505050565b60008219821115610615576106156106c5565b500190565b600060ff821660ff84168060ff03821115610637576106376106c5565b019392505050565b60005b8381101561065a578181015183820152602001610642565b83811115610669576000848401525b50505050565b60028104600182168061068357607f821691505b602082108114156106a457634e487b7160e01b600052602260045260246000fd5b50919050565b60006000198214156106be576106be6106c5565b5060010190565b634e487b7160e01b600052601160045260246000fd5b634e487b7160e01b600052604160045260246000fdfea26469706673582212209f320105a458507e085527f5c4161eab929b127edce74bf918d6753476aeea2c64736f6c63430008020033";

    public static final String PROOF_ABI = "[{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_proofName\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"_govAddress\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"_key\",\"type\":\"string\"}],\"name\":\"Remove\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"_key\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"_value\",\"type\":\"string\"}],\"name\":\"Set\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"address\",\"name\":\"_govAddress\",\"type\":\"address\"}],\"name\":\"SetGov\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_key\",\"type\":\"string\"}],\"name\":\"get\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"govAddress\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"proofName\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"name\":\"proofs\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_key\",\"type\":\"string\"}],\"name\":\"remove\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_key\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"_value\",\"type\":\"string\"}],\"name\":\"set\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_govAddress\",\"type\":\"address\"}],\"name\":\"setGov\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";
    public static final String VOTE_ABI = "[{\"inputs\":[{\"internalType\":\"string[]\",\"name\":\"candidateNames\",\"type\":\"string[]\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"candidate\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"voterId\",\"type\":\"address\"}],\"name\":\"VOTE\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"candidateCount\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"candidateList\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"totalVotes\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"candidate\",\"type\":\"string\"}],\"name\":\"totalVotesFor\",\"outputs\":[{\"internalType\":\"uint8\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"candidate\",\"type\":\"string\"}],\"name\":\"validCandidate\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"candidate\",\"type\":\"string\"}],\"name\":\"voteForCandidate\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"name\":\"votesReceived\",\"outputs\":[{\"internalType\":\"uint8\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"}]";

    public static final String SUCCESS = "SUCCESS";

    // 上传者类型
    public static final String SELF = "SELF";
    public static final String OTHER = "OTHER";

}
