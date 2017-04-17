package com.bing.water.common.cache;


import com.bing.water.common.utils.CacheUtils;

import java.io.Serializable;
import java.util.Date;

public class CacheMeta implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6005414461794809229L;
	private String key;
	private Date createDate;
	private Date updateDate;
	
	public CacheMeta() {
		super();
	}

	public CacheMeta(String key, Date createDate, Date updateDate) {
		super();
		this.key = key;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}
	
	/**
	 * 获取该元数据对应的缓存数据
	 * @param key
	 * @return
	 */
	public Object getCacheValue(String key){
		return CacheUtils.get(key);
	}
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheMeta other = (CacheMeta) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
}
