package com.onchain.dna2explorer.constants;

public class UrlConst {

    public static final String INTERNAL_SERVICE = "internal-api";
    public static final String SEATA_ACCOUNT = "account/deductMoney";
    public static final String SEATA_STORAGE = "storage/deductStorage";

    public static final String USER_MANAGE_SERVICE = "baas-service";

    public static final String REGISTER_USER = "/baas/user/registerUser";
    public static final String CHECK_USER_REGISTER = "/baas/user/checkUserRegister";
    public static final String LOGIN = "/baas/user/login";
    public static final String REFRESH_TOKEN = "/baas/user/refreshToken";
    public static final String LOGOUT = "/baas/user/logout";
    public static final String GET_USER_BY_ID = "/baas/user/getUserById";
    public static final String CHANGE_PASSWORD = "/baas/user/changePassword";
    public static final String APPROVE_USER = "/baas/user/approveUser";
    public static final String GET_USER_LIST = "/baas/user/getUserList";

    public static final String SEND_REGISTER = "/baas/common/sendRegisterCode";
    public static final String SEND_LOGIN = "/baas/common/sendLoginCode";
    public static final String SEND_AUTH = "/baas/common/sendAuthCode";
    public static final String UPLOAD_FILE = "/baas/common/uploadFile";
    public static final String UPDATE_FILE = "/baas/common/updateFile";
    public static final String GET_SERVER_TIME = "/baas/common/getServerTime";
    public static final String GET_RETURN_CODES = "/baas/common/getReturnCodes";
    public static final String GET_PLATFORM_INFO = "/baas/common/getPlatformInfo";
    public static final String GET_TOOLS = "/baas/common/getTools";

    public static final String APP_CREATE = "/baas/contract/appCreate";
    public static final String APP_REMOVE = "/baas/contract/appRemove";
    public static final String GET_APP_LIST = "/baas/contract/getAppList";
    public static final String GET_APP = "/baas/contract/getApp";
    public static final String GET_CONTRACT_TEMPLATES = "/baas/contract/getContractTemplates";
    public static final String DEPLOY = "/baas/contract/deploy";
    public static final String UPDATE_FILE_LIST = "/baas/contract/updateFileList";

    public static final String ACCOUNT_CREATE = "/baas/chain/accountCreate";
    public static final String GET_CHAIN_ACCOUNT = "/baas/chain/getChainAccount";
    public static final String APPLY_GAS = "/baas/chain/applyGas";

    public static final String UPLOAD_PDFS = "/baas/pdfs/uploadPdfs";
    public static final String GET_PDFS_LIST = "/baas/pdfs/getPdfsList";
    public static final String DOWNLOAD_PDFS = "/baas/pdfs/downloadPdfs";

    public static final String SEND_CROSS_CHAIN = "/cross/chain/sendCrossChain";
    public static final String GET_CROSS_CHAIN_LIST = "/cross/chain/getCrossChainList";
    public static final String GET_CROSS_CHAIN = "/cross/chain/getCrossChain";

    public static final String GET_BLOCK_LIST = "/explorer/block/getBlockList";
    public static final String GET_BLOCK = "/explorer/block/getBlock";
    public static final String GET_TRANSACTION_LIST = "/explorer/transaction/getTransactionList";
    public static final String GET_TRANSACTION = "/explorer/transaction/getTransaction";
    public static final String GET_ADDRESS_LIST = "/explorer/address/getAddressList";
    public static final String GET_ADDRESS_LIST_BY_ADDRESS = "/explorer/address/getAddressListByAddress";
    public static final String GET_ADDRESS = "/explorer/address/getAddress";
    public static final String GET_TRANSACTION_LIST_BY_ADDRESS = "/explorer/address/getTransactionListByAddress";
    public static final String GET_TRANSFER_LIST_BY_ADDRESS = "/explorer/address/getTransferListByAddress";
    public static final String GET_TRANSFER_LIST_BY_ADDRESS_AND_TIME = "/explorer/address/downloadTransferList";
    public static final String GET_TRANSACTION_LIST_BY_ADDRESS_AND_TIME = "/explorer/address/downloadTransactionList";
    public static final String GET_TOTAL_SUMMARY = "/explorer/summary/getTotalSummary";
    public static final String GET_ADDRESS_SUMMARY = "/explorer/summary/getAddressSummary";
    public static final String GET_BLOCK_SUMMARY = "/explorer/summary/getBlockSummary";
    public static final String GET_TRANSACTION_SUMMARY = "/explorer/summary/getTransactionSummary";
    public static final String UPLOAD_ABI = "/explorer/contract/uploadAbi";
    public static final String GET_TOKEN_HOLDER = "/explorer/token/getTokenHolder";

    // rest cross micro-service
    public static final String REST_GET_FILE_BY_UUID = "/rest/userManage/getFileByUuid";
    public static final String REST_GET_FILE_BY_UUIDS = "/rest/userManage/getFileByUuids";
    public static final String REST_GET_VERIFIED_CENTERS = "/rest/userManage/getVerifiedCenters";
    public static final String REST_GET_CENTER_COMPANY_BY_ID = "/rest/userManage/getCenterCompanyById";
    public static final String REST_GET_SUPPLIER_BY_ID = "/rest/userManage/getSupplierByIdOrName";
    public static final String REST_MARK_FILES = "/rest/userManage/markFiles";

    //external-api
    public static final String EXTERNAL_GET_TOKEN_HOLDER = "/external/token/getTokenHolder";

    // 不需要登录的接口
    public static final String[] NOT_LOGIN_URLS = {REGISTER_USER, CHECK_USER_REGISTER, LOGIN, REFRESH_TOKEN, SEND_REGISTER, SEND_LOGIN, GET_SERVER_TIME, GET_RETURN_CODES, UPLOAD_FILE,};
    // 所有角色都可以访问的接口
    public static final String[] COMMON_URLS = {LOGOUT, GET_USER_BY_ID, CHANGE_PASSWORD, GET_PLATFORM_INFO, UPDATE_FILE, GET_TOOLS,
            GET_BLOCK_LIST, GET_BLOCK, GET_TRANSACTION_LIST, GET_TRANSACTION, GET_ADDRESS_LIST, GET_ADDRESS, GET_TRANSACTION_LIST_BY_ADDRESS, GET_TOTAL_SUMMARY, GET_ADDRESS_SUMMARY, GET_BLOCK_SUMMARY, GET_TRANSACTION_SUMMARY, EXTERNAL_GET_TOKEN_HOLDER};
    // 普通用户 可以访问的接口
    public static final String[] CU_URLS = {APP_CREATE, APP_REMOVE, GET_APP_LIST, GET_APP, GET_CONTRACT_TEMPLATES, DEPLOY, UPDATE_FILE_LIST,
            ACCOUNT_CREATE, GET_CHAIN_ACCOUNT, APPLY_GAS, UPLOAD_PDFS, GET_PDFS_LIST, DOWNLOAD_PDFS,
            SEND_CROSS_CHAIN, GET_CROSS_CHAIN, GET_CROSS_CHAIN_LIST};
    // SM 可以访问的接口
    public static final String[] PM_URLS = {APPROVE_USER, GET_USER_LIST};

    // 不能通过网关访问的接口
    public static final String[] INTERNAL_URLS = {};
}
