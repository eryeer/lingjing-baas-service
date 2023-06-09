package com.onchain.dna2explorer.constants;

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
    USER_PENDING(19014, "用户未认证"),

    //验证码错误 【191xx】
    LOGIN_CODE_ERROR(19101, "登录验证码错误"),
    REGISTER_CODE_ERROR(19102, "注册验证码错误"),
    AUTH_CODE_ERROR(19103, "注册授权码错误"),
    SMS_INTERVAL_ERROR(19104, "用户验证码发送间隔太短"),
    SMS_SEND_ERROR(19105, "用户验证码发送失败"),
    CAPTCHA_SEND_ERROR(19106, "用户图形验证码发送失败"),
    QUERY_TIME_ERROR(19107, "查询时间为未来时间"),
    GENERATE_CSV_ERROR(19008, "生成csv文件出错"),

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
    NOT_FOUND_TOKEN(30112, "contract合约不存在token"),

    // 链账户或合约错误 [32xxx]
    CHAIN_ACCOUNT_EXIST(32001, "链账户已存在"),
    CHAIN_ACCOUNT_NOT_EXIST(32002, "链账户不存在"),
    CHAIN_ACCOUNT_CREATE_ERROR(32003, "链账户创建失败"),
    GAS_APPLY_LIMIT(32004, "申领燃料达到上限"),
    APP_EXIST(32005, "应用已存在"),
    APP_NOT_EXIST(32006, "应用不存在"),

    CONTRACT_NOT_EXIST(32100, "合约不存在"),
    CONTRACT_ABI_UPLOADER_INVALID(32200, "合约上传签名校验失败"),
    CONTRACT_TOKEN_NONE(32200, "合约地址没有token"),

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

