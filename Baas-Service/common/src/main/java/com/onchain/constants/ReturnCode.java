package com.onchain.constants;

import lombok.Getter;

/**
 * ReturnCode规则如下：
 * 错误码从10000-99999。步长1000，1000个为一个大组。10个为一个小组。
 * code%1000 =0 表示成功。其余表示失败
 * 业务失败错误码  自定义,30开头 @定义30xxx
 * 除业务错误码，其他通用错误码的新增需要通过评审
 */
public enum ReturnCode {
    //请求参数错误（如参数格式错误，参数缺少） [12xxx]
    PARAMETER_FAILED(12010, " parameter failed 参数校验失败"),
    TOKEN_NOT_MATCH(12011, " token failed 令牌校验失败"),
    ACCESS_TOKEN_FAIL(12013, "accessToken校验失败"),
    REFRESH_TOKEN_FAIL(12015, "refreshToken校验失败"),
    API_TOKEN_FAIL(12016, "apiToken校验失败"),

    //网络错误,网络超时，系统调用失败（如系统内调用，系统外调用， occur error，如redis，服务间调用超时，httpclient调用，区块链连接节点错误）  [14xxx]
    HYSTRIX_FALLBACK(14030, " 接口熔断打回"),
    REQ_LIMIT_ERROR(18011, " 接口访问频率超出限制"),

    //会话超时  [16xxx]
    SESSION_TIMEOUT(16010, " session timeout 会话超时"),
    TOKEN_EXPIRED(16020, " token expired token过期"),

    //通用请求成功，失败(通用成功消息码) [17xxx]
    REQUEST_SUCCESS(17000, " success 请求成功"),
    REQUEST_FAILED(17010, " failed 请求失败"),

    //文件错误  [18xxx]
    FILE_UPLOAD_FAILED(18010, " upload failed 文件上传失败"),
    FILE_SIZE_ERROR(18011, " 文件大小超出限制"),
    FILE_TYPE_ERROR(18012, " 文件格式错误"),
    FILE_DOWNLOAD_FAILED(18020, " download failed 文件下载失败"),
    FILE_UPDATE_ERROR(18021, " update file failed 文件更新失败"),
    FILE_SHARE_ERROR(18022, "文件共享失败"),
    FILE_NOT_FOUND(18023, "文件不存在"),

    //用户信息错误 【190xx】
    USER_UNREGISTER(19001, "用户未注册"),
    USER_UNLOGIN(19002, "用户未登录"),
    USER_NOT_EXIST(19003, "用户不存在"),
    USER_EXIST(19004, "用户已存在"),
    USER_IS_REGISTER(19005, "手机号已经注册"),
    USER_NAME_NOT_HAVE(19006, "用户名不存在"),
    USER_PASSWORD_ERROR(19007, "用户名或密码错误"),
    USER_ROLE_ERROR(19008, "用户角色权限或访问地址错误"),
    USER_INACTIVE(19010, "用户已停用"),
    USER_URL_ERROR(19011, "用户访问地址或角色错误"),
    USER_REGISTER_ERROR(19012, "用户手机号或角色不满足注册条件"),
    USER_ORIGINAL_PASSWORD_ERROR(19013, "原密码错误"),
    USER_REJECTED(19014, "用户认证被拒绝"),
    USER_APPROVE_STATUS_ERROR(19015, "用户认证状态错误"),

    //验证码错误 【191xx】
    VERIFY_CODE_ERROR(19101, "验证码错误"),
    REGISTER_CODE_ERROR(19102, "注册验证码错误"),
    AUTH_CODE_ERROR(19103, "注册授权码错误"),
    SMS_INTERVAL_ERROR(19104, "用户验证码发送间隔太短"),
    SMS_SEND_ERROR(19105, "用户验证码发送失败"),

    //业务失败错误码  自定义,[30xxx]
    JSON_FORMAT_ERROR(30101, "JSON格式有误"),
    NOT_ON_CHAIN(30102, "数据尚未上链"),
    HASH_MISMATCH(30103, "Hash和原数据不匹配"),
    PROCESS_ID_NOT_FOUNT(30104, "存证编号不存在"),
    PROCESS_TYPE_CHANGE(30105, "存证类型不能修改"),
    JSON_FIELDS_ERROR(30106, "JSON字段有误"),
    DETAIL_FIELD_MISSING(30107, "详情字段缺失"),
    VERIFICATION_INFORMATION_ERROR(30108, "接口验证信息出错"),
    DUPLICATE_COMPANY_NAME(30109, "公司名称重复"),
    DUPLICATE_PHONE_NUMBER(30110, "手机号重复"),
    NO_SUPPLIER_NUMBER(30111, "供应商不存在"),

    // 债权凭证错误 [31xxx]
    TRANSFER_AMOUNT_ERROR(31001, "凭证转让金额错误"),
    TRANSFER_NOT_EXIST(31002, "凭证转让记录不存在"),
    TRANSFER_PARENT_CHANGE(31003, "凭证转让不能修改已选凭证"),
    TRANSFER_CONTRACT_CHANGE(31004, "凭证转让不能修改已选合同"),
    TOKEN_STATUS_ERROR(31005, "凭证状态错误"),
    TOKEN_NOT_EXIST(31006, "债权凭证不存在"),
    TOKEN_ACCEPTOR_ERROR(31007, "债权凭证接收方用户错误"),
    TOKEN_ACCEPT_MESSAGE_ERROR(31008, "拒绝接收意见不能为空"),
    CONTRACT_NOT_EXIST(31009, "合同不存在"),
    DEBT_TOKEN_EXPIRED(31010, "债权凭证已到期"),
    DEBT_TOKEN_DUE_DATE_LIMIT(31011, "债权凭证到期日应在明日起一年以内"),
    OUT_AMOUNT(31012, "凭证金额不得高于合同可关联金额"),
    SAME_CONTRACT(31013, "合同重复"),

    // 链账户或合约错误 [32xxx]
    CHAIN_ACCOUNT_EXIST(32001, "链账户已存在"),
    CHAIN_ACCOUNT_NOT_EXIST(32002, "链账户不存在"),
    CHAIN_ACCOUNT_CREATE_ERROR(32003, "链账户创建失败"),
    GAS_APPLY_LIMIT(32004, "申领燃料达到上限"),
    APP_EXIST(32005, "应用已存在"),
    APP_NOT_EXIST(32006, "应用不存在"),
    PRIVATE_KEY_CHAIN_ACCOUNT_UN_MATCH(32007, "链户与私钥不匹配"),
    PRIVATE_KEY_UN_CUSTOD(32008, "链户未托管私钥"),
    CHAIN_ACCOUNT_SIGNATURE_ERROR(32009, "链户地址与签名信息有问题"),
    SIGNATURE_ORIGIN_TEXT_FORMAT_ERROR(32010, "签名原文格式有问题"),

    // 燃料签约合同错误 [34xxx]
    GAS_CONTRACT_NOT_EXIST(34001, "燃料签约合同不存在"),
    GAS_CONTRACT_STATUS_ERROR(34002, "燃料签约合同审批状态错误"),

    // 跨链错误 [33xxx]
    SRC_CONTRACT_ADDRESS_ERROR(33001, "跨链源合约地址错误"),

    // 链操作错误 [35xxx]
    GET_BALANCE_ERROR(35001, "web3j获取余额报错"),
    TRANSACTION_ERROR(35002, "交易未执行成功"),
    TX_HASH_MISMATCH_ERROR(35003, "本地交易hash与远程交易hash不一致"),
    REMAIN_NOT_ENOUGH_ERROR(35004, "签约的余额不足"),
    UN_MATCH_MIN_TRANSFR_AMOUNT_ERROR(35005, "不满足最小转账金额"),

    //最大错误码
    MAX_RETURN_CODE(99999, "最大错误码");

    @Getter
    private Integer value;
    @Getter
    private String desc;

    ReturnCode(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ReturnCode getByCode(Integer code) {
        ReturnCode[] values = ReturnCode.values();
        for (ReturnCode p : values) {
            if (code.equals(p.getValue())) {
                return p;
            }
        }
        return MAX_RETURN_CODE;
    }

}

