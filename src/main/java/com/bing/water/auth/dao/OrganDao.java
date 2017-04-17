/**
 *
 */
package com.bing.water.auth.dao;

import com.bing.water.auth.entity.Organ;
import com.bing.water.auth.entity.Role;
import com.bing.water.common.model.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机构DAO接口
 *
 * @author xuguobing
 */
public interface OrganDao extends BaseDao<Organ> {

    Organ findByOrgCode(String orgCode);

    List<Organ> findByName(String name);

    List<Organ> findOrgansByRoles(@Param("roles") List<Role> roles);

    Organ findByUserId(@Param("userId") String userId);
}