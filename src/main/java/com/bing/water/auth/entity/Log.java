/**
 *
 */
package com.bing.water.auth.entity;

import com.bing.water.common.utils.DateUtils;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 日志实体
 *
 * @author xuguobing
 */
public class Log {

    private static final long serialVersionUID = 1L;

    @Length(max = 32)
    private String id;    // 编号

    @Length(max = 1)
    private String type;    // 日志类型:1.正常，2.异常

    @Length(max = 32)
    private String userId;    // 创建者

    private String userName;

    private Date createDate;

    @Length(max = 255)
    private String remoteIp;    // 操作IP地址

    @Length(max = 255)
    private String userAgent;    // 用户标识

    @Length(max = 255)
    private String requestUri;    // 请求URI

    @Length(max = 5)
    private String method;    // 操作方式

    @Length(max = 65535)
    private String params;    // 操作提交的数据

    @Length(max = 65535)
    private String exception;    // 异常信息

    @Length(max = 64)
    private String title;


    private Long runTime;

    public Log() {
        super();
    }


    public String getCreateDateStr() {
        if (createDate != null) {
            return DateUtils.formatDate(createDate, "yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }

    public String getRunTimeStr() {
        if (runTime != null) {
            return DateUtils.formatDuring(runTime);
        }
        return "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

}