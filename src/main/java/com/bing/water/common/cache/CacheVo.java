package com.bing.water.common.cache;

import java.util.List;

public class CacheVo {

	private String key;
	
	private List<CacheMeta> metas;

	public CacheVo() {
		super();
	}

	public CacheVo(String key, List<CacheMeta> metas) {
		super();
		this.key = key;
		this.metas = metas;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<CacheMeta> getMetas() {
		return metas;
	}

	public void setMetas(List<CacheMeta> metas) {
		this.metas = metas;
	}

	
}
