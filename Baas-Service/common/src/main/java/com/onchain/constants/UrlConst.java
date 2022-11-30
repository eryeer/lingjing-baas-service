package com.onchain.constants;

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
    public static final String CHANGE_PHONE_NUMBER = "/baas/user/changePhoneNumber";
    public static final String RESET_PASSWORD = "/baas/user/resetPassword";
    public static final String SUBMIT_USER_KYC = "/baas/user/submitUserKyc";
    public static final String APPROVE_USER_KYC = "/baas/user/approveUserKyc";
    public static final String GET_USER_LIST = "/baas/user/getUserList";
    public static final String GET_USER_KYC_RECORD_LIST = "/baas/user/getUserKycRecordList";

    public static final String SEND_VERIFY_CODE = "/baas/common/sendVerifyCode";
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
    public static final String PRIVATE_KEY_CUSTODY = "/baas/chain/privateKeyCustody";
    public static final String DOWNLOAD_PRIVATE_KEY = "/baas/chain/downloadPrivateKey";
    public static final String GET_CHAIN_ACCOUNT = "/baas/chain/getChainAccount";
    public static final String CHANGE_GAS_TRANSFER_STATUS = "/baas/chain/changeGasTransferStatus";
    public static final String DELETE_CHAIN_ACCOUNT = "/baas/chain/deleteChainAccount";
    public static final String APPLY_GAS = "/baas/chain/applyGas";
    public static final String GET_APPLY_LIST = "/baas/chain/getApplyList";

    public static final String CREATE_GAS_CONTRACT = "/baas/gas/createGasContract";
    public static final String GET_GAS_CONTRACT_LIST = "/baas/gas/getGasContractList";
    public static final String GET_GAS_SUMMARY = "/baas/gas/getGasSummary";
    public static final String GET_ADMIN_GAS_CONTRACT_LIST = "/baas/gas/getAdminGasContractList";
    public static final String APPROVE_GAS_CONTRACT = "/baas/gas/approveGasContract";
    public static final String GET_GAS_CONTACT_STATISTIC_LIST = "/baas/gas/getGasContactStatisticList";

    public static final String UPLOAD_PDFS = "/baas/pdfs/uploadPdfs";
    public static final String GET_PDFS_LIST = "/baas/pdfs/getPdfsList";
    public static final String DOWNLOAD_PDFS = "/baas/pdfs/downloadPdfs";
    public static final String SHARE_PDFS = "/baas/pdfs/sharePdfs";

    public static final String SEND_CROSS_CHAIN = "/cross/chain/sendCrossChain";
    public static final String SET_CROSS_DST = "/cross/chain/setCrossDst";
    public static final String GET_CROSS_TBD_LIST = "/cross/chain/getCrossTbdList";
    public static final String GET_CROSS_CHAIN_LIST = "/cross/chain/getCrossChainList";
    public static final String GET_CROSS_CHAIN = "/cross/chain/getCrossChain";

    public static final String GET_BLOCK_LIST = "/explorer/block/getBlockList";
    public static final String GET_BLOCK = "/explorer/block/getBlock";
    public static final String GET_TRANSACTION_LIST = "/explorer/transaction/getTransactionList";
    public static final String GET_TRANSACTION = "/explorer/transaction/getTransaction";
    public static final String GET_ADDRESS_LIST = "/explorer/address/getAddressList";
    public static final String GET_ADDRESS = "/explorer/address/getAddress";
    public static final String GET_TRANSACTION_LIST_BY_ADDRESS = "/explorer/address/getTransactionListByAddress";
    public static final String GET_TOTAL_SUMMARY = "/explorer/summary/getTotalSummary";
    public static final String GET_ADDRESS_SUMMARY = "/explorer/summary/getAddressSummary";
    public static final String GET_BLOCK_SUMMARY = "/explorer/summary/getBlockSummary";
    public static final String GET_TRANSACTION_SUMMARY = "/explorer/summary/getTransactionSummary";

    // rest cross micro-service
    public static final String REST_GET_FILE_BY_UUID = "/rest/userManage/getFileByUuid";
    public static final String REST_GET_FILE_BY_UUIDS = "/rest/userManage/getFileByUuids";
    public static final String REST_GET_VERIFIED_CENTERS = "/rest/userManage/getVerifiedCenters";
    public static final String REST_GET_CENTER_COMPANY_BY_ID = "/rest/userManage/getCenterCompanyById";
    public static final String REST_GET_SUPPLIER_BY_ID = "/rest/userManage/getSupplierByIdOrName";
    public static final String REST_MARK_FILES = "/rest/userManage/markFiles";


    // 不需要登录的接口
    public static final String[] NOT_LOGIN_URLS = {REGISTER_USER, CHECK_USER_REGISTER, LOGIN, REFRESH_TOKEN, SEND_VERIFY_CODE, GET_SERVER_TIME, GET_RETURN_CODES, UPLOAD_FILE, RESET_PASSWORD};
    // 所有角色都可以访问的接口
    public static final String[] COMMON_URLS = {LOGOUT, GET_USER_BY_ID, CHANGE_PASSWORD, CHANGE_PHONE_NUMBER, GET_PLATFORM_INFO, UPDATE_FILE, GET_TOOLS,
            GET_BLOCK_LIST, GET_BLOCK, GET_TRANSACTION_LIST, GET_TRANSACTION, GET_ADDRESS_LIST, GET_ADDRESS, GET_TRANSACTION_LIST_BY_ADDRESS, GET_TOTAL_SUMMARY, GET_ADDRESS_SUMMARY, GET_BLOCK_SUMMARY, GET_TRANSACTION_SUMMARY,
            GET_CHAIN_ACCOUNT,};
    // 普通用户 可以访问的接口
    public static final String[] CU_URLS = {APP_CREATE, APP_REMOVE, GET_APP_LIST, GET_APP, GET_CONTRACT_TEMPLATES, DEPLOY, UPDATE_FILE_LIST,
            ACCOUNT_CREATE, PRIVATE_KEY_CUSTODY, DOWNLOAD_PRIVATE_KEY, CHANGE_GAS_TRANSFER_STATUS, DELETE_CHAIN_ACCOUNT, APPLY_GAS, GET_APPLY_LIST, UPLOAD_PDFS, GET_PDFS_LIST, DOWNLOAD_PDFS, SHARE_PDFS,
            SEND_CROSS_CHAIN, GET_CROSS_CHAIN, GET_CROSS_CHAIN_LIST, SET_CROSS_DST, GET_CROSS_TBD_LIST, SUBMIT_USER_KYC,
            CREATE_GAS_CONTRACT, GET_GAS_CONTRACT_LIST, GET_GAS_SUMMARY};
    // SM 可以访问的接口
    public static final String[] PM_URLS = {APPROVE_USER_KYC, GET_USER_LIST, GET_USER_KYC_RECORD_LIST,
            GET_ADMIN_GAS_CONTRACT_LIST, APPROVE_GAS_CONTRACT, GET_GAS_CONTACT_STATISTIC_LIST};

    // 不能通过网关访问的接口
    public static final String[] INTERNAL_URLS = {};
}
