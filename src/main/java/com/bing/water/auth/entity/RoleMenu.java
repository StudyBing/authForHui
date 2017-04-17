package com.bing.water.auth.entity;

import java.io.Serializable;

/**
 * Created by xuguobing on 2016/11/15 0015.
 */
public class RoleMenu implements Serializable {

    private String roleId;
    private String menuId;

    public RoleMenu() {
    }

    public RoleMenu(String roleId, String menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
