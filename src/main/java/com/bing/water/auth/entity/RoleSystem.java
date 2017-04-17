package com.bing.water.auth.entity;

import java.io.Serializable;

/**
 * Created by xuguobing on 2016/11/15 0015.
 */
public class RoleSystem implements Serializable {

    private String systemId;
    private String roleId;

    public RoleSystem() {
    }

    public RoleSystem(String systemId, String roleId) {
        this.systemId = systemId;
        this.roleId = roleId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
