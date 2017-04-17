/**
 * 
 */
package com.bing.water.auth.entity;

import com.bing.water.common.model.BaseEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单实体
 * @author xuguobing
 */
public class Menu extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Length(max=32)
	private String id;	

	@Length(max=32)
	private String pid;	

	@Length(max=64)
	@NotEmpty
	private String name;	// 名称

	@Length(max=64)
	private String href;	// 菜单链接

	@Length(max=64)
	private String target;	// 目标

	@Length(max=64)
	private String icon;	// 菜单图标

	@Length(max=64)
	private String permission;	// 权限标识

	@Length(max=2)
	@NotEmpty
	private String display;	// 是否显示

	@NotNull
	private Integer sort;	// 排序

	//子记录
    private List<Menu> children;

	public Menu() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}