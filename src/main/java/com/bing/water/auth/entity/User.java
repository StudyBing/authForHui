/**
 *
 */
package com.bing.water.auth.entity;

import com.bing.water.common.model.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户实体
 *
 * @author xuguobing
 */
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Length(max = 32)
    private String id;

    @Length(max = 32)
    @NotNull
    private String username;    // 登录名

    @Length(max = 64)
    @NotNull
    private String password;    // 密码

    @Length(max = 32)
    @NotNull
    private String name;    // 名称

    @Length(max = 32)
    @NotNull
    private String orgCode;    // 机构代码

    @Length(max = 32)
    @NotNull
    private String jobNo;

    @Length(max = 32)
    private String idcard;    // 身份证号

    @Length(max = 16)
    private String phone;    // 联系电话

    @Length(max = 500)
    private String intro;    // 简介

    private List<String> roleIds;

    private String orgName;

    public User() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}