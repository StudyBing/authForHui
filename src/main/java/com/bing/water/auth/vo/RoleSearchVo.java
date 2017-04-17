/**
 * 
 */
package com.bing.water.auth.vo;

import com.bing.water.common.model.DtGridSearch;

/**
 * 角色搜索
 * @author xuguobing
 */
public class RoleSearchVo extends DtGridSearch {

	private String userId;		//用户ID
	private String name;		// 角色名称

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}