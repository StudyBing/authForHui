/**
 * 
 */
package com.bing.water.auth.vo;


import com.bing.water.common.model.DtGridSearch;

/**
 * 字典搜索
 * @author xuguobing
 */
public class DictSearchVo extends DtGridSearch {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}