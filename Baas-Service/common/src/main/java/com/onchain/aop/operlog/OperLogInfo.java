package com.onchain.aop.operlog;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhaochen on 2018/3/16.
 */
@Data
public class OperLogInfo implements Serializable {
    public OperLogInfo() {
    }

    public OperLogInfo(OperLogInfo info) {
        this.id = info.id;
        this.opertype = info.opertype;
        this.createDate = info.createDate;
        this.endDate = info.endDate;
        this.hostName = info.hostName;
        this.hostAddr = info.hostAddr;
        this.title = info.title;
        this.params = info.params;
        this.remoteAddr = info.remoteAddr;
        this.requestUri = info.requestUri;
        this.method = info.method;
        this.details = info.details;
        this.returnValue = info.returnValue;
    }

    public static final String LOG_TYPE_INFO = "info";
    public static final String LOG_TYPE_WARN = "warn";
    public static final String LOG_TYPE_ERROR = "error";

    private static final long serialVersionUID = 1L;

    private String id;           //日志主键
    private String opertype = LOG_TYPE_INFO;            //日志类型
    private Date createDate;           //开始时间
    private Date endDate;           //完成时间
    private String hostName;     //服务节点名称
    private String hostAddr;       //服务节点ip
    private String title;           //日志标题
    private String params;          //提交参数
    private String remoteAddr;          //请求地址
    private String requestUri;          //URI
    private String method;          //请求方式
    private String details;
    private String returnValue;

}
