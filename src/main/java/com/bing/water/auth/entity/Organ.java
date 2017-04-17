/**
 * 
 */
package com.bing.water.auth.entity;

import com.bing.water.common.model.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 机构实体
 * @author xuguobing
 */
public class Organ extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Length(max=32)
	private String id;	

	@Length(max=32)
	@NotNull
	private String code;	

	@Length(max=32)
	@NotNull
	private String name;	


	public Organ() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
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