/**
 * 
 */
package com.bing.water.auth.entity;

import com.bing.water.common.model.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 字典实体
 * @author xuguobing
 */
public class Dict extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Length(max=64)
	private String id;	// 编号

	@Length(max=100)
	@NotNull
	private String type;	// 类型

	@Length(max=100)
	@NotNull
	private String label;	// 标签名

	@Length(max=100)
	@NotNull
	private String value;	// 数据值

	@NotNull
	private Integer sort;	// 排序（升序）


	public Dict() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}