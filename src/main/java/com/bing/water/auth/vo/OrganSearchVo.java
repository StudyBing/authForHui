/**
 * 
 */
package com.bing.water.auth.vo;

import com.bing.water.common.model.DtGridSearch;

/**
 * 机构搜索
 * @author xuguobing
 */
public class OrganSearchVo extends DtGridSearch {

	private String code;		
	private String name;		


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}