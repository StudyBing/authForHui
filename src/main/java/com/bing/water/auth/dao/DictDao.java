/**
 * 
 */
package com.bing.water.auth.dao;

import com.bing.water.auth.entity.Dict;
import com.bing.water.common.model.BaseDao;

import java.util.List;

/**
 * 字典DAO接口
 * @author xuguobing
 */
public interface DictDao extends BaseDao<Dict> {

    List<String> findGroupType();
}