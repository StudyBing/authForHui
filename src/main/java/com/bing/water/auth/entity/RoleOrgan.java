package com.bing.water.auth.entity;

import java.io.Serializable;

/**
 * Created by xuguobing on 2016/11/15 0015.
 */
public class RoleOrgan implements Serializable{

    private String roleId;
    private String orgId;

    public RoleOrgan() {
    }

    public RoleOrgan(String roleId, String orgId) {
        this.roleId = roleId;
        this.orgId = orgId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
