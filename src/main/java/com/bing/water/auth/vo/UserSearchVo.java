/**
 * 
 */
package com.bing.water.auth.vo;

import com.bing.water.common.model.DtGridSearch;

import java.util.List;

/**
 * 用户搜索
 * @author xuguobing
 */
public class UserSearchVo extends DtGridSearch {

	private String username;		// 登录名
	private String name;		// 名称
	private String orgCode;		// 机构代码
	private String jobNo;		// 工号
	private String idcard;		// 医生身份证号
	private String roleId; //所属的角色
	private String notInRole; //不在角色范围内

	private List<String> orgCodes;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public List<String> getOrgCodes() {
		return orgCodes;
	}

	public void setOrgCodes(List<String> orgCodes) {
		this.orgCodes = orgCodes;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getNotInRole() {
		return notInRole;
	}

	public void setNotInRole(String notInRole) {
		this.notInRole = notInRole;
	}
}