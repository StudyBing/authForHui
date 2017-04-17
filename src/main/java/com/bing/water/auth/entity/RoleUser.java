package com.bing.water.auth.entity;

import java.io.Serializable;

/**
 * Created by xuguobing on 2016/11/16 0016.
 */
public class RoleUser implements Serializable {

    private String userId;
    private String roleId;

    public RoleUser() {
    }

    public RoleUser(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
