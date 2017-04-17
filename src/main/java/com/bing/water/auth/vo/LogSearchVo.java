/**
 * 
 */
package com.bing.water.auth.vo;

import com.bing.water.common.model.DtGridSearch;
import com.bing.water.common.utils.DateUtils;

import java.util.Date;

/**
 * 日志搜索
 * @author xuguobing
 */
public class LogSearchVo extends DtGridSearch {

	private String type;		// 日志类型:1.正常，2.异常
	private Date beginCreateDate;	// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}

	public Date getEndCreateDate() {
        if (endCreateDate != null && endCreateDate instanceof Date) {
            try {
                return DateUtils.addDays(DateUtils.parseDate(DateUtils.formatDate(endCreateDate, "yyyy-MM-dd"), "yyyy-MM-dd"), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
}