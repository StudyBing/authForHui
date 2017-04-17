/**
 *
 */
package com.bing.water.auth.entity;

import com.bing.water.common.model.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色实体
 *
 * @author xuguobing
 */
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    @Length(max = 64)
    @NotNull
    private String name;    // 角色名称

    @Length(max = 2)
    @NotNull
    private String dataScope;    // 数据范围

    private List<String> menuIds;  //选择的资源ID

    private List<String> organIds; //选择的机构ID

    public Role() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public List<String> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<String> menuIds) {
        this.menuIds = menuIds;
    }

    public List<String> getOrganIds() {
        return organIds;
    }

    public void setOrganIds(List<String> organIds) {
        this.organIds = organIds;
    }

}